package com.xpto.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleService;
import com.xpto.impl.toggleRouter.Router;
import com.xpto.impl.toggleRouter.ToggleRouter;
import lombok.Value;
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
    public void shouldBeAbleToCreateAFeatureWhenUserIsAdmin() throws Exception {
        assertThat(createFeature(message, new Header("User-Token", "Xm28dxc"))).isInstanceOf(Done.class);
    }

    @Test
    public void shouldNotBeAbleToCreateAFeatureIfIfUserIsNotAnAdmin() throws Exception {
        assertThatExceptionOfType(ExecutionException.class).isThrownBy(() ->
                createFeature(message, new Header("User-Token", "da39a3e"))).withMessage(String.valueOf(new Forbidden("You must have admin privileges")));
    }

    @Test
    public void shouldBeAbleToRetrieveAFeatureIfExists() throws Exception {
        createFeature(message, new Header("User-Token", "Xm28dxc"));

        assertThat(getFeature(message.getId(), message.getVersion())).isEqualTo(message);
    }

    @Test
    public void shouldThrowNotFoundWhenFeatureDoesNotExist() throws Exception {
        assertThatExceptionOfType(ExecutionException.class).isThrownBy(() -> getFeature("2", "3")).withCauseInstanceOf(NotFound.class);
    }

    @Test
    public void shouldBeAbleToTellIfAToggleIsEnabled() throws Exception {
        createFeature(message, new Header("User-Token", "Xm28dxc"));

        assertThat(isFeatureEnabled(message)).isTrue();
    }

    private Boolean isFeatureEnabled(FeatureMessage featureMessage) throws Exception {
        return toggleService.isEnabled(featureMessage.getId(), featureMessage.getVersion()).invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    private Done createFeature(FeatureMessage featureMessage, Header header) throws Exception {
        return toggleService.createToggle().handleRequestHeader(f -> f.withHeader(header.getName(), header.getValue())).invoke(featureMessage).toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    private FeatureMessage getFeature(String id, String version) throws Exception {
        return toggleService.toggle(id, version).invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
    }

    @Value
    public static class Header {
        private final String name;
        private final String value;
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