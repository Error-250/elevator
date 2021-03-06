package com.niexiaoping.elevator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by error on 2017/7/8.
 */
public class ElavatorActionListener implements ActionListener {

    private ElevatorManager elevatorManager;
    public ElavatorActionListener(ElevatorManager elevatorManager) {
        this.elevatorManager = elevatorManager;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String[] args = command.split(" ");
        elevatorManager.addTarget(ElevatorFloors.values()[Integer.valueOf(args[0]).intValue() - 1], ElevatorState.valueOf(args[1].toUpperCase()));
        JButton btn = (JButton)e.getSource();
        btn.setEnabled(false);
    }
}
