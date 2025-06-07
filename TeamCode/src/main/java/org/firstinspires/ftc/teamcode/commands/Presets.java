package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.commands.end_effector.EndEffectorCommands;
import org.firstinspires.ftc.teamcode.commands.pink_arm.PinkArmCommands;
import org.firstinspires.ftc.teamcode.commands.superstructure.SuperstructureCommands;

@Config
public class Presets {
    @Config
    public class PivotPresets {
        public static double TRAVEL = 0.0;
        public static double INTAKE_HALF = 0.0;
        public static double INTAKE = 0.0;
        public static double PREPARE_FEED = 0.0;
        public static double FEED = 0.0;
        public static double SCORE_LOW_BUCKET = 0.0;
        public static double SCORE_HIGH_BUCKET = 0.0;
        public static double SCORE_SPEC = 0.0;

        public static double getPreset(PinkArmCommands.PinkArmPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case INTAKE_HALF -> INTAKE_HALF;
                case INTAKE_FULL -> INTAKE;
                case SCORE_LOW_BUCKET -> SCORE_LOW_BUCKET;
                case SCORE_HIGH_BUCKET -> SCORE_HIGH_BUCKET;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }

    @Config
    public class ElevatorPresets {
        public static double TRAVEL = 0.0;
        public static double INTAKE_HALF = 0.0;
        public static double INTAKE_FULL = 0.0;
        public static double PREPARE_FEED = 0.0;
        public static double FEED = 0.0;
        public static double SCORE_LOW_BUCKET = 0.0;
        public static double SCORE_HIGH_BUCKET = 0.0;
        public static double SCORE_SPEC = 0.0;

        public static double getPreset(PinkArmCommands.PinkArmPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case INTAKE_HALF -> INTAKE_HALF;
                case INTAKE_FULL -> INTAKE_FULL;
                case SCORE_LOW_BUCKET -> SCORE_LOW_BUCKET;
                case SCORE_HIGH_BUCKET -> SCORE_HIGH_BUCKET;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }

    @Config
    public class ShoulderPivotPresets {
        public static double TRAVEL = 0.0;
        public static double PREPARE_FRONT_INTAKE = 0.0;
        public static double FRONT_INTAKE = 0.0;
        public static double PREPARE_BACK_INTAKE = 0.1;
        public static double BACK_INTAKE = 0.3;
        public static double PREPARE_SCORE_BUCKET = 0.0;
        public static double SCORE_BUCKET = 0.0;
        public static double PREPARE_SCORE_SPEC = 0.0;
        public static double SCORE_SPEC = 0.0;

        public static double getPreset(EndEffectorCommands.EndEffectorPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case PREPARE_FRONT_INTAKE -> PREPARE_FRONT_INTAKE;
                case FRONT_INTAKE -> FRONT_INTAKE;
                case PREPARE_BACK_INTAKE -> PREPARE_BACK_INTAKE;
                case BACK_INTAKE -> BACK_INTAKE;
                case PREPARE_SCORE_BUCKET -> PREPARE_SCORE_BUCKET;
                case SCORE_BUCKET -> SCORE_BUCKET;
                case PREPARE_SCORE_SPEC -> PREPARE_SCORE_SPEC;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }

    @Config
    public class WristPivotPresets {
        public static double TRAVEL = 0.0;
        public static double PREPARE_FRONT_INTAKE = 0.0;
        public static double FRONT_INTAKE = 0.0;
        public static double PREPARE_BACK_INTAKE = 1.0;
        public static double BACK_INTAKE = 0.9;
        public static double PREPARE_SCORE_BUCKET = 0.0;
        public static double SCORE_BUCKET = 0.0;
        public static double PREPARE_SCORE_SPEC = 0.0;
        public static double SCORE_SPEC = 0.0;

        public static double getPreset(EndEffectorCommands.EndEffectorPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case PREPARE_FRONT_INTAKE -> PREPARE_FRONT_INTAKE;
                case FRONT_INTAKE -> FRONT_INTAKE;
                case PREPARE_BACK_INTAKE -> PREPARE_BACK_INTAKE;
                case BACK_INTAKE -> BACK_INTAKE;
                case PREPARE_SCORE_BUCKET -> PREPARE_SCORE_BUCKET;
                case SCORE_BUCKET -> SCORE_BUCKET;
                case PREPARE_SCORE_SPEC -> PREPARE_SCORE_SPEC;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }

    @Config
    public class WristPresets {
        public static double TRAVEL = 0.5;
        public static double PREPARE_FRONT_INTAKE = 0.5;
        public static double FRONT_INTAKE = 0.5;
        public static double PREPARE_BACK_INTAKE = 0.5;
        public static double BACK_INTAKE = 0.5;
        public static double PREPARE_SCORE_BUCKET = 0.5;
        public static double SCORE_BUCKET = 0.5;
        public static double PREPARE_SCORE_SPEC = 0.5;
        public static double SCORE_SPEC = 0.5;

        public static double getPreset(EndEffectorCommands.EndEffectorPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case PREPARE_FRONT_INTAKE -> PREPARE_FRONT_INTAKE;
                case FRONT_INTAKE -> FRONT_INTAKE;
                case PREPARE_BACK_INTAKE -> PREPARE_BACK_INTAKE;
                case BACK_INTAKE -> BACK_INTAKE;
                case PREPARE_SCORE_BUCKET -> PREPARE_SCORE_BUCKET;
                case SCORE_BUCKET -> SCORE_BUCKET;
                case PREPARE_SCORE_SPEC -> PREPARE_SCORE_SPEC;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }

    @Config
    public class ClawPresets {
        public static double OPEN = 1.0;
        public static double CLOSE = 0.75;

        public static double getPreset(EndEffectorCommands.ClawPreset presetName) {
            return switch (presetName) {
                case OPEN -> OPEN;
                case CLOSE -> CLOSE;
            };
        }
    }
}
