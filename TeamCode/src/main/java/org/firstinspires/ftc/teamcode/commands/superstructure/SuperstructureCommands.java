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

    public static double PIECE_Y_THRESHOLD = 0.0; // Threshold for y position to consider piece found
    public static double PIECE_X_THRESHOLD = 0.0; // Threshold for x position to consider piece found

    public enum SuperstructureState {
        SEEK_SPEC,
        INTAKE_SPEC,
        PREPARE_SCORE_SPEC,
        SCORE_SPEC,
        SEEK_SAMP,
        INTAKE_SAMP,
        PREPARE_SCORE_SAMP,
        SCORE_SAMP
    }

    public static Command seekSpec(Superstructure superstructure, Constants.AllianceColor allianceColor) {
        return Commands.sequence(
                Vision.setSampPipeline(superstructure.vision(), allianceColor),
                SuperstructureCommands.seekPiece(superstructure)
        );
    }

    public static Command seekPiece(Superstructure superstructure) {
        return Commands.sequence(
                Commands.deadline(
                        Commands.waitUntil(() -> superstructure.vision().seesPiece()),
                        PinkArmCommands.setPinkArmPreset(
                                superstructure,
                                PinkArmCommands.PinkArmPreset.INTAKE),
                        EndEffectorCommands.setEndEffectorPreset(
                                superstructure,
                                EndEffectorCommands.EndEffectorPreset.PREPARE_BACK_INTAKE)
                ),
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
                                .until(() -> Math.abs(PIECE_X_SETPOINT - superstructure.vision().getTy()) < PIECE_X_THRESHOLD)
                )
        );
    }
}
