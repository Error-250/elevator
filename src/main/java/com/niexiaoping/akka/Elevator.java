package com.niexiaoping.akka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Elevator {
    private JPanel elevator;  // 电梯
    private JPanel showPanel; // 显示区
    private JLabel showName;  // 显示名字
    private JLabel showState; // 显示状态
    private JLabel showFloor; // 显示楼层
    private JPanel btnPanel;  // 按钮区

    public Elevator(String name) {
        elevator = new JPanel(new BorderLayout());
        showPanel = new JPanel(new GridLayout(3, 1));
        btnPanel = new JPanel(new GridLayout(FloorEnum.values().length, 1));
        showName = new JLabel(name);
        showState = new JLabel("");
        showFloor = new JLabel("");

        showPanel.add(showName);
        showPanel.add(showState);
        showPanel.add(showFloor);
        for(FloorEnum floorEnum : FloorEnum.values()) {
            JRadioButton jRadioButton = new JRadioButton(floorEnum.toString());
            btnPanel.add(jRadioButton);
        }

        elevator.add(btnPanel, BorderLayout.CENTER);
        elevator.add(showPanel, BorderLayout.NORTH);
    }

    public JPanel getElevator() {
        return elevator;
    }

    public void setActionListener(ActionListener actionListener) {
        for(int i = 0;i < btnPanel.getComponentCount();i++) {
            JRadioButton jRadioButton = (JRadioButton) btnPanel.getComponent(i);
            jRadioButton.addActionListener(actionListener);
        }
    }

    public boolean isBtnActive() {
        for(int i = 0;i < btnPanel.getComponentCount();i++) {
            JRadioButton jRadioButton = (JRadioButton) btnPanel.getComponent(i);
            if(jRadioButton.isSelected())
                return true;
        }
        return false;
    }

    public void setDisableBtn(String name) {
        for(int i = 0;i < btnPanel.getComponentCount();i++) {
            JRadioButton jRadioButton = (JRadioButton) btnPanel.getComponent(i);
            if(jRadioButton.getText().equals(name)) {
                jRadioButton.setSelected(false);
                jRadioButton.setEnabled(true);
                return;
            }
        }
    }

    public void setDisableAll() {
        for(int i = 0;i < btnPanel.getComponentCount();i++) {
            JRadioButton jRadioButton = (JRadioButton) btnPanel.getComponent(i);
            jRadioButton.setSelected(false);
            jRadioButton.setEnabled(true);
        }
    }

    public void setState(ElevatorState state) {
        showState.setText(state.getDesc());
    }

    public void setFloor(int floor) {
        showFloor.setText(String.valueOf(floor));
    }
}
