package com.niexiaoping.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Collections;
import java.util.HashMap;

public class ElevatorLeader extends AbstractActor {
    private ElevatorManager elevatorManager;

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private HashMap<ActorRef, Integer> actorRefList = new HashMap<>();
    private HashMap<String, Boolean> tasks = new HashMap<>();

    public ElevatorLeader(ElevatorManager elevatorManager) {
        this.elevatorManager = elevatorManager;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ElevatorMessage.class, elevatorMessage -> {
                    log.info("Leader Got: floor->{}, state->{}", elevatorMessage.floor, elevatorMessage.state.getDesc());
                    // 保存最新状态
                    ActorRef sender = getSender();
                    if(!actorRefList.containsKey(sender)) {
                        actorRefList.put(sender, elevatorMessage.floor);
                    }else {
                        actorRefList.replace(sender, elevatorMessage.floor);
                    }
                    // 准备回复
                    if(tasks.containsKey(elevatorMessage.floor + elevatorMessage.state.getDesc())) {
                        tasks.remove(elevatorMessage.floor + elevatorMessage.state.getDesc());
                        elevatorManager.setDisenbleRadio(elevatorMessage.floor, elevatorMessage.state);
                        getSender().tell(new ElevatorActor.LeaderAnswer(TaskState.HasTask, ElevatorState.Stay), getSelf());
                    }else {
                        if(tasks.size() > 0) {
                            String target = tasks.keySet().toArray()[0].toString();
                            if (elevatorMessage.state.equals(ElevatorState.Up))
                                target = Collections.max(tasks.keySet());
                            if (elevatorMessage.state.equals(ElevatorState.Down))
                                target = Collections.min(tasks.keySet());
                            int targetFloor = Integer.valueOf(target.replaceAll(ElevatorState.Up.getDesc(), "").replaceAll(ElevatorState.Down.getDesc(), "").trim());
                            if(targetFloor == elevatorMessage.floor)
                                getSender().tell(new ElevatorActor.LeaderAnswer(TaskState.SelectTask, ElevatorState.Stay), getSelf());
                            else
                                getSender().tell(new ElevatorActor.LeaderAnswer(TaskState.HasTask, targetFloor > elevatorMessage.floor ? ElevatorState.Up : ElevatorState.Down), getSelf());
                        }else {
                            getSender().tell(new ElevatorActor.LeaderAnswer(TaskState.NoTask, ElevatorState.Stay), getSelf());
                        }
                    }
                })
                .match(Message.class, message -> {
                    log.info("Leader got: floor->{}, state->{}", message.floor, message.state.getDesc());
                    tasks.put(message.floor + message.state.getDesc(), true);
                    actorRefList.forEach((actorRef, integer) -> {
                        actorRef.tell(new ElevatorActor.LeaderAnswer(TaskState.NewTask, message.floor > integer? ElevatorState.Up: ElevatorState.Down), getSelf());
                    });
                })
                .build();
    }

    static public Props props(ElevatorManager elevatorManager) {
        return Props.create(ElevatorLeader.class, () -> new ElevatorLeader(elevatorManager));
    }

    static class ElevatorMessage {
        private final int floor; // 楼层
        private final ElevatorState state; // 状态

        public ElevatorMessage(int floor, ElevatorState state) {
            this.floor = floor;
            this.state = state;
        }
    }

    static class Message {
        private final int floor;
        private final ElevatorState state;

        public Message(int floor, ElevatorState state) {
            this.floor = floor;
            this.state = state;
        }
    }
}
