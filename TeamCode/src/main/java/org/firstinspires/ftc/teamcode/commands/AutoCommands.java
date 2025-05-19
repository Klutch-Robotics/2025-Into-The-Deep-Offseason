package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.localization.Pose;

import org.firstinspires.ftc.teamcode.commands.auto.AutoBuilder;
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;
import org.firstinspires.ftc.teamcode.subsystems.pivot.Pivot;

import edu.wpi.first.wpilibj2.command.Command;

@Config
public class AutoCommands {
    public static Command blueAuto(Drive drive, Pivot pivot) {
        return new AutoBuilder(drive, new Pose(10, 0, 0))
                .addPoseCommand(
                        new Pose(40, -12, 0),
                        Pivot.setPosition(pivot, () -> 0.5), 10)
                .addCommmand(Pivot.setPosition(pivot, () -> 0.0))
                .addPose(new Pose(50, 10, Math.toRadians(90)))
                .build();
    }

    public static Command redAuto(Drive drive, Pivot pivot) {
        return new AutoBuilder(drive, new Pose(10, 0, 0))
                .forward(10)
                .strafeLeft(10)
                .addCommmand(Pivot.setPosition(pivot, () -> 0.0))
                .build();
    }
}
