package com.xpto.impl;


import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class AbcEntity extends PersistentEntity<AbcCommand, AbcEvent, AbcState> {

    @Override
    public Behavior initialBehavior(Optional<AbcState> snapshotState) {
        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(new AbcState(false)));

        b.setCommandHandler(AbcCommand.UpdateConfig.class, (cmd, ctx) ->
                ctx.thenPersist(new AbcEvent.UpdateConfig(cmd.getToggle()),
                        evt -> ctx.reply(Done.getInstance())));

        b.setEventHandler(AbcEvent.UpdateConfig.class, evt -> new AbcState(true));
        return b.build();
    }
}
