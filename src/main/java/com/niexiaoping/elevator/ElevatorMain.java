package com.niexiaoping.elevator;


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
        elevatorManager.setFloorLabel(ui.getElevator1Floor(), ui.getElevator2Floor());
        elevatorManager.setStateLabel(ui.getElevator1State(), ui.getElevator2State());
        elevatorManager.setUI(ui);
        // 自动运行电梯
        elevatorManager.autoRun();
    }
}
