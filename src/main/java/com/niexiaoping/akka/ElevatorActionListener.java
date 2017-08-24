package com.niexiaoping.akka;

import akka.actor.ActorRef;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorActionListener implements ActionListener {
    private ActorRef elevatorActor;

    public ElevatorActionListener(ActorRef actorRef) {
        this.elevatorActor = actorRef;
    }
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        FloorEnum floorEnum = FloorEnum.valueOf(command);
        JRadioButton jRadioButton = (JRadioButton) e.getSource();
        jRadioButton.setEnabled(false);
        elevatorActor.tell(new ElevatorActor.FloorMessage(floorEnum.getCode()), ActorRef.noSender());
    }
}
