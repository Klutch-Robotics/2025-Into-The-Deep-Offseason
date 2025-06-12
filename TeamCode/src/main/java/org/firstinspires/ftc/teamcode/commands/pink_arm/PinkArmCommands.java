package org.firstinspires.ftc.teamcode.commands.pink_arm;

import static java.lang.Math.abs;

import org.firstinspires.ftc.teamcode.commands.Presets;
import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;
import org.firstinspires.ftc.teamcode.subsystems.pink_arm.elevator.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.pink_arm.pivot.Pivot;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

public class PinkArmCommands {
    public static double PINK_ARM_EXTENSION_THRESHOLD = 0.1;

    public enum PinkArmPreset {
        TRAVEL,
        INTAKE_HALF,
        INTAKE_FULL,
        SCORE_LOW_BUCKET,
        SCORE_HIGH_BUCKET,
        SCORE_SPEC
    }

    // Sets a preset on the pink arm with a specific pivot and elevator preset.
    // Waits until the elevator is below a threshold before setting the pivot preset.
    public static Command setPinkArmPreset(Superstructure superstructure, PinkArmPreset preset) {
        return  Commands.either(
                //on true
                setElevatorPreset(superstructure, preset),
                //on false
                Commands.deadline(
                                Commands.waitUntil(() -> superstructure.elevator().getPosition() < PINK_ARM_EXTENSION_THRESHOLD),
                                setElevatorPreset(superstructure, PinkArmPreset.TRAVEL)).andThen(setPivotPreset(superstructure, preset)).andThen(setElevatorPreset(superstructure, preset))
                ,
                //Condition
                ()->(abs(superstructure.pivot().getPosition()- Presets.PivotPresets.getPreset(preset))<50)

        );

    }

    // Sets a preset on the pivot
    public static Command setPivotPreset(Superstructure superstructure, PinkArmPreset preset) {
        return Pivot.setPosition(superstructure.pivot(),() ->
                Presets.PivotPresets.getPreset(preset));
    }

    // Sets a preset on the elevator
    public static Command setElevatorPreset(Superstructure superstructure, PinkArmPreset preset) {
        return Elevator.setPosition(superstructure.elevator(), () ->
                Presets.ElevatorPresets.getPreset(preset));
    }

    public static Command setElevatorVoltage(Superstructure superstructure, DoubleSupplier voltage) {
        return Elevator.setVoltage(superstructure.elevator(), voltage);
    }
}
