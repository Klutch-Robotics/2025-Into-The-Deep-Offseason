package org.firstinspires.ftc.teamcode.subsystems.end_effector.shoulder_pivot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.end_effector.claw.ClawConstants;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShoulderPivot extends SubsystemBase {
    private final Servo leftShoulderPivot;
    private final Servo rightShoulderPivot;

    private double kSetpoint = ShoulderPivotConstants.setpoint;

    public ShoulderPivot(HardwareMap hwMap) {
        leftShoulderPivot = hwMap.get(Servo.class, "leftShoulderPivot");
        rightShoulderPivot = hwMap.get(Servo.class, "rightShoulderPivot");

        leftShoulderPivot.setDirection(Servo.Direction.FORWARD);
        rightShoulderPivot.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        // If setpoint on dashboard changes, update the setpoint
        if (kSetpoint != ShoulderPivotConstants.setpoint) {
            kSetpoint = ShoulderPivotConstants.setpoint;
            setPosition(kSetpoint);
        }
    }

    private void setPosition(double position) {
        leftShoulderPivot.setPosition(position);
        rightShoulderPivot.setPosition(position);
    }

    public static Command setPosition(ShoulderPivot shoulderPivot, DoubleSupplier position) {
        return Commands.runOnce(() -> shoulderPivot.setPosition(position.getAsDouble()), shoulderPivot);
    }
}
