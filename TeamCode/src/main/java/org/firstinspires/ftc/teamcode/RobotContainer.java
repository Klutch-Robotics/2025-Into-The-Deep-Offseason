package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.DriveCommands;
import org.firstinspires.ftc.teamcode.commands.auto.BucketAutos;
import org.firstinspires.ftc.teamcode.commands.superstructure.SuperstructureCommands;
import org.firstinspires.ftc.teamcode.lib.wpilib.CommandGamepad;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.claw.Claw;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.shoulder_pivot.ShoulderPivot;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist.Wrist;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist_pivot.WristPivot;
import org.firstinspires.ftc.teamcode.subsystems.pink_arm.elevator.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.pink_arm.pivot.Pivot;
import org.firstinspires.ftc.teamcode.subsystems.vision.Vision;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {
    // Drive
    private final Drive drive;

    // Pink Arm
    private final Pivot pivot;
    private final Elevator elevator;

    // End Effector
    private final ShoulderPivot shoulderPivot;
    private final WristPivot wristPivot;
    private final Wrist wrist;
    private final Claw claw;

    // Vision
    private final Vision vision;

    // Superstructure
    private final Superstructure superstructure;

    private final CommandGamepad driverController;

    private final Constants.AllianceColor allianceColor;

    private boolean specMode;

    public RobotContainer(HardwareMap hwMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2, int autoNum, Constants.AllianceColor allianceColor) {
        drive = new Drive(hwMap, telemetry);

        pivot = new Pivot(hwMap, telemetry);
        elevator = new Elevator(hwMap, telemetry);

        shoulderPivot = new ShoulderPivot(hwMap);
        wristPivot = new WristPivot(hwMap);
        wrist = new Wrist(hwMap);
        claw = new Claw(hwMap);

        vision = new Vision(hwMap, telemetry);

        superstructure = new Superstructure(
                drive,
                pivot,
                elevator,
                shoulderPivot,
                wristPivot,
                wrist,
                claw,
                vision);

        driverController = new CommandGamepad(gamepad1);

        this.allianceColor = allianceColor;

        if (autoNum == 0) {
            setDefaultCommands();
            configureButtonBindings();
        } else {
            getAutoCommand(autoNum);
        }

    }

    public void setDefaultCommands(){
        drive.setDefaultCommand(
                DriveCommands.joystickDrive(
                        drive,
                        () -> -driverController.getLeftY(),
                        () -> -driverController.getLeftX(),
                        () -> -driverController.getRightX()));
    }

    public void configureButtonBindings() {
        // Toggle spec mode
//        driverController.b().onTrue(Commands.runOnce(() -> specMode = !specMode));
//
//        driverController.a().and(this::isSampMode).onTrue(
//                SuperstructureCommands.setSuperstructureState(
//                    superstructure,
//                    SuperstructureCommands.SuperstructureState.SEEK_SAMP,
//                    allianceColor));
//
//        driverController.a().and(this::isSpecMode).onTrue(
//                SuperstructureCommands.setSuperstructureState(
//                        superstructure,
//                        SuperstructureCommands.SuperstructureState.SEEK_SPEC,
//                        allianceColor));
        driverController.a().onTrue(SuperstructureCommands.testPiecePickUpWithDrive(superstructure));
    }

    private boolean isSpecMode() {
        return specMode;
    }

    private boolean isSampMode() {
        return !specMode;
    }

    public Command getAutoCommand(int chooser) {
        switch (chooser) {
            case 1:
            return BucketAutos.blueAuto(superstructure, allianceColor);
        }
        return Commands.none();
    }
}