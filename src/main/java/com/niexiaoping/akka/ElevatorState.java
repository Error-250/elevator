package com.niexiaoping.akka;

public enum ElevatorState {
    Up(1, "Upping"),
    Down(2, "Downing"),
    Running(3, "Running"), //  分割电梯正常运行状态和未运行状态
    Stop(4, "Stopped"),
    Stay(5, "Stay"),
    Error(6, "Error");

    private int code;
    private String desc;

    private ElevatorState (int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ElevatorState getElevatorStateByCode(int code) {
        for(ElevatorState state: ElevatorState.values()) {
            if(state.code == code)
                return state;
        }
        return null;
    }

    public static ElevatorState getElevatorStateByDesc(String desc) {
        for(ElevatorState state: ElevatorState.values()) {
            if(state.desc.equals(desc))
                return state;
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
