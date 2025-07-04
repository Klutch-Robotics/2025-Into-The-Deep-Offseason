package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;

import org.firstinspires.ftc.teamcode.lib.controller.SquIDController;
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;
import org.firstinspires.ftc.teamcode.subsystems.vision.Vision;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

@Config
public class DriveCommands {
    public static double PIECE_TRANSLATIONALP = 0.1;
    public static double PIECE_ROTATIONALP = 0.01;

    public static double PIECE_XSETPOINT = 0.0;
    public static double PIECE_YSETPOINT = 0.0;
    public static double PIECE_OMEGASETPOINT = 0.0;

    public static double DRIVE_STARTING_SPEED = 0.5;

    private DriveCommands() {}

    /**
     * Field relative drive command using two joysticks (controlling linear and angular velocities).
     */
    public static Command joystickDrive(
            Drive drive,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier omegaSupplier) {
        return Commands.runOnce(drive::startTeleopDrive, drive).andThen(Commands.run(() -> drive.drive(
                xSupplier,
                ySupplier,
                omegaSupplier)));
    }

    public static Command joystickDriveGas(
            Drive drive,
            DoubleSupplier speedSupplier,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier omegaSupplier) {
        return Commands.runOnce(drive::startTeleopDrive, drive).andThen(Commands.run(() -> drive.drive(
                () -> xSupplier.getAsDouble() * (DRIVE_STARTING_SPEED + speedSupplier.getAsDouble()*.5),

                () -> ySupplier.getAsDouble() * (DRIVE_STARTING_SPEED + speedSupplier.getAsDouble()*.5),

                (omegaSupplier)

        )));
    }

    public static Command driveRobotCentric(
            Drive drive,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier omegaSupplier) {
        return Commands.runOnce(drive::startTeleopDrive, drive).andThen(Commands.run(() -> drive.driveRobotCentric(
                xSupplier,
                ySupplier,
                omegaSupplier)));
    }

    /**
     * Field relative drive command using joystick for linear control and PID for angular control.
     * Possible use cases include snapping to an angle, aiming at a vision target, or controlling
     * absolute rotation with a joystick.
     */
    public static Command joystickDriveAtAngle(
            Drive drive,
            DoubleSupplier xSupplier,
            DoubleSupplier ySupplier,
            DoubleSupplier rotationSupplier) {

        // Create PID controller
        SquIDController angleController =
                new SquIDController(
                        0.1);

        angleController.enableContinuousInput(-Math.PI, Math.PI);

        // Construct command
        return Commands.runOnce(drive::startTeleopDrive, drive).andThen(
                Commands.run(
                        () -> {
                            // Calculate angular speed
                            double omega =
                                    angleController.calculate(
                                            drive.getPose().getHeading(), rotationSupplier.getAsDouble());

                            drive.drive(
                                    xSupplier,
                                    ySupplier,
                                    () -> omega);
                        },
                        drive));
    }

    public static Command driveToPiece(Drive drive, Vision vision) {
        SquIDController translationalController =
                new SquIDController(
                        PIECE_TRANSLATIONALP);

        SquIDController omegaController =
                new SquIDController(
                        PIECE_ROTATIONALP);

        omegaController.enableContinuousInput(0, 180);

        return Commands.runOnce(drive::startTeleopDrive, drive).andThen(
                Commands.run(
                        () -> {
                            // Calculate angular speed
                            double xEffort =
                                    translationalController.calculate(PIECE_TRANSLATIONALP,
                                            PIECE_XSETPOINT, vision.getTx());

                            double yEffort =
                                    -translationalController.calculate(PIECE_TRANSLATIONALP,
                                            PIECE_YSETPOINT, vision.getTy());

                            double omegaEffort =
                                    omegaController.calculate(PIECE_ROTATIONALP,
                                        PIECE_OMEGASETPOINT, vision.getAngle());

                            drive.drive(
                                    () -> xEffort,
                                    () -> yEffort,
                                    () -> omegaEffort);
                        },
                        drive));
    }

    public static Command driveToPose(Drive drive, Supplier<Pose> pose) {
        return Drive.followPath(
                drive,
                () -> new PathBuilder()
                        .addPath(new Path(new BezierLine(drive.getPose(), pose.get())))
                        .setLinearHeadingInterpolation(drive.getPose().getHeading(), pose.get().getHeading())
                        .build());
    }

    public static Command splineToPose(Drive drive, Supplier<Pose> pose,Supplier<Pose> pose2) {
        return Drive.followPath(
                drive,
                () -> new PathBuilder()


                        .addPath(new Path(new BezierCurve(drive.getPose(), pose.get(),pose2.get())))
                        .setLinearHeadingInterpolation(drive.getPose().getHeading(), pose2.get().getHeading())


                        .build());
    }

    public static Command splineToPose(Drive drive, Supplier<Pose> pose, Supplier<Pose> pose2, Command command, DoubleSupplier commandActivationPoint) {
        return Drive.followPath(
                drive,
                () -> new PathBuilder()
                        .addPath(new Path(new BezierCurve(drive.getPose(), pose.get(),pose2.get())))
                        .setLinearHeadingInterpolation(drive.getPose().getHeading(), pose2.get().getHeading())
                        .addParametricCallback(commandActivationPoint.getAsDouble(), command::schedule)
                        .build());
    }



    public static Command driveToPose(Drive drive, Supplier<Pose> pose, Command command, DoubleSupplier commandActivationPoint) {
        return Drive.followPath(
                drive,
                () -> new PathBuilder()
                        .addPath(new Path(new BezierLine(drive.getPose(), pose.get())))
                        .setLinearHeadingInterpolation(drive.getPose().getHeading(), pose.get().getHeading())
                        .addParametricCallback(commandActivationPoint.getAsDouble(), command::schedule)
                        .build());
    }

    public static Command setPose(Drive drive, Supplier<Pose> pose) {
        return Commands.runOnce(() -> drive.setPose(pose.get()), drive);
    }
}
