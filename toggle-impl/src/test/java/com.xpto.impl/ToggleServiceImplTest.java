package com.xpto.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import com.xpto.api.FeatureMessage;
import com.xpto.api.HeaderFilters.UserPrincipal;
import com.xpto.api.ToggleService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static play.inject.Bindings.bind;

public class ToggleServiceImplTest {

    private static ServiceTest.TestServer testServer;
    private static ToggleService toggleService;

    private final static ServiceTest.Setup setup = defaultSetup().withCassandra(true)
            .configureBuilder(b ->
                    // by default, cassandra-query-journal delays propagation of events by 10sec. In test we're using
                    // a 1 node cluster so this delay is not necessary.
                    b.configure("cassandra-query-journal.eventual-consistency-delay", "0")
                            .overrides(
                                    bind(ToggleRouter.Factory.class).to(FactoryStub.class),
                                    bind(Router.class).to(ToggleRouterStub.class))

            );

    private static FeatureMessage message;

    @BeforeClass
    public static void beforeAll() {
        testServer = ServiceTest.startServer(setup);
        toggleService = testServer.client(ToggleService.class);
        message = new FeatureMessage("1", "redButton", "1", "abc", true, true);
    }

    @AfterClass
    public static void afterAll() {
        testServer.stop();
    }

    @Test
    public void shouldBeAbleToCreateAFeature() throws Exception {
        assertThat(createFeature(message)).isInstanceOf(Done.class);
    }

    @Test
    public void shouldBeAbleToRetrieveAFeatureIfExists() throws Exception {
        createFeature(message);

        assertThat(getFeature(message.getId(), message.getVersion())).isEqualTo(message);
    }

    @Test
    public void shouldThrowNotFoundWhenFeatureDoesNotExist() throws Exception {
        assertThatExceptionOfType(ExecutionException.class).isThrownBy(() -> getFeature(message.getId(), message.getVersion())).withCauseInstanceOf(NotFound.class);
    }

    @Test
    public void shouldBeAbleToTellIfAToggleIsEnabled() throws Exception {
        createFeature(message);

        assertThat(isFeatureEnabled(message)).isTrue();
    }

    private Boolean isFeatureEnabled(FeatureMessage featureMessage) throws Exception {
        return toggleService.isEnabled(featureMessage.getId(), featureMessage.getVersion()).invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    private Done createFeature(FeatureMessage featureMessage) throws Exception {
        return toggleService.createToggle().handleRequestHeader(f -> f.withHeader("User-Token", "Xm28dxc")).invoke(featureMessage).toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    private FeatureMessage getFeature(String id, String version) throws Exception {
        return toggleService.toggle(id, version).invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    public static class ToggleRouterStub implements Router {
        @Inject
        public ToggleRouterStub() {
        }

        @Override
        public Boolean isEnabled() {
            return true;
        }

        @Override
        public Boolean isDisabled() {
            return false;
        }
    }

    public static class FactoryStub implements ToggleRouter.Factory {
        @Override
        public Router buildInstance(Optional<String> serviceName, Feature feature) {
            return new ToggleRouterStub();
        }
    }
}