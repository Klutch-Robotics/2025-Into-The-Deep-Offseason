package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.commands.superstructure.SuperstructureCommands;

@Config
public class Presets {
    @Config
    public class PivotPresets {
        public static double TRAVEL = 0.0;
        public static double INTAKE = 0.0;
        public static double FEED = 0.0;
        public static double SCORE_LOW_BUCKET = 0.0;
        public static double SCORE_HIGH_BUCKET = 0.0;
        public static double SCORE_SPEC = 0.0;

        public static double getPreset(SuperstructureCommands.PinkArmPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case INTAKE -> INTAKE;
                case FEED -> FEED;
                case SCORE_LOW_BUCKET -> SCORE_LOW_BUCKET;
                case SCORE_HIGH_BUCKET -> SCORE_HIGH_BUCKET;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }

    @Config
    public class ElevatorPresets {
        public static double TRAVEL = 0.0;
        public static double INTAKE = 0.0;
        public static double FEED = 0.0;
        public static double SCORE_LOW_BUCKET = 0.0;
        public static double SCORE_HIGH_BUCKET = 0.0;
        public static double SCORE_SPEC = 0.0;

        public static double getPreset(SuperstructureCommands.PinkArmPreset presetName) {
            return switch (presetName) {
                case TRAVEL -> TRAVEL;
                case INTAKE -> INTAKE;
                case FEED -> FEED;
                case SCORE_LOW_BUCKET -> SCORE_LOW_BUCKET;
                case SCORE_HIGH_BUCKET -> SCORE_HIGH_BUCKET;
                case SCORE_SPEC -> SCORE_SPEC;
            };
        }
    }
}
