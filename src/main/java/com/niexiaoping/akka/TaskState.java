package com.niexiaoping.akka;

public enum TaskState {
    NewTask(1, "NewTask"),
    HasTask(2, "HasTask"),
    NoTask(3, "NoTasks"),
    SelectTask(4, "CanSelectTask");

    private int code;
    private String desc;

    private TaskState(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
