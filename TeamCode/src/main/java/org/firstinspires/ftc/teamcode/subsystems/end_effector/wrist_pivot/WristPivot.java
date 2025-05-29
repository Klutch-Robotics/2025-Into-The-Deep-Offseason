package org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist_pivot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.end_effector.claw.ClawConstants;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist.WristConstants;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WristPivot extends SubsystemBase {
    private final Servo wristPivot;

    private double kSetpoint = WristPivotConstants.setpoint;

    public WristPivot(HardwareMap hwMap) {
        wristPivot = hwMap.get(Servo.class, "wristPivot");

        wristPivot.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        // If setpoint on dashboard changes, update the setpoint
        if (kSetpoint != WristPivotConstants.setpoint) {
            kSetpoint = WristPivotConstants.setpoint;
            setPosition(kSetpoint);
        }
    }

    private void setPosition(double position) {
        wristPivot.setPosition(position);
    }

    public static Command setPosition(WristPivot wristPivot, DoubleSupplier position) {
        return Commands.runOnce(() -> wristPivot.setPosition(position.getAsDouble()), wristPivot);
    }
}