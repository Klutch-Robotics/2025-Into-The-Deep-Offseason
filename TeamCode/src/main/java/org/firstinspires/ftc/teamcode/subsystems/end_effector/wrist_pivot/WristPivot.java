package org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist_pivot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WristPivot extends SubsystemBase {
    private final Servo wristPivot;

    public WristPivot(HardwareMap hwMap) {
        wristPivot = hwMap.get(Servo.class, "wristPivot");

        wristPivot.setDirection(Servo.Direction.FORWARD);
    }

    private void setPosition(double position) {
        wristPivot.setPosition(position);
    }

    public static Command setPosition(WristPivot wristPivot, DoubleSupplier position) {
        return Commands.runOnce(() -> wristPivot.setPosition(position.getAsDouble()), wristPivot);
    }
}