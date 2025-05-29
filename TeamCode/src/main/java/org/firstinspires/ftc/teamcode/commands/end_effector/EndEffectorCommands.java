package org.firstinspires.ftc.teamcode.commands.end_effector;

import org.firstinspires.ftc.teamcode.commands.Presets;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.claw.Claw;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.shoulder_pivot.ShoulderPivot;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist.Wrist;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist_pivot.WristPivot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class EndEffectorCommands {
    public enum EndEffectorPreset {
        TRAVEL,
        PREPARE_FRONT_INTAKE,
        FRONT_INTAKE,
        PREPARE_BACK_INTAKE,
        BACK_INTAKE,
        PREPARE_SCORE_BUCKET,
        SCORE_BUCKET,
        PREPARE_SCORE_SPEC,
        SCORE_SPEC
    }

    public enum ClawPreset {
        OPEN,
        CLOSE
    }

    //Sets a preset on the end effector with a specific claw preset.
    public static Command setEndEffectorPreset(
            Superstructure superstructure,
            EndEffectorPreset preset) {

        return EndEffectorCommands.setEndEffectorPreset(superstructure, preset, ClawPreset.OPEN);
    }

    //Sets a preset on the end effector with a specific claw preset.
    public static Command setEndEffectorPreset(
            Superstructure superstructure,
            EndEffectorPreset preset,
            ClawPreset clawPreset) {

        return Commands.parallel(
                ShoulderPivot.setPosition(superstructure.shoulderPivot(), () -> Presets.ShoulderPivotPresets.getPreset(preset)),
                WristPivot.setPosition(superstructure.wristPivot(), () -> Presets.WristPivotPresets.getPreset(preset)),
                Wrist.setPosition(superstructure.wrist(), () -> Presets.WristPresets.getPreset(preset)),
                Claw.setPosition(superstructure.claw(), () -> Presets.ClawPresets.getPreset(clawPreset))
        );
    }

    //Sets a preset on the end effector with a specific wrist position and claw preset.
    public static Command setEndEffectorPreset(
            Superstructure superstructure,
            EndEffectorPreset preset,
            DoubleSupplier wristPosition,
            ClawPreset clawPreset) {

        return Commands.parallel(
                ShoulderPivot.setPosition(superstructure.shoulderPivot(), () -> Presets.ShoulderPivotPresets.getPreset(preset)),
                WristPivot.setPosition(superstructure.wristPivot(), () -> Presets.WristPivotPresets.getPreset(preset)),
                Wrist.setPosition(superstructure.wrist(), wristPosition),
                Claw.setPosition(superstructure.claw(), () -> Presets.ClawPresets.getPreset(clawPreset))
        );
    }

    public static Command setWristPosition(
            Superstructure superstructure,
            DoubleSupplier wristPosition) {

        return Wrist.setPosition(superstructure.wrist(), wristPosition);
    }

    public static Command setClawPreset(
            Superstructure superstructure,
            ClawPreset clawPreset) {

        return Claw.setPosition(superstructure.claw(), () -> Presets.ClawPresets.getPreset(clawPreset));
    }

}
