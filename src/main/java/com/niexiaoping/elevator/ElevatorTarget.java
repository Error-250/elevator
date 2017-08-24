package com.niexiaoping.elevator;

/**
 * Created by error on 2017/7/8.
 */
public class ElevatorTarget {
    private ElevatorFloors floor;
    private ElevatorState state;

    public ElevatorFloors getFloor() {
        return floor;
    }

    public void setFloor(ElevatorFloors floor) {
        this.floor = floor;
    }

    public ElevatorState getState() {
        return state;
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }
}
