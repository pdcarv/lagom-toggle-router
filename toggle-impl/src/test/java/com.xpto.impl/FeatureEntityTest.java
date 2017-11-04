package com.xpto.impl;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureEntityTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void shouldCreateFeature() {
        PersistentEntityTestDriver<FeatureCommand, FeatureEvent, FeatureState> driver = new PersistentEntityTestDriver<>(system, new FeatureEntity(), "1");

        Feature newFeature = new Feature("1", "redButton", "1", "abc", true, true);
        PersistentEntityTestDriver.Outcome<FeatureEvent, FeatureState> outcome = driver.run(new FeatureCommand.CreateFeature(newFeature));
        assertThat(outcome.events().get(0)).isEqualTo(new FeatureEvent.FeatureChanged(newFeature));
    }

}
