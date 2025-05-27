package org.firstinspires.ftc.teamcode.commands.superstructure;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.commands.Presets;
import org.firstinspires.ftc.teamcode.subsystems.elevator.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.pivot.Pivot;
import org.firstinspires.ftc.teamcode.subsystems.superstructure.Superstructure;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

@Config
public class SuperstructureCommands {
    public static double PINK_ARM_EXTENSION_THRESHOLD = 0.1;

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

    public enum PinkArmPreset {
        TRAVEL,
        INTAKE,
        FEED,
        SCORE_LOW_BUCKET,
        SCORE_HIGH_BUCKET,
        SCORE_SPEC
    }

    public static Command setPinkArmPreset(Superstructure superstructure, PinkArmPreset preset) {
        return Commands.deadline(
                Commands.waitUntil(() -> superstructure.getElevator().getPosition() < PINK_ARM_EXTENSION_THRESHOLD),
                setElevatorPreset(superstructure, PinkArmPreset.TRAVEL))
                .andThen(setPivotPreset(superstructure, preset))
                .andThen(setElevatorPreset(superstructure, preset));
    }

    public static Command setPivotPreset(Superstructure superstructure, PinkArmPreset preset) {
        return Pivot.setPosition(superstructure.getPivot(),() ->
                                 Presets.PivotPresets.getPreset(preset));
    }

    public static Command setElevatorPreset(Superstructure superstructure, PinkArmPreset preset) {
        return Elevator.setPosition(superstructure.getElevator(), () ->
                                 Presets.ElevatorPresets.getPreset(preset));
    }
}
