package com.xpto.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Source;
import akka.stream.testkit.TestSubscriber;
import akka.stream.testkit.javadsl.TestSink;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.testkit.ProducerStub;
import com.lightbend.lagom.javadsl.testkit.ProducerStubFactory;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import com.xpto.api.AbcService;
import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleMessage;
import com.xpto.api.ToggleService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import javax.inject.Inject;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.eventually;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static org.assertj.core.api.Assertions.assertThat;
import static play.inject.Bindings.bind;

public class AbcServiceImplTest {

    private static ServiceTest.TestServer testServer;
    private static AbcService abcService;
    private static ToggleService toggleService;

    private final static ServiceTest.Setup setup = defaultSetup().withCassandra(true)
            .configureBuilder(b ->
                    // by default, cassandra-query-journal delays propagation of events by 10sec. In test we're using
                    // a 1 node cluster so this delay is not necessary.
                    b.configure("cassandra-query-journal.eventual-consistency-delay", "0")
                    .overrides(bind(ToggleService.class).to(ToggleServiceStub.class),
                               bind(AbcService.class).to(AbcServiceImpl.class))

            );

    private static ProducerStub<FeatureMessage> toggleProducer;

    @BeforeClass
    public static void beforeAll() {
        testServer = ServiceTest.startServer(setup);
        abcService = testServer.client(AbcService.class);
    }

    @AfterClass
    public static void afterAll() {
        testServer.stop();
    }

    @Test
    public void shouldReceiveUpSteam() {
        FeatureMessage message = new FeatureMessage("1", "redButton", "1", "abc", true, true);

        toggleProducer.send(message);

        eventually(new FiniteDuration(5, TimeUnit.SECONDS), () -> {
            Source<ToggleMessage, NotUsed> toggleStream = testServer.client(AbcService.class).toggleStream().invoke().toCompletableFuture().get(3, TimeUnit.SECONDS);
            TestSubscriber.Probe<ToggleMessage> probe = toggleStream.runWith(TestSink.probe(testServer.system()), testServer.materializer());
            probe.request(10);

            assertThat(probe.requestNext()).isEqualTo(EntityMapper.toToggleMessage(message));
        });
    }

    static  class ToggleServiceStub implements ToggleService {
        @Inject
        ToggleServiceStub(ProducerStubFactory producerFactory) {
            toggleProducer = producerFactory.producer("feature-changed");
        }

        @Override
        public ServiceCall<NotUsed, FeatureMessage> toggle(String id, String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServiceCall<FeatureMessage, Done> createToggle() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServiceCall<NotUsed, Boolean> isEnabled(String id, String version) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Topic<FeatureMessage> featureChanged() {
            return toggleProducer.topic();
        }
    }
}
