package org.firstinspires.ftc.teamcode.commands.superstructure;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.DriveCommands;
import org.firstinspires.ftc.teamcode.commands.end_effector.EndEffectorCommands;
import org.firstinspires.ftc.teamcode.commands.pink_arm.PinkArmCommands;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;
import org.firstinspires.ftc.teamcode.subsystems.vision.Vision;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

@Config
public class SuperstructureCommands {
    public static double PIECE_X_KP = 0.0;
    public static double PIECE_Y_KP = 0.0;

    public static double PIECE_X_SETPOINT = 0.0;
    public static double PIECE_Y_SETPOINT = 0.0;

    public static double PIECE_Y_THRESHOLD = 0.2; // Threshold for y position to consider piece found
    public static double PIECE_X_THRESHOLD = 0.2; // Threshold for x position to consider piece found

    public static double BACK_INTAKE_WAIT_SECONDS = 0.2; // Time to wait before closing the claw

    public static double SAMP_SCORE_TIMEOUT = 0.5;

// Was gonna use this to manage superstructure states, but is now just a list of actions that the robot needs to do
//    public enum SuperstructureState {
//        SEEK_SPEC,
//        INTAKE_SPEC,
//        PREPARE_SCORE_SPEC,
//        SCORE_SPEC,
//        SEEK_SAMP,
//        INTAKE_SAMP,
//        PREPARE_SCORE_SAMP,
//        SCORE_SAMP
//    }

    private static Command seekSpec(Superstructure superstructure, Constants.AllianceColor allianceColor) {
        return Commands.sequence(
                Vision.setSampPipeline(superstructure.vision(), allianceColor),
                SuperstructureCommands.seekPiece(superstructure)
        );
    }

    private static Command seekSamp(Superstructure superstructure, Constants.AllianceColor allianceColor) {
        return Commands.sequence(
                Vision.setSpecPipeline(superstructure.vision(), allianceColor),
                SuperstructureCommands.seekPiece(superstructure)
        );
    }

    private static Command seekYellow(Superstructure superstructure) {
        return Commands.sequence(
                Vision.setYellowPipeline(superstructure.vision()),
                SuperstructureCommands.seekPiece(superstructure)
        );
    }

