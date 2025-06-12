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

    public static TunablePose scoreSpec = new TunablePose(17, 130, -45);



    public static TunablePose pickUpintOne = new TunablePose(32, 125, 0);
    public static TunablePose pickUpOne = new TunablePose(32, 125, 0);
    public static TunablePose pickUpTwo = new TunablePose(32, 134, 0);
    public static TunablePose pickUpThree = new TunablePose(32, 134, 0);


    public static Command clipAuto(Superstructure superstructure, Constants.AllianceColor color) {
        return Commands.sequence(
                DriveCommands.setPose(superstructure.drive(), startPose::getPose),
                //Score Preload
                new ParallelDeadlineGroup(
                        DriveCommands.driveToPose(superstructure.drive(), scoreSpec::getPose),
                        SuperstructureCommands.prepareScoreSamp(superstructure)
                ),
                SuperstructureCommands.releaseClawSamp(superstructure).withTimeout(.1),
                //Pickup and throw spike mark samples into HP Zone
                DriveCommands.splineToPose(superstructure.drive(), scoreSpec::getPose,pickUpOne::getPose),
                SuperstructureCommands.seekPieceHalfAuto(superstructure),
                //toss
                DriveCommands.driveToPose(superstructure.drive(), pickUpTwo::getPose),
                SuperstructureCommands.seekPieceHalfAuto(superstructure),
                //toss
                DriveCommands.driveToPose(superstructure.drive(), pickUpThree::getPose),
                SuperstructureCommands.seekPieceHalfAuto(superstructure),
                //toss

                //pickup and score from HP
                DriveCommands.driveToPose(superstructure.drive(), pickUpThree::getPose)
                //score spec command?








        );
    }

}
