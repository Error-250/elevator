package com.niexiaoping.elevator.dev;

import com.niexiaoping.elevator.ElevatorFloors;
import com.niexiaoping.elevator.ElevatorState;
import com.niexiaoping.elevator.ElevatorTarget;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by error on 2017/7/9.
 */
public class Elevator implements Runnable {
    // 电梯自己的目标
    private ArrayList<ElevatorFloors> targets;
    // 电梯自己的状态
    private ElevatorState state;
    // 电梯当前楼层
    private ElevatorFloors nowFloor;
    // 电梯状态显示
    private JPanel showInfoUI;

    public Elevator() {
        nowFloor = ElevatorFloors.ONE;
        state = ElevatorState.STAY;
        targets = new ArrayList<>();
    }

    @Override
    public void run() {
        while(state.compareTo(ElevatorState.STAY) >= 0) {
            //  检查是否还有目标
            checkTarget();
            showState();
            if(state == ElevatorState.UP)
                goUp();
            else if(state == ElevatorState.DOWN)
                goDown();
            else if(state == ElevatorState.STAY) {
                try {
                    synchronized (this) {
                        wait();
                    }
                }catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                    state = ElevatorState.ERROR;
                }
            }
        }
        // 显示出错状态
        showState();
    }

    public void setShowInfoUI(JPanel showInfoUI) {
        this.showInfoUI = showInfoUI;
    }

    public void addTarget(ElevatorFloors floor) {
        final ElevatorFloors now = nowFloor;
        targets.add(floor);
        if(state == ElevatorState.STAY)
            return;
        targets.sort(new Comparator<ElevatorFloors>() {
            @Override
            public int compare(ElevatorFloors o1, ElevatorFloors o2) {
                int base = now.ordinal();
                int t1 = o1.ordinal() - base;
                int t2 = o2.ordinal() - base;
                //
                if(state == ElevatorState.UP) {
                    if(t1 < 0)
                        t1 = ElevatorFloors.TEN.ordinal() + Math.abs(t1);
                    if(t2 < 0)
                        t2 = ElevatorFloors.TEN.ordinal() + Math.abs(t2);
                }
                if(t1 < t2)
                    return -1;
                else if(t1 > t2)
                    return 1;
                return 0;
            }
        });
    }
    // 检查目标状态
    private void checkTarget() {
        if(ElevatorMain.elevatorManager.targets.size() == 0 && targets.size() == 0) {
            state = ElevatorState.STAY;
            return;
        }
        // 以targets为主, 当没到一个楼层检查公共楼层是否有一致的Target
        ElevatorFloors targetFloor;
        if(targets.size() > 0) {
            //
            targetFloor = targets.get(0);
        }else {
            targetFloor = ElevatorMain.elevatorManager.targets.get(0).getFloor();
        }
        // 更改电梯状态
        int compare = nowFloor.compareTo(targetFloor);
        if(compare < 0)
            state = ElevatorState.UP;
        else if(compare > 0)
            state = ElevatorState.DOWN;
        else {
            try {
                buttonBack(nowFloor);
                // 开门
                state = ElevatorState.OPEN;
                showState();
                Thread.sleep(1000);
                // 关门
                state = ElevatorState.CLOSE;
                showState();
                Thread.sleep(1000);
                // 下一个目标
                checkTarget();
            }catch (java.lang.InterruptedException e) {
                e.printStackTrace();
                state = ElevatorState.ERROR;
            }
        }
    }

    private void buttonBack(ElevatorFloors floor) {
        if(targets.size() > 0 && floor.compareTo(targets.get(0)) == 0) {
            targets.remove(0);
        }else {
            ElevatorTarget tmpTarget = new ElevatorTarget();
            for(ElevatorTarget target : ElevatorMain.elevatorManager.targets) {
                if(target.getFloor().equals(nowFloor)) {
                    if(target.getState().equals(state)) {
                        tmpTarget = target;
                        break;
                    }else {
                        if(state.equals(tmpTarget.getState()))
                            break;
                        else
                            tmpTarget = target;
                    }
                }
            }
            // 回显电梯信息
            ElevatorMain.elevatorManager.setButtonBack(tmpTarget);
        }
    }
    // 显示电梯状态
    private void showState(){
        JLabel floorLabel = (JLabel) showInfoUI.getComponent(1);
        JLabel stateLabel = (JLabel) showInfoUI.getComponent(2);
        floorLabel.setText("Floor: " + (nowFloor.ordinal() + 1));
        stateLabel.setText("State: " + state.toString());
    }
    // 电梯上行
    private void goUp(){
        try {
            Thread.sleep(1000);
            nowFloor = ElevatorFloors.values()[nowFloor.ordinal() + 1];
        }catch (java.lang.InterruptedException e) {
            e.printStackTrace();
            state = ElevatorState.ERROR;
        }
    }
    // 电梯下行
    private void goDown(){
        try {
            Thread.sleep(1000);
            nowFloor = ElevatorFloors.values()[nowFloor.ordinal() - 1];
        }catch (java.lang.InterruptedException e) {
            e.printStackTrace();
            state = ElevatorState.ERROR;
        }
    }

    public ElevatorState getState() {
        return state;
    }
}
