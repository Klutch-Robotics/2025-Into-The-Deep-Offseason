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
    public static TunablePose startPose = new TunablePose(8, 112, -90);

    public static TunablePose scoreBucket = new TunablePose(14, 130, -45);

    public static TunablePose pickUpOne = new TunablePose(24, 120, 0);
    public static TunablePose pickUpTwo = new TunablePose(24, 132, 0);
    public static TunablePose pickUpThree = new TunablePose(24, 132, 210);

    public static double prepareScoreSampParameter = 0.0;

    public static Command blueAuto(Superstructure superstructure, Constants.AllianceColor color) {
        return Commands.sequence(
                DriveCommands.setPose(superstructure.drive(), startPose::getPose),
                new ParallelDeadlineGroup(
                        DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                ),
                SuperstructureCommands.releaseClawSamp(superstructure).withTimeout(1),
                new ParallelCommandGroup(
                        DriveCommands.driveToPose(superstructure.drive(),pickUpOne::getPose),
                        SuperstructureCommands.seekPieceHalfAuto(superstructure)
                ),


                /*DriveCommands.driveToPose(
                        superstructure.drive(),
                        scoreBucket::getPose,
                        SuperstructureCommands.setSuperstructureState(
                                superstructure,
                                SuperstructureCommands.SuperstructureState.PREPARE_SCORE_SAMP,
                                color),
                        () -> prepareScoreSampParameter),
                SuperstructureCommands.setSuperstructureState(
                        superstructure,
                        SuperstructureCommands.SuperstructureState.SCORE_SPEC,
                        color),*/

                DriveCommands.driveToPose(superstructure.drive(), pickUpOne::getPose),
                DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),

                DriveCommands.driveToPose(superstructure.drive(), pickUpTwo::getPose),
                DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose),

                DriveCommands.driveToPose(superstructure.drive(), pickUpThree::getPose),
                DriveCommands.driveToPose(superstructure.drive(), scoreBucket::getPose)

        );
    }

}
