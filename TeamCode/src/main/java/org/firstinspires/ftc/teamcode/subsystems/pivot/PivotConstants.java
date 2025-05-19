package org.firstinspires.ftc.teamcode.subsystems.pivot;

import com.acmerobotics.dashboard.config.Config;

@Config
public class PivotConstants {
    public static double kP = 5;
    public static double kG = 0.04;

    public static double ticksToRotations = 1.0 / (751.8 * 5);

    public static double setpoint = -0.12;

    public static double initialPosition = -0.12;
}
