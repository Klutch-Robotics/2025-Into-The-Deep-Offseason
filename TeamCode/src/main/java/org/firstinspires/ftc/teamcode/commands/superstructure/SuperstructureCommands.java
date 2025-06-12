package org.firstinspires.ftc.teamcode.commands.superstructure;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.commands.DriveCommands;
import org.firstinspires.ftc.teamcode.commands.end_effector.EndEffectorCommands;
import org.firstinspires.ftc.teamcode.commands.pink_arm.PinkArmCommands;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

@Config
public class SuperstructureCommands {
    // Tunable variables for automation
<<<<<<< Updated upstream
    public static double PIECE_X_KP = 0.0;
    public static double PIECE_Y_KP = 0.0;
=======
    public static double PIECE_X_KP = 0.2;
    public static double PIECE_Y_KP = -0.2;
>>>>>>> Stashed changes

    public static double PIECE_X_SETPOINT = 0.0;
    public static double PIECE_Y_SETPOINT = 0.0;

    public static double PIECE_Y_THRESHOLD = 0.2; // Threshold for y position to consider piece found
    public static double PIECE_X_THRESHOLD = 0.2; // Threshold for x position to consider piece found

    public static double BACK_INTAKE_WAIT_SECONDS = 0.2; // Time to wait before closing the claw

    public static double SCORE_SAMP_TIMEOUT = 0.5;
    public static double SCORE_SPEC_TIMEOUT = 0.5;

    /* --- Commands for Piece Intake --- */
    public static Command seekPieceHalf(
            Superstructure superstructure,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier omegaSupplier) {
        return Commands.sequence(
                // Move the pink arm to the intake preset until the vision system sees a piece
                prepareForIntakeHalf(superstructure),
                // Move elevator and strafe drivetrain to align with the piece, and set the wrist to the piece angle
                Commands.deadline(
                        Commands.waitUntil(() -> isPieceAligned(superstructure)),
                        alignPinkArmToPiece(superstructure),
                        alignDriveToPiece(superstructure, xSupplier, ySupplier, omegaSupplier),
                        angleWristToPiece(superstructure)
                ),
                // Set the end effector to the back intake preset
                pickUpPiece(superstructure)
        );
    }

    public static Command seekPieceFull(
            Superstructure superstructure) {
        return Commands.sequence(
                // Move the pink arm to the intake preset until the vision system sees a piece
                prepareForIntakeFull(superstructure),
                // Move elevator and strafe drivetrain to align with the piece, and set the wrist to the piece angle
                Commands.deadline(
                        Commands.waitUntil(() -> isPieceAligned(superstructure)),
                        alignPinkArmToPiece(superstructure),
                        angleWristToPiece(superstructure)
                ),
                // Set the end effector to the back intake preset
<<<<<<< Updated upstream
                pickUpPiece(superstructure)
        );
    }

=======
                pickUpPiece(superstructure),
                PinkArmCommands.setElevatorVoltage(superstructure, () -> 0.0).withTimeout(0.1)
        );
    }


    public static Command seekPieceHalfAuto(
            Superstructure superstructure) {
        return Commands.sequence(
                // Move the pink arm to the intake preset until the vision system sees a piece

                // Move elevator and strafe drivetrain to align with the piece, and set the wrist to the piece angle
                Commands.deadline(
                        Commands.waitUntil(() -> isPieceAligned(superstructure)),
                        alignPinkArmToPiece(superstructure),
                        angleWristToPiece(superstructure),
                        DriveCommands.driveRobotCentric(superstructure.drive(), ()->0,
                                () -> {
                                    if (superstructure.vision().seesPiece()) {
                                        return SquIDController.calculateStatic(
                                                PIECE_Y_KP,
                                                PIECE_Y_SETPOINT,
                                                superstructure.vision().getTy());
                                    } else {
                                        return 0.0;
                                    }},

                                ()->0)
                ),
                // Set the end effector to the back intake preset
                pickUpPiece(superstructure),
                PinkArmCommands.setElevatorVoltage(superstructure, () -> 0.0).withTimeout(0.1)
        );
    }


