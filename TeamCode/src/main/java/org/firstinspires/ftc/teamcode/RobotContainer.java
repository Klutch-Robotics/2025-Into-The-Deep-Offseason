package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.DriveCommands;
import org.firstinspires.ftc.teamcode.commands.auto.BucketAutos;
import org.firstinspires.ftc.teamcode.commands.superstructure.SuperstructureCommands;
import org.firstinspires.ftc.teamcode.lib.wpilib.CommandGamepad;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;
import org.firstinspires.ftc.teamcode.subsystems.color_sensor.ColorSensor;
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

    // Sensors
    private final Vision vision;
    private final ColorSensor colorSensor;

    // Superstructure
    private final Superstructure superstructure;

    // Controller
    private final CommandGamepad driverController;

    // Alliance Color
    private final Constants.AllianceColor allianceColor;

    // Drive Modes
    private boolean specMode = false;

    private boolean readyToScoreSpec = false;

    public RobotContainer(HardwareMap hwMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2, int autoNum, Constants.AllianceColor allianceColor) {
        drive = new Drive(hwMap, telemetry);

        pivot = new Pivot(hwMap, telemetry);
        elevator = new Elevator(hwMap, telemetry);

        shoulderPivot = new ShoulderPivot(hwMap);
        wristPivot = new WristPivot(hwMap);
        wrist = new Wrist(hwMap);
        claw = new Claw(hwMap);

        vision = new Vision(hwMap, telemetry);
        colorSensor = new ColorSensor(hwMap, telemetry);

        superstructure = new Superstructure(
                drive,
                pivot,
                elevator,
                shoulderPivot,
                wristPivot,
                wrist,
                claw,
                vision,
                colorSensor);

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
                DriveCommands.joystickDriveGas(
                        drive,
                        driverController::getLeftTrigger,
                        () -> -driverController.getLeftY(),
                        () -> -driverController.getLeftX(),
                        () -> -driverController.getRightX()));
    }

    public void configureButtonBindings() {
        // Toggle spec mode
        driverController.b().onTrue(Commands.runOnce(() -> specMode = !specMode));

        /* --- Spec Mode Commands --- */
        // Extend Feeder Half
        driverController.rightTrigger().and(this::isSpecMode).onTrue(SuperstructureCommands.seekPieceHalf(
                superstructure));

        // Extend Feeder Full
        driverController.y().and(this::isSpecMode).onTrue(SuperstructureCommands.seekPieceFull(
                superstructure));

        // Pickup override
        driverController.rightBumper().and(this::isSpecMode).onTrue(SuperstructureCommands.pickUpPieceOverride(superstructure));

        // Prepare to score spec
        driverController.leftBumper().and(this::isSpecMode)
                .onTrue(SuperstructureCommands.prepareScoreSpec(superstructure));
        // Release claw spec
        driverController.x().and(this::isSpecMode)
                .onTrue(SuperstructureCommands.releaseClawSpec(superstructure));



        /* --- Sample Mode Commands --- */



        // Extend Feeder Half
        driverController.rightTrigger().and(this::isSampMode).onTrue(SuperstructureCommands.seekPieceHalf(
                superstructure));

        // Extend Feeder Full
        driverController.y().and(this::isSampMode).onTrue(SuperstructureCommands.seekPieceFull(
                superstructure));

        // Pickup override
        driverController.rightBumper().and(this::isSampMode).onTrue(SuperstructureCommands.pickUpPieceOverride(superstructure));

        // Extend up For High Bucket
        driverController.leftBumper().and(this::isSampMode)
                .onTrue(SuperstructureCommands.prepareScoreSamp(superstructure));
        // Release claw spec
        driverController.x().and(this::isSampMode)
                .onTrue(SuperstructureCommands.releaseClawSamp(superstructure));



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