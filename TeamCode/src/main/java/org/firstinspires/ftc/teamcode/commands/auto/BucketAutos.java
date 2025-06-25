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
public class BucketAutos {
    public static TunablePose startPose = new TunablePose(6, 102, -90);

    public static TunablePose scoreBucket = new TunablePose(17, 130, -45);

    public static TunablePose pickUpOne = new TunablePose(32, 125, 0);
    public static TunablePose pickUpTwo = new TunablePose(32, 134, 0);

    public static TunablePose subOne = new TunablePose(70,93 , -90);

    public static TunablePose subintOne = new TunablePose(55,120 , -90);
    public static TunablePose pickUpThree = new TunablePose(24, 132, 210);

    public static double prepareScoreSampParameter = 0.0;

    public static Command bucketAuto(Superstructure superstructure, Constants.AllianceColor color) {
        return Commands.sequence(
                DriveCommands.setPose(superstructure.drive(), startPose::getPose),
                new ParallelDeadlineGroup(
                        DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                ),
                SuperstructureCommands.releaseClawSamp(superstructure).withTimeout(.1),

                new ParallelDeadlineGroup(
                        DriveCommands.driveToPose(superstructure.drive(),pickUpOne::getPose),
                        SuperstructureCommands.prepareForIntakeHalf(superstructure)
                ),
                SuperstructureCommands.seekPieceHalfAuto(superstructure),

                new ParallelCommandGroup(
                        DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                ),
                SuperstructureCommands.releaseClawSamp(superstructure).withTimeout(.1),
                new ParallelDeadlineGroup(
                        DriveCommands.driveToPose(superstructure.drive(),pickUpTwo::getPose),
                        SuperstructureCommands.prepareForIntakeHalf(superstructure)
                ),
                
                SuperstructureCommands.seekPieceHalfAuto(superstructure),

                new ParallelCommandGroup(
                        DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                ),

                SuperstructureCommands.releaseClawSamp(superstructure).withTimeout(1),
                new ParallelDeadlineGroup(
                        SuperstructureCommands.prepareForIntakeHalf(superstructure),
                        DriveCommands.splineToPose(superstructure.drive(),subintOne::getPose,subOne::getPose)
                ),
                SuperstructureCommands.seekPieceHalfAuto(superstructure),

                new ParallelCommandGroup(
                        DriveCommands.splineToPose(superstructure.drive(),subintOne::getPose, scoreBucket::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                ),
                SuperstructureCommands.releaseClawSamp(superstructure).withTimeout(1)








        );
    }

}