>>>>>>> Stashed changes
    public static Command prepareForIntakeHalf(Superstructure superstructure) {
        return Commands.parallel(
                PinkArmCommands.setPinkArmPreset(
                        superstructure,
                        PinkArmCommands.PinkArmPreset.INTAKE_HALF),
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE)
        );
    }

    public static Command prepareForIntakeFull(Superstructure superstructure) {
        return Commands.parallel(
                PinkArmCommands.setPinkArmPreset(
                        superstructure,
                        PinkArmCommands.PinkArmPreset.INTAKE_FULL),
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE)
        );
    }

    public static Command alignDriveToPiece(
            Superstructure superstructure,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier omegaSupplier) {
        return DriveCommands.joystickDrive(
                superstructure.drive(),
                () -> SquIDController.calculateStatic(
                        PIECE_X_KP,
                        PIECE_X_SETPOINT,
                        superstructure.vision().getTx()) + xSupplier.getAsDouble(),
                ySupplier,
                omegaSupplier);
    }

    public static Command alignPinkArmToPiece(Superstructure superstructure) {
        return PinkArmCommands.setElevatorVoltage(
                superstructure,
                () -> SquIDController.calculateStatic(
                        PIECE_Y_KP,
                        PIECE_Y_SETPOINT,
                        superstructure.vision().getTy()));
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

    public static Command manualIntake(
            Superstructure superstructure,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier omegaSupplier) {
        return Commands.sequence(
                prepareForIntakeHalf(superstructure),
                Commands.parallel(
                        alignPinkArmToPiece(superstructure),
                        alignDriveToPiece(superstructure, xSupplier, ySupplier, omegaSupplier),
                        angleWristToPiece(superstructure)
                ));
    }

    // TODO: Optimize wait times
    public static Command pickUpPiece(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.BACK_INTAKE),
                Commands.waitSeconds(() -> BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setClawPreset(
                        superstructure,
                        EndEffectorCommands.ClawPreset.CLOSE),
                Commands.waitSeconds(() -> BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE,
                        EndEffectorCommands.ClawPreset.CLOSE)
        );
    }


    public static Command pickUpPieceOverride(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setClawPreset(
                        superstructure,
                        EndEffectorCommands.ClawPreset.OPEN),
                Commands.waitSeconds(() -> BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.BACK_INTAKE),
                Commands.waitSeconds(() -> BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setClawPreset(
                        superstructure,
                        EndEffectorCommands.ClawPreset.CLOSE),
                Commands.waitSeconds(() -> BACK_INTAKE_WAIT_SECONDS),
                EndEffectorCommands.setEndEffectorPreset(
                        superstructure,
                        EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE,
                        EndEffectorCommands.ClawPreset.CLOSE)
        );
    }

    /* --- Commands for Scoring --- */
    public static Command prepareScoreSamp(Superstructure superstructure) {
        return Commands.parallel(
            PinkArmCommands.setPinkArmPreset(superstructure, PinkArmCommands.PinkArmPreset.SCORE_HIGH_BUCKET),
            EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_SCORE_BUCKET)
        );
    }

    public static Command releaseClawSamp(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setClawPreset(superstructure, EndEffectorCommands.ClawPreset.OPEN),
                Commands.waitSeconds(SCORE_SAMP_TIMEOUT),
                Commands.parallel(
                        PinkArmCommands.setPinkArmPreset(superstructure, PinkArmCommands.PinkArmPreset.TRAVEL),
                        EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.TRAVEL)
                )
        );
    }

    public static Command prepareScoreSpec(Superstructure superstructure) {
        return Commands.parallel(
                PinkArmCommands.setPinkArmPreset(superstructure, PinkArmCommands.PinkArmPreset.SCORE_SPEC),
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_SCORE_SPEC)
        );
    }

    public static Command releaseClawSpec(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setClawPreset(superstructure, EndEffectorCommands.ClawPreset.OPEN),
                Commands.waitSeconds(SCORE_SPEC_TIMEOUT),
                Commands.parallel(
                        PinkArmCommands.setPinkArmPreset(superstructure, PinkArmCommands.PinkArmPreset.TRAVEL),
                        EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.TRAVEL)
                )
        );
    }

    /* --- Commands for Testing --- */
    public static Command testPiecePickUp(Superstructure superstructure) {
        return Commands.sequence(
                EndEffectorCommands.setEndEffectorPreset(superstructure, EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE),
                Commands.waitUntil(() -> superstructure.vision().seesPiece()),
                Commands.deadline(
                        Commands.waitUntil(() -> isPieceAligned(superstructure)),
                        angleWristToPiece(superstructure)
                ),
                pickUpPiece(superstructure)
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
                pickUpPiece(superstructure)
        );
    }

    /* -- Helper Methods --- */
    public static boolean isPieceAligned(Superstructure superstructure) {
        return (Math.abs(PIECE_Y_SETPOINT - superstructure.vision().getTy()) < PIECE_Y_THRESHOLD &&
               Math.abs(PIECE_X_SETPOINT - superstructure.vision().getTx()) < PIECE_X_THRESHOLD) && superstructure.vision().seesPiece();
    }
}
