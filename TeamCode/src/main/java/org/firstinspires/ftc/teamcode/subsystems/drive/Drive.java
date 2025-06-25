package org.firstinspires.ftc.teamcode.subsystems.drive;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.auto.PoseStorage;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {
    private final Follower drive;
    private final Telemetry telemetry;

    public Drive(HardwareMap hwMap, Telemetry telemetry) {
        drive = new Follower(hwMap, DriveConstants.FConstants.class, DriveConstants.LConstants.class);

        drive.setPose(PoseStorage.currentPose);

        this.telemetry = telemetry;
    }

    @Override
    public void periodic() {
        try {
            drive.update();

            // Hopefully commenting this out will increase loop times
            drive.drawOnDashBoard();
            drive.telemetryDebug(telemetry);
        } catch (Exception ignored) {
        }
    }

    public void setPose(Pose pose) {
        drive.setPose(pose);
    }

    public void startTeleopDrive() {
        drive.startTeleopDrive();
    }

    public void drive(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier rotationSupplier) {
        drive.setTeleOpMovementVectors(xSupplier.getAsDouble(), ySupplier.getAsDouble(), rotationSupplier.getAsDouble(), false);
    }

    public Pose getPose() {
        return drive.getPose();
    }

    public PathBuilder getPathBuilder() {
        return drive.pathBuilder();
    }

    public void followPath(PathChain path) {
        drive.followPath(path, true);
    }

    public boolean isFinished() {
        return !drive.isBusy();
    }

    public static Command followPath(Drive drive, Pose startPose, PathChain path) {
        return Commands.runOnce(() -> drive.setPose(startPose), drive)
                .andThen(Commands.runOnce(() -> drive.followPath(path), drive));
    }

    public static Command followPath(Drive drive, Supplier<PathChain> path) {
        return Commands.runOnce(() -> drive.followPath(path.get()), drive).andThen(Commands.waitUntil(drive::isFinished));
    }




    public void driveRobotCentric(DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier rotationSupplier) {
        drive.setTeleOpMovementVectors(xSupplier.getAsDouble(), ySupplier.getAsDouble(), rotationSupplier.getAsDouble(), true);
    }

}
