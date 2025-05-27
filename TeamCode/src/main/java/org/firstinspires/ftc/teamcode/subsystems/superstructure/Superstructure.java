package org.firstinspires.ftc.teamcode.subsystems.superstructure;

import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;
import org.firstinspires.ftc.teamcode.subsystems.elevator.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.pivot.Pivot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Superstructure extends SubsystemBase {
    private final Drive drive;
    private final Pivot pivot;
    private final Elevator elevator;

    private SuperstructureState currentState = SuperstructureState.FEED;
    private SuperstructureState desiredState = SuperstructureState.FEED;

    public enum SuperstructureState {
        FEED,
        SCORE
    }

    public Superstructure(Drive drive, Pivot pivot, Elevator elevator) {
        this.drive = drive;
        this.pivot = pivot;
        this.elevator = elevator;
    }

    @Override
    public void periodic() {
        switch (desiredState) {

        }
    }

    public void setDesiredState(SuperstructureState state) {
        this.desiredState = state;
    }

    public Drive getDrive() {
        return drive;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public Elevator getElevator() {
        return elevator;
    }

}
