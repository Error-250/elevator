package com.niexiaoping.elevator;

import javax.swing.*;
import java.awt.*;

/**
 * Created by error on 2017/7/8.
 */
public class UI {
    private JFrame jFrame;
    private JPanel elevator1;
    private JPanel elevator2;
    private JPanel user;
    private JLabel elevator1Label;
    private JLabel elevator2Label;
    private JLabel elevator1Floor;
    private JLabel elevator2Floor;
    private JLabel elevator1State;
    private JLabel elevator2State;
    private JButton floor1up;
    private JButton floor2up;
    private JButton floor2down;
    private JButton floor3up;
    private JButton floor3down;
    private JButton floor4up;
    private JButton floor4down;
    private JButton floor5up;
    private JButton floor5down;
    private JButton floor6up;
    private JButton floor6down;
    private JButton floor7up;
    private JButton floor7down;
    private JButton floor8up;
    private JButton floor8down;
    private JButton floor9up;
    private JButton floor9down;
    private JButton floor10down;

    public UI(ElevatorManager elevatorManager) {
        jFrame = new JFrame("Elevator");
        elevator1 = new JPanel();
        elevator2 = new JPanel();
        user = new JPanel();

        elevator1Label = new JLabel("Elevator1");
        elevator2Label = new JLabel("Elevator2");
        elevator1Floor = new JLabel("Floor: 1");
        elevator2Floor = new JLabel("Floor: 1");
        elevator1State = new JLabel("State: STAY");
        elevator2State = new JLabel("State: STAY");

        floor1up = new JButton("1 up");
        floor2up = new JButton("2 up");
        floor2down = new JButton("2 down");
        floor3up = new JButton("3 up");
        floor3down = new JButton("3 down");
        floor4up = new JButton("4 up");
        floor4down = new JButton("4 down");
        floor5up = new JButton("5 up");
        floor5down = new JButton("5 down");
        floor6up = new JButton("6 up");
        floor6down = new JButton("6 down");
        floor7up = new JButton("7 up");
        floor7down = new JButton("7 down");
        floor8up = new JButton("8 up");
        floor8down = new JButton("8 down");
        floor9up = new JButton("9 up");
        floor9down = new JButton("9 down");
        floor10down = new JButton("10 down");

        floor1up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor2up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor2down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor3up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor3down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor4up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor4down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor5up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor5down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor6up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor6down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor7up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor7down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor8up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor8down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor9up.addActionListener(new ElavatorActionListener(elevatorManager));
        floor9down.addActionListener(new ElavatorActionListener(elevatorManager));
        floor10down.addActionListener(new ElavatorActionListener(elevatorManager));

        elevator1.add(elevator1Label);
        elevator1.add(elevator1Floor);
        elevator1.add(elevator1State);
        elevator2.add(elevator2Label);
        elevator2.add(elevator2Floor);
        elevator2.add(elevator2State);
        user.setLayout(new BoxLayout(user, BoxLayout.Y_AXIS));
        user.add(floor1up);

        user.add(floor2up);
        user.add(floor2down);
        user.add(floor3up);
        user.add(floor3down);
        user.add(floor4up);
        user.add(floor4down);
        user.add(floor5up);
        user.add(floor5down);
        user.add(floor6up);
        user.add(floor6down);
        user.add(floor7up);
        user.add(floor7down);
        user.add(floor8up);
        user.add(floor8down);
        user.add(floor9up);
        user.add(floor9down);
        user.add(floor10down);
        jFrame.add(elevator1, BorderLayout.WEST);
        jFrame.add(elevator2, BorderLayout.CENTER);
        jFrame.add(user, BorderLayout.EAST);
        jFrame.setVisible(true);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(3);
    }

    public JLabel getElevator1Floor() {
        return elevator1Floor;
    }

    public JLabel getElevator2Floor() {
        return elevator2Floor;
    }

    public JLabel getElevator1State() {
        return elevator1State;
    }

    public JLabel getElevator2State() {
        return elevator2State;
    }

    public void setButtonEnabled(ElevatorTarget target) {
        int count = user.getComponentCount();
        String targetName = target.getFloor().ordinal() + 1 + " " + target.getState().toString().toLowerCase();
        for(int i = 0;i < count;i++) {
            JButton btn = (JButton) user.getComponent(i);
            if(btn.getText().equals(targetName)) {
                btn.setEnabled(true);
            }
        }
    }
}
