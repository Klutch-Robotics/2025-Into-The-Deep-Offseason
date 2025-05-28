package org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Wrist extends SubsystemBase {
    private final Servo wrist;

    public Wrist(HardwareMap hwMap) {
        wrist = hwMap.get(Servo.class, "wrist");

        wrist.setDirection(Servo.Direction.FORWARD);
    }

    private void setPosition(double position) {
        wrist.setPosition(position);
    }

    public static Command setPosition(Wrist wrist, DoubleSupplier position) {
        return Commands.runOnce(() -> wrist.setPosition(position.getAsDouble()), wrist);
    }
}
