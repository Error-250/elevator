package com.niexiaoping.elevator;

import javax.swing.*;
import java.util.ArrayList;

/**
 * 电梯管理器
 * Created by error on 2017/7/8.
 */
public class ElevatorManager {
    // 受管理的电梯们
    private ArrayList<Elevator> elevators;
    // 默认电梯数量为两个
    public ElevatorManager() { this(2); }
    // 设置初始化参数
    public ElevatorManager(int n) {
        elevators = new ArrayList<Elevator>();
        for(int i = 0;i < n;i++) {
            elevators.add(new Elevator());
        }
    }
    // 电梯信息显示设置
    public void setFloorLabel(JLabel... labels) {
        if(labels.length >= elevators.size()) {
            int size = elevators.size();
            for(int i = 0;i < size;i++) {
                elevators.get(i).setFloorLabel(labels[i]);
            }
        }
    }
    public void setStateLabel(JLabel... labels) {
        if(labels.length >= elevators.size()) {
            int size = elevators.size();
            for(int i = 0;i < size;i++) {
                elevators.get(i).setStateLabel(labels[i]);
            }
        }
    }
    // 自动管理运行
    public void autoRun() {
        for(Elevator elevator : elevators) {
            new Thread(elevator).start();
        }
    }
    // 添加电梯目标楼层
    public void addTarget(ElevatorFloors floor, ElevatorState state) {
        // TODO
        int floor1 = elevators.get(0).getNowFloor().ordinal();
        int floor2 = elevators.get(1).getNowFloor().ordinal();
        if(elevators.get(0).getState() == state) {

        }
        ElevatorTarget target = new ElevatorTarget();
        target.setFloor(floor);
        target.setState(state);
        elevators.get(0).addTarget(target);
    }
    // 设置电梯UI回馈参数
    public void setUI(UI ui) {
        for(Elevator elevator : elevators) {
            elevator.setUI(ui);
        }
    }
}
