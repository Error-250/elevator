package com.niexiaoping.akka;

import akka.actor.ActorRef;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerActionListener implements ActionListener {
    private ActorRef leader;
    public ManagerActionListener(ActorRef leader) {
        this.leader = leader;
    }
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        ElevatorState state = null;
        Integer floor = null;
        if(command.indexOf(ElevatorState.Up.toString()) != -1) {
            state = ElevatorState.Up;
            floor = Integer.valueOf(command.replaceAll(ElevatorState.Up.toString(), "").trim());
        }else if(command.indexOf(ElevatorState.Down.toString()) != -1) {
            state = ElevatorState.Down;
            floor = Integer.valueOf(command.replaceAll(ElevatorState.Down.toString(), "").trim());
        }
        JRadioButton jRadioButton = (JRadioButton) e.getSource();
        jRadioButton.setEnabled(false);
        if(floor != null && state != null) {
            leader.tell(new ElevatorLeader.Message(floor, state), ActorRef.noSender());
        }
    }
}
