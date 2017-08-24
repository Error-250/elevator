package com.niexiaoping.elevator.dev;

import com.niexiaoping.elevator.ElevatorFloors;
import com.niexiaoping.elevator.ElevatorState;
import com.niexiaoping.elevator.ElevatorTarget;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by error on 2017/7/9.
 */
public class ElevatorManager {
    // 电梯公共目标
    public ArrayList<ElevatorTarget> targets;
    // 受管理的电梯们
    private ArrayList<Elevator> elevators;
    // 电梯UI
    private UI ui;

    public ElevatorManager() {
        elevators = new ArrayList<>();
        elevators.add(new Elevator());
        elevators.add(new Elevator());
        targets = new ArrayList<>();
    }
    public void setJpanel(JPanel... panels) {
        if(panels.length >= elevators.size()) {
            for(int i = 0;i < elevators.size();i++) {
                elevators.get(i).setShowInfoUI(panels[i]);
            }
        }
    }
    // 自动运行
    public void autoRun() {
        for(Elevator elevator : elevators) {
            new Thread(elevator).start();
        }
    }
    // 电梯回馈显示
    public void setButtonBack(ElevatorTarget target) {
        ui.setButtonEnabled(target);
    }
    // 添加目标
    public void addTarget(ElevatorFloors floor, ElevatorState state) {
        ElevatorTarget target = new ElevatorTarget();
        target.setFloor(floor);
        target.setState(state);
        targets.add(target);
        activeElevator();
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }
    // 激活电梯
    private void activeElevator() {
        for(Elevator elevator : elevators) {
            if(elevator.getState().equals(ElevatorState.STAY)) {
                synchronized (elevator) {
                    elevator.notify();
                }
                return;
            }
        }
    }
}
