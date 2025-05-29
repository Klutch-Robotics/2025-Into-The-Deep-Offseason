package org.firstinspires.ftc.teamcode.subsystems.end_effector.claw;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.commands.Presets;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Config
public class Claw extends SubsystemBase {
    private final Servo claw;

    private double kSetpoint = ClawConstants.setpoint;

    public Claw(HardwareMap hwMap) {
        claw = hwMap.get(Servo.class, "clawServo");

        claw.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        // If setpoint on dashboard changes, update the setpoint
        if (kSetpoint != ClawConstants.setpoint) {
            kSetpoint = ClawConstants.setpoint;
            setPosition(kSetpoint);
        }
    }

    private void setPosition(double position) {
        claw.setPosition(position);
    }

    public static Command setPosition(Claw claw, DoubleSupplier position) {
        return Commands.runOnce(() -> claw.setPosition(position.getAsDouble()), claw);
    }
}
