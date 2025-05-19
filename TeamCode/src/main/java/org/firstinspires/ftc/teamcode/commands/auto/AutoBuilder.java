package org.firstinspires.ftc.teamcode.commands.auto;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;

import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoBuilder {
    private final Drive drive;

    private final Command autoCommand;
    private Pose previousPose;

    public AutoBuilder(Drive drive) {
        this.drive = drive;
        autoCommand = Commands.none();
        this.previousPose = new Pose(0, 0, 0);
    }

    public AutoBuilder(Drive drive, Pose startPose) {
        this.drive = drive;
        autoCommand = Commands.none();
        this.previousPose = startPose;
    }

    public AutoBuilder addPose(Pose pose) {
        autoCommand.andThen(
                Drive.followPath(
                        drive,
                        new PathBuilder()
                                .addPath(new Path(new BezierLine(previousPose, pose)))
                                .setLinearHeadingInterpolation(previousPose.getHeading(), pose.getHeading())
                                .build()));

         previousPose = pose;

         return this;
    }

    public AutoBuilder addPoseCommand(Pose pose, Command command, double distance) {
        double poseDistance = Math.sqrt(
                Math.pow(previousPose.getX() - pose.getX(), 2)
                        + Math.pow(previousPose.getY() - pose.getY(), 2));

        double t = distance / poseDistance;
        if (t > 1) {
            t = 1;
        }

        autoCommand.andThen(
                Drive.followPath(
                        drive,
                        new PathBuilder()
                                .addPath(new Path(new BezierLine(previousPose, pose)))
                                .setLinearHeadingInterpolation(previousPose.getHeading(), pose.getHeading())
                                .addParametricCallback(t, command::schedule)
                                .build()));

        previousPose = pose;

        return this;
    }

    public AutoBuilder forward(double inches) {
        this.addPose(new Pose(previousPose.getX() + inches, previousPose.getY(), previousPose.getHeading()));

        return this;
    }

    public AutoBuilder backward(double inches) {
        this.addPose(new Pose(previousPose.getX() - inches, previousPose.getY(), previousPose.getHeading()));

        return this;
    }

    public AutoBuilder strafeLeft(double inches) {
        this.addPose(new Pose(previousPose.getX(), previousPose.getY() - inches, previousPose.getHeading()));

        return this;
    }

    public AutoBuilder strafeRight(double inches) {
        this.addPose(new Pose(previousPose.getX(), previousPose.getY() + inches, previousPose.getHeading()));

        return this;
    }

    public AutoBuilder turn(double radians) {
        this.addPose(new Pose(previousPose.getX(), previousPose.getY(), previousPose.getHeading() + radians));

        return this;
    }

    public AutoBuilder addCommmand(Command command) {
        autoCommand.andThen(command);

        return this;
    }

    public Command build() {
        return autoCommand;
    }
}