    // TODO:MAKE SURE TO ADD MAX EXTENSION PROTECTION
    private static Command seekPiece(Superstructure superstructure) {
        return Commands.sequence(
                // Move the pink arm to the intake preset until the vision system sees a piece
                Commands.deadline(
                        Commands.waitUntil(() -> superstructure.vision().seesPiece()),
                        PinkArmCommands.setPinkArmPreset(
                                superstructure,
                                PinkArmCommands.PinkArmPreset.INTAKE),
                        EndEffectorCommands.setEndEffectorPreset(
                                superstructure,
                                EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE)
                ),
                // Move elevator and strafe drivetrain to align with the piece, and set the wrist to the piece angle
                Commands.parallel(
                        PinkArmCommands.setPinkArmPreset(
                                superstructure,
                                PinkArmCommands.PinkArmPreset.INTAKE,
                                () -> SquIDController.calculateStatic(
                                        PIECE_Y_KP,
                                        PIECE_Y_SETPOINT,
                                        superstructure.vision().getTy()))
                                .until(() -> Math.abs(PIECE_Y_SETPOINT - superstructure.vision().getTy()) < PIECE_Y_THRESHOLD),
                        DriveCommands.joystickDrive(
                                superstructure.drive(),
                                () -> SquIDController.calculateStatic(
                                        PIECE_X_KP,
                                        PIECE_X_SETPOINT,
                                        superstructure.vision().getTx()),
                                () -> 0.0,
                                () -> 0.0)
                                .until(() -> Math.abs(PIECE_X_SETPOINT - superstructure.vision().getTy()) < PIECE_X_THRESHOLD),
                        EndEffectorCommands.setWristPosition(superstructure, () -> superstructure.vision().getAngle())
                ),
                // Set the end effector to the back intake preset
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.BACK_INTAKE
                ),
                // Wait for a short time before closing the claw
                Commands.waitSeconds(BACK_INTAKE_WAIT_SECONDS),
                // Close the claw to intake the piece
                EndEffectorCommands.setClawPreset(superstructure, EndEffectorCommands.ClawPreset.CLOSE)
        );
    }

    private static Command prepareScoreSamp(Superstructure superstructure) {
        return Commands.parallel(
            PinkArmCommands.setPinkArmPreset(superstructure, PinkArmCommands.PinkArmPreset.SCORE_HIGH_BUCKET),
            EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_SCORE_BUCKET)
        );
    }

    private static Command scoreSamp(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setClawPreset(superstructure, EndEffectorCommands.ClawPreset.OPEN),
                Commands.waitSeconds(SAMP_SCORE_TIMEOUT),
                Commands.parallel(
                        PinkArmCommands.setPinkArmPreset(superstructure, PinkArmCommands.PinkArmPreset.TRAVEL),
                        EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.TRAVEL)
                )
        );
    }

    public static Command angleWristToPiece(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setWristPosition(
                        superstructure,
                        () -> {
                            if (superstructure.vision().seesPiece()) {
                                if (superstructure.vision().getAngle() < 90) {
                                    return (0.25 / 90.0) * superstructure.vision().getAngle() + 0.5;
                                } else {
                                    return (0.25 / 90.0) * superstructure.vision().getAngle();
                                }
                            } else {
                                return superstructure.wrist().getPosition();
                            }
                }).repeatedly()
        );
    }


    //Todo: make a test command that angles the wrist, and moves down to intake a piece once the limelight thinks it's ready
    public static Command testPiecePickUp(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE),
                Commands.waitUntil(() -> superstructure.vision().seesPiece()),
                Commands.deadline(
                        Commands.waitUntil(() -> isPieceAligned(superstructure)),
                        angleWristToPiece(superstructure)
                ),
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.BACK_INTAKE),
                Commands.waitSeconds(BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setClawPreset(superstructure, EndEffectorCommands.ClawPreset.CLOSE),
                Commands.waitSeconds(BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE, EndEffectorCommands.ClawPreset.CLOSE)

        );
    }

    public static Command testPiecePickUpWithDrive(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE),
                Commands.waitUntil(() -> superstructure.vision().seesPiece()),
                Commands.deadline(
                        DriveCommands.joystickDrive(
                                superstructure.drive(),
                                () -> {
                                    if (superstructure.vision().seesPiece()) {
                                        return SquIDController.calculateStatic(
                                                PIECE_Y_KP,
                                                PIECE_Y_SETPOINT,
                                                superstructure.vision().getTy());
                                    } else {
                                        return 0.0;
                                    }},
                                () -> {
                                    if (superstructure.vision().seesPiece()) {
                                        return SquIDController.calculateStatic(
                                                PIECE_X_KP,
                                                PIECE_X_SETPOINT,
                                                superstructure.vision().getTx());
                                    } else {
                                        return 0.0;
                                    }},
                                () -> 0.0).until(() -> isPieceAligned(superstructure)),
                        angleWristToPiece(superstructure)
                ),
                Commands.waitUntil(() -> isPieceAligned(superstructure)),
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.BACK_INTAKE),
                Commands.waitSeconds(BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setClawPreset(superstructure, EndEffectorCommands.ClawPreset.CLOSE),
                Commands.waitSeconds(BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE, EndEffectorCommands.ClawPreset.CLOSE)
        );
    }

    public static boolean isPieceAligned(Superstructure superstructure) {
        return (Math.abs(PIECE_Y_SETPOINT - superstructure.vision().getTy()) < PIECE_Y_THRESHOLD &&
               Math.abs(PIECE_X_SETPOINT - superstructure.vision().getTx()) < PIECE_X_THRESHOLD) && superstructure.vision().seesPiece();
    }
}
