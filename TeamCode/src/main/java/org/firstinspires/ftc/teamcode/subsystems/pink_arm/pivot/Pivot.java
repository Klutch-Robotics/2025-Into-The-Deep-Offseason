package org.firstinspires.ftc.teamcode.subsystems.pink_arm.pivot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.lib.ftclib.hardware.motors.MotorEx;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
    private final Telemetry telemetry;

    private final MotorEx topMotor;
    private final MotorEx bottomMotor;

    private final SquIDController controller;

    private double kSetpoint;

    private double currentPosition = PivotConstants.initialPosition;
    private double desiredPosition = 0;

    public enum ControlMode {
        POSITION,
        VOLTAGE
    }

    private ControlMode controlMode = ControlMode.POSITION;

    public Pivot(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        topMotor = new MotorEx(hwMap, "liftTop");
        bottomMotor = new MotorEx(hwMap, "liftBottom");

        topMotor.setInverted(true);
        bottomMotor.setInverted(false);

        topMotor.stopAndResetEncoder();
        bottomMotor.stopAndResetEncoder();

        controller = new SquIDController(0.1);

        kSetpoint = PivotConstants.setpoint;
    }

    @Override
    public void periodic() {
        try {
            telemetry.addLine("Pivot");
            telemetry.addData("Pivot Position", currentPosition);
            telemetry.addData("Pivot Voltage", topMotor.getVoltage());
            telemetry.addData("Pivot Current", topMotor.getCurrent());

            currentPosition = topMotor.getCurrentPosition() * PivotConstants.ticksToRotations + PivotConstants.initialPosition;

            if (controlMode == ControlMode.POSITION) {
                // If setpoint on dashboard changes, update the setpoint
                if (kSetpoint != PivotConstants.setpoint) {
                    kSetpoint = PivotConstants.setpoint;
                    desiredPosition = kSetpoint;
                }

                calculateVoltage(desiredPosition);
            }
        } catch (Exception ignored) {

        }
    }

    private void calculateVoltage(double position) {
        telemetry.addData("Pivot Desired Position", position);

        double output = controller.calculate(PivotConstants.kP, position, currentPosition)
                + Math.cos(currentPosition) * PivotConstants.kG;

        topMotor.set(output);
        bottomMotor.set(output);
    }

    private void setVoltage(double voltage) {
        if (controlMode != ControlMode.VOLTAGE) {
            controlMode = ControlMode.VOLTAGE;
        }

        topMotor.set(voltage);
        bottomMotor.set(voltage);
    }

    private void setPosition(double position) {
        if (controlMode != ControlMode.POSITION) {
            controlMode = ControlMode.POSITION;
        }
        desiredPosition = position;
    }

    private boolean isFinished() {
        return Math.abs(currentPosition - desiredPosition) < PivotConstants.tolerance;
    }

    public double getPosition() {
        return currentPosition;
    }

    // Runs the pivot to a position and stops when it is within tolerance
    public static Command setPosition(Pivot pivot, DoubleSupplier position) {
        return Commands.run(() -> pivot.setPosition(position.getAsDouble()), pivot).until(pivot::isFinished);
    }

    public static Command setVoltage(Pivot pivot, DoubleSupplier voltage) {
        return Commands.run(() -> pivot.setVoltage(voltage.getAsDouble()), pivot);
    }
}
