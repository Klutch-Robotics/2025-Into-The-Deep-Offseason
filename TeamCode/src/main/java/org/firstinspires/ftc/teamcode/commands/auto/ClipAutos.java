package org.firstinspires.ftc.teamcode.commands.auto;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.DriveCommands;
import org.firstinspires.ftc.teamcode.commands.superstructure.SuperstructureCommands;
import org.firstinspires.ftc.teamcode.subsystems.Superstructure;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

@Config
public class ClipAutos {
    public static TunablePose startPose = new TunablePose(6, 102, -90);

    public static TunablePose scoreBucket = new TunablePose(17, 130, -45);



    public static Command bucketAuto(Superstructure superstructure, Constants.AllianceColor color) {
        return Commands.sequence(
                DriveCommands.setPose(superstructure.drive(), startPose::getPose),
                new ParallelDeadlineGroup(
                        DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                )






        );
    }

}
