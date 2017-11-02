package com.xpto.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class FeatureEntity extends PersistentEntity<FeatureCommand, FeatureEvent, FeatureState> {

    @Override
    public Behavior initialBehavior(Optional<FeatureState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(new FeatureState(Optional.empty())));

        b.setCommandHandler(FeatureCommand.CreateFeature.class, (cmd, ctx) ->
                ctx.thenPersist(new FeatureEvent.FeatureChanged(cmd.getFeature()),
                        evt -> ctx.reply(Done.getInstance()))
        );

        b.setEventHandler(FeatureEvent.FeatureChanged.class, evt -> new FeatureState(Optional.of(evt.getFeature())));

        b.setReadOnlyCommandHandler(FeatureCommand.GetFeature.class, (item, ctx) -> ctx.reply(state().getFeature()));

        return b.build();
    }
}
