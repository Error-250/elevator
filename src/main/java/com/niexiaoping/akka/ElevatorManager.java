package com.niexiaoping.akka;

import javax.swing.*;

public class ElevatorManager {
    private JPanel leaderPanel;

    public ElevatorManager() {
        leaderPanel = new JPanel();
        for(FloorEnum floorEnum: FloorEnum.values()) {
            if(floorEnum.equals(FloorEnum.getMin())) {
                JRadioButton op = new JRadioButton(floorEnum.getCode() + ElevatorState.Up.toString());
                leaderPanel.add(op);
            }else if(floorEnum.equals(FloorEnum.getMax())) {
                JRadioButton op = new JRadioButton(floorEnum.getCode() + ElevatorState.Down.toString());
                leaderPanel.add(op);
            }else {
                JRadioButton op = new JRadioButton(floorEnum.getCode() + ElevatorState.Up.toString());
                leaderPanel.add(op);
                op = new JRadioButton(floorEnum.getCode() + ElevatorState.Down.toString());
                leaderPanel.add(op);
            }
        }
    }

    public JPanel getLeaderPanel() {
        return leaderPanel;
    }

    public void setDisenbleRadio(int floor, ElevatorState state) {
        String name = floor + state.toString();
        for(int i = 0;i < leaderPanel.getComponentCount();i++) {
            JRadioButton jRadioButton = (JRadioButton) leaderPanel.getComponent(i);
            if(jRadioButton.getText().equals(name)) {
                jRadioButton.setSelected(false);
                jRadioButton.setEnabled(true);
                return;
            }
        }
    }
}
