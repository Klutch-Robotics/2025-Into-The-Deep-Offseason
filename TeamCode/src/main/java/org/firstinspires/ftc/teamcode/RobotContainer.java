package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.AutoCommands;
import org.firstinspires.ftc.teamcode.lib.wpilib.CommandGamepad;
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;
import org.firstinspires.ftc.teamcode.subsystems.pivot.Pivot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {
    private final Drive drive;
    private final Pivot pivot;
//    private final ServoIntake servoIntake;

    private final CommandGamepad driverController;

    public RobotContainer(HardwareMap hwMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2, int autoNum) {
        drive = new Drive(hwMap, telemetry);
        pivot = new Pivot(hwMap, telemetry);
//        servoIntake = new ServoIntake(hwMap, telemetry);

        driverController = new CommandGamepad(gamepad1);

        if (autoNum == 0) {
            setDefaultCommands();
            configureButtonBindings();
        } else {
            getAutoCommand(autoNum);
        }
    }

    public void setDefaultCommands(){
//        drive.setDefaultCommand(
//                DriveCommands.joystickDrive(
//                        drive,
//                        () -> -driverController.getLeftY(),
//                        () -> -driverController.getLeftX(),
//                        () -> -driverController.getRightX()));
    }

    public void configureButtonBindings() {
    }

    public Command getAutoCommand(int chooser) {
        switch (chooser) {
            case 1:
            return AutoCommands.blueAuto(drive, pivot);
        }
        return Commands.none();
    }
}