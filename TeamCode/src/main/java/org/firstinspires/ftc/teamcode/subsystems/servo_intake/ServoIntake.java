package org.firstinspires.ftc.teamcode.subsystems.servo_intake;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.lib.ftclib.hardware.motors.CRServo;
import org.firstinspires.ftc.teamcode.lib.ftclib.hardware.motors.MotorEx;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ServoIntake extends SubsystemBase {
    private final Telemetry telemetry;

    private final CRServo leftServo;
    private final CRServo rightServo;

    public ServoIntake(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        leftServo = new CRServo(hwMap, "leftIntake");
        rightServo = new CRServo(hwMap, "rightIntake");
    }

    @Override
    public void periodic() {
        try {
            setPower(ServoIntakeConstants.setpoint);
        } catch (Exception ignored) {

        }
    }

    private void setPower(double power) {
        leftServo.set(power);
        rightServo.set(power);
    }
}
