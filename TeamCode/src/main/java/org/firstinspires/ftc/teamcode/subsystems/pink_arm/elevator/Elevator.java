package org.firstinspires.ftc.teamcode.subsystems.pink_arm.elevator;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.lib.ftclib.hardware.motors.MotorEx;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
    private final Telemetry telemetry;

    private final MotorEx topMotor;
    private final MotorEx bottomMotor;

    private final SquIDController controller;

    private double kSetpoint;

    private double currentPosition = 0;
    private double desiredPosition = 0;

    public enum ControlMode {
        POSITION,
        VOLTAGE
    }

    private ControlMode controlMode = ControlMode.POSITION;

    public Elevator(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        topMotor = new MotorEx(hwMap, "liftBottom");
        bottomMotor = new MotorEx(hwMap, "liftTop");

        topMotor.setInverted(false);
        bottomMotor.setInverted(true);

        topMotor.stopAndResetEncoder();
        bottomMotor.stopAndResetEncoder();

        controller = new SquIDController(0.1);

        kSetpoint = ElevatorConstants.setpoint;
    }

    @Override
    public void periodic() {
        try {
            telemetry.addLine("Elevator");
            telemetry.addData("Elevator Position", currentPosition);
            telemetry.addData("Elevator Voltage", topMotor.getVoltage());
            telemetry.addData("Elevator Current", topMotor.getCurrent());

            currentPosition = topMotor.getCurrentPosition() * ElevatorConstants.ticksToInches;

            if (controlMode == ControlMode.POSITION) {
                // If setpoint on dashboard changes, update the setpoint
                if (kSetpoint != ElevatorConstants.setpoint) {
                    kSetpoint = ElevatorConstants.setpoint;
                    desiredPosition = kSetpoint;
                }

                calculateVoltage(desiredPosition);
            }
        } catch (Exception ignored) {

        }
    }

    private void calculateVoltage(double position) {
        telemetry.addData("Elevator Desired Position", position);

        double output = controller.calculate(ElevatorConstants.kP, position, currentPosition) + ElevatorConstants.kG;

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
        return Math.abs(currentPosition - desiredPosition) < ElevatorConstants.tolerance;
    }

    public double getPosition() {
        return currentPosition;
    }

    // Runs the elevator to a position and stops when it is within tolerance
    public static Command setPosition(Elevator elevator, DoubleSupplier position) {
        return Commands.run(() -> elevator.setPosition(position.getAsDouble()), elevator).until(elevator::isFinished);
    }

    public static Command setVoltage(Elevator elevator, DoubleSupplier voltage) {
        return Commands.run(() -> elevator.setVoltage(voltage.getAsDouble()), elevator);
    }
}
