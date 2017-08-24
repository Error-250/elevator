package com.niexiaoping.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import javax.swing.*;
import java.awt.*;

public class MainUI {
    public static void main(String[] args) {
        JFrame win = new JFrame("Elevator");
        JPanel elevators = new JPanel(new GridLayout(1, 1));
        Elevator elevator = new Elevator("elevator1");
        ElevatorManager elevatorManager = new ElevatorManager();

        final ActorSystem system = ActorSystem.create("testElevator");
        ActorRef leader = system.actorOf(ElevatorLeader.props(elevatorManager), "leader");
        ActorRef elevatorActor = system.actorOf(ElevatorActor.props(elevator, leader), "elevator1");

        elevator.setActionListener(new ElevatorActionListener(elevatorActor));
        elevators.add(elevator.getElevator());

        for(int i = 0;i < elevatorManager.getLeaderPanel().getComponentCount();i++) {
            JRadioButton jRadioButton = (JRadioButton) elevatorManager.getLeaderPanel().getComponent(i);
            jRadioButton.addActionListener(new ManagerActionListener(leader));
        }
        win.setVisible(true);
        win.add(elevators);
        win.add(elevatorManager.getLeaderPanel(), BorderLayout.SOUTH);
        win.pack();
        win.setDefaultCloseOperation(3);
    }
}
