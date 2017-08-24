package com.niexiaoping.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Collections;
import java.util.HashMap;

public class ElevatorActor extends AbstractActor {

    private ElevatorState state = ElevatorState.Stay; // 状态信息
    private FloorEnum floor = FloorEnum.One;
    private HashMap<Integer, Boolean> que = new HashMap<>();

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef leader;
    private Elevator elevator;

    public ElevatorActor(Elevator elevator, ActorRef leader) {
        this.leader = leader;
        this.elevator = elevator;
        elevator.setFloor(floor.getCode());
        elevator.setState(state);
    }

    private void changeState(ElevatorState state) {
        this.state = state;
        elevator.setState(state);
    }
    // 电梯暂停
    public ElevatorState elevatorStay() {
        ElevatorState oldState = this.state;
        if(que.containsKey(floor.getCode())) {
            que.remove(floor.getCode());
            elevator.setDisableBtn(floor.toString());
        }
        changeState(ElevatorState.Stay);
        try {
            log.info("Staying...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            changeState(ElevatorState.Error);
        }
        return oldState;
    }
    // 电梯运行
    public void elevatorRun() {
        if (state.getCode() < ElevatorState.Running.getCode()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                changeState(ElevatorState.Error);
            }
            if(state.equals(ElevatorState.Up))
                floor = FloorEnum.getFloorEnumByCode(floor.getCode() + 1);
            if(state.equals(ElevatorState.Down))
                floor = FloorEnum.getFloorEnumByCode(floor.getCode() - 1);
            // 到顶或到底
            if(state.equals(ElevatorState.Up) && floor.equals(FloorEnum.getMax()))
                changeState(ElevatorState.Down);
            if(state.equals(ElevatorState.Down) && floor.equals(FloorEnum.getMin()))
                changeState(ElevatorState.Up);
            elevator.setFloor(floor.getCode());
            reportState(floor.getCode(), state);
        }
        log.info("Now: {} State: {}", floor.getCode(), state.getDesc());
    }

    public void reportState(int floor, ElevatorState state) {
        leader.tell(new ElevatorLeader.ElevatorMessage(floor, state), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(LeaderAnswer.class, leaderAnswer -> {
                    log.info("elevator get hasTask->{} state->{}",leaderAnswer.taskState, leaderAnswer.state);
                    if(leaderAnswer.taskState.equals(TaskState.NewTask)) {
                        if(state.equals(ElevatorState.Stay)) {
                            // 唤醒电梯
                            changeState(leaderAnswer.state);
                            elevatorRun();
                        }
                        // 其他指令忽略
                    } else if(leaderAnswer.taskState.equals(TaskState.SelectTask)) {
                        // 当没有任务或者任务中的最后一个与可选任务一致时  可以执行
                        int floorLast = -1;
                        if(que.size() > 0)
                            floorLast = state.equals(ElevatorState.Up) ? Collections.max(que.keySet()) : Collections.min(que.keySet());
                        if(que.size() == 0 || floor.getCode() == floorLast) {
                            if(state.equals(ElevatorState.Up))
                                changeState(ElevatorState.Down);
                            else if(state.equals(ElevatorState.Down))
                                changeState(ElevatorState.Up);
                            reportState(floor.getCode(), state);
                        }
                    }else {
                        if(que.size() == 0) {
                            if(elevator.isBtnActive()) {
                                elevator.setDisableAll();
                            }
                            if(leaderAnswer.state.equals(ElevatorState.Stay)) {
                                ElevatorState oldState = elevatorStay();
                                if(leaderAnswer.taskState.equals(TaskState.HasTask)) {
                                    reportState(floor.getCode(), oldState);
                                }
                            }else {
                                changeState(leaderAnswer.state);
                                elevatorRun();
                            }
                        }else {
                            if(leaderAnswer.state.equals(ElevatorState.Stay)) {
                                ElevatorState oldState = state;
                                if(leaderAnswer.taskState.equals(TaskState.HasTask))
                                    oldState = elevatorStay();
                                if (que.size() == 0) {
                                    getSender().tell(new ElevatorLeader.ElevatorMessage(floor.getCode(), ElevatorState.Stay), getSelf());
                                } else {
                                    changeState(oldState);
                                    elevatorRun();
                                }
                            }else {
                                elevatorRun();
                            }
                            // 其他指令忽略
                            if(que.containsKey(floor.getCode())) {
                                ElevatorState oldSate = elevatorStay();
                                changeState(oldSate);
                            }
                        }
                    }
                })
                .match(FloorMessage.class, floorMessage -> {
                    log.info("get floor message floor->{}", floorMessage.floor);
                    if(state.equals(ElevatorState.Stay)) {
                        que.put(floorMessage.floor, true);
                        if(floor.getCode() < floorMessage.floor)
                            changeState(ElevatorState.Up);
                        if(floor.getCode() > floorMessage.floor)
                            changeState(ElevatorState.Down);
                        elevatorRun();
                    } else if(state.equals(ElevatorState.Up) && floor.getCode() < floorMessage.floor) {
                        que.put(floorMessage.floor, true);
                    }else if(state.equals(ElevatorState.Down) && floor.getCode() > floorMessage.floor) {
                        que.put(floorMessage.floor, true);
                    }
                })
                .build();
    }

    @Override
    public void preStart() throws Exception {
        leader.tell(new ElevatorLeader.ElevatorMessage(floor.getCode(), state), getSelf());
        super.preStart();
    }

    static public Props props(Elevator elevator, ActorRef leader) {
        return Props.create(ElevatorActor.class, () -> new ElevatorActor(elevator, leader));
    }

    static class LeaderAnswer {
        private final TaskState taskState;
        private final ElevatorState state;

        public LeaderAnswer(TaskState taskState, ElevatorState state) {
            this.taskState = taskState;
            this.state = state;
        }
    }

    static class FloorMessage {
        private final int floor;

        public FloorMessage(int floor) {
            this.floor = floor;
        }
    }
}
