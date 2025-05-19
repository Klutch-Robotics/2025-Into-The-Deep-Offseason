package org.firstinspires.ftc.teamcode.subsystems.pivot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.lib.ftclib.hardware.motors.MotorEx;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
    private final Telemetry telemetry;

    private final MotorEx pivotMotor;

    private final SquIDController controller;

    private double kSetpoint;

    private double currentPosition = PivotConstants.initialPosition;
    private double desiredPosition = PivotConstants.initialPosition;

    public Pivot(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        pivotMotor = new MotorEx(hwMap, "pivot");

        pivotMotor.setInverted(false);

        pivotMotor.stopAndResetEncoder();

        controller = new SquIDController(0.1);

        kSetpoint = PivotConstants.setpoint;
    }

    @Override
    public void periodic() {
        try {
            currentPosition = pivotMotor.getCurrentPosition() * PivotConstants.ticksToRotations + PivotConstants.initialPosition;

            telemetry.addLine("Pivot");
            telemetry.addData("Pivot Position", currentPosition);
            telemetry.addData("Pivot Voltage", pivotMotor.getVoltage());
            telemetry.addData("Pivot Current", pivotMotor.getCurrent());

            // If setpoint on dashboard changes, update the setpoint
            if (kSetpoint != PivotConstants.setpoint) {
                kSetpoint = PivotConstants.setpoint;
                desiredPosition = kSetpoint;
            }

            calculateVoltage(desiredPosition);
        } catch (Exception ignored) {

        }
    }

    private void calculateVoltage(double position) {
        telemetry.addData("Pivot Desired Position", position);

        double output = controller.calculate(PivotConstants.kP, position, currentPosition) + PivotConstants.kG * Math.cos(Units.rotationsToRadians(currentPosition));
        pivotMotor.set(output);
    }

    private void setPosition(double position) {
        desiredPosition = position;
    }

    public static Command setPosition(Pivot pivot, DoubleSupplier position) {
        return Commands.run(() -> pivot.setPosition(position.getAsDouble()), pivot);
    }
}
