package com.niexiaoping.elevator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by error on 2017/7/8.
 */
public class Elevator implements Runnable {
    // 电梯状态
    private ElevatorState state;
    // 电梯当前楼层
    private ElevatorFloors nowFloor;
    // 电梯目标
    private ArrayList<ElevatorTarget> targets;
    // 电梯显示
    private JLabel floorLabel;
    // 电梯状态显示
    private JLabel stateLabel;
    private UI ui;

    // 初始化
    public Elevator() {
        state = ElevatorState.STAY;
        nowFloor = ElevatorFloors.ONE;
        targets = new ArrayList<ElevatorTarget>();
    }

    // 电梯自动运行
    public void run() {
        while(state != ElevatorState.STOP || state != ElevatorState.ERROR) {
            stateLabel.setText("State: " + state.toString());
            if(targets.size() > 0)
                checkState();
            if(state == ElevatorState.UP)
                up();
            else if(state == ElevatorState.DOWN)
                down();
        }
    }

    public ElevatorState getState() {
        return state;
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }

    public ElevatorFloors getNowFloor() {
        return nowFloor;
    }

    public void setNowFloor(ElevatorFloors nowFloor) {
        this.nowFloor = nowFloor;
    }

    public void addTarget(ElevatorTarget target) {
        this.targets.add(target);
        targetSort();
    }

    public void setFloorLabel(JLabel floorLabel) {
        this.floorLabel = floorLabel;
    }

    public void setStateLabel(JLabel stateLabel) {
        this.stateLabel = stateLabel;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    private void checkState(){
        if(targets.size() == 0)
            state = ElevatorState.STAY;
        else {
            ElevatorFloors first = targets.get(0).getFloor();
            int compare = nowFloor.compareTo(first);
            if(compare < 0) {
                state = ElevatorState.UP;
            }else if(compare > 0) {
                state = ElevatorState.DOWN;
            }else {
                try{
                    System.out.println("Stay 1s at" + (nowFloor.ordinal() + 1));
                    ElevatorTarget last = targets.remove(0);
                    ui.setButtonEnabled(last);
                    state = ElevatorState.STAY;
                    Thread.sleep(1000);
                }catch (java.lang.InterruptedException e) {
                    System.out.println("Stay Error.");
                    state = ElevatorState.ERROR;
                }
            }
        }
    }

    private void up() {
        try {
            Thread.sleep(1000);
            nowFloor = ElevatorFloors.values()[nowFloor.ordinal() + 1];
            floorLabel.setText("Floor: " + (nowFloor.ordinal() + 1));
        }catch (java.lang.InterruptedException e) {
            e.printStackTrace();
            this.state = ElevatorState.ERROR;
        }
    }

    private void down() {
        try {
            Thread.sleep(1000);
            nowFloor = ElevatorFloors.values()[nowFloor.ordinal() - 1];
            floorLabel.setText("Floor: " + (nowFloor.ordinal() + 1));
        }catch (java.lang.InterruptedException e) {
            e.printStackTrace();
            this.state = ElevatorState.ERROR;
        }
    }

    private void targetSort() {
        if(state == ElevatorState.STAY)
            return;
        targets.sort(new Comparator<ElevatorTarget>() {
            public int compare(ElevatorTarget o1, ElevatorTarget o2) {
                if(o1.getState().compareTo(o2.getState()) != 0){
                    if(o1.getState().compareTo(state) == 0)
                        return -1;
                    else
                        return 1;
                }else {
                    return o1.getFloor().compareTo(o2.getFloor());
                }
            }
        });
    }
}
