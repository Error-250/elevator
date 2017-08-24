package com.niexiaoping.akka;

public enum FloorEnum {
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6);

    private int code;

    private FloorEnum(int code) {
        this.code = code;
    }

    public static FloorEnum getFloorEnumByCode(int code) {
        for(FloorEnum floorEnum : FloorEnum.values()) {
            if(floorEnum.code == code)
                return floorEnum;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public static FloorEnum getMax() {
        return FloorEnum.values()[FloorEnum.values().length - 1];
    }

    public static FloorEnum getMin() {
        return FloorEnum.values()[0];
    }
}
