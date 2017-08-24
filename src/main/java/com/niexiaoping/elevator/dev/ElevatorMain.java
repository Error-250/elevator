package com.niexiaoping.elevator.dev;

/**
 * 工程入口
 * Created by error on 2017/7/8.
 */
public class ElevatorMain {
    public static ElevatorManager elevatorManager;

    public static void main(String[] args) {
        // Elevator管理器
        elevatorManager = new ElevatorManager();
        // UI界面
        UI ui = new UI(elevatorManager);
        // 设置电梯参数
        elevatorManager.setJpanel(ui.getElevator1(), ui.getElevator2());
        elevatorManager.setUi(ui);
        // 自动运行电梯
        elevatorManager.autoRun();
    }
}
