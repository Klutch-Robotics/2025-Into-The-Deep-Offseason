package org.firstinspires.ftc.teamcode.commands.auto;

import com.pedropathing.localization.Pose;

public class TunablePose {
    public double x;
    public double y;
    public double heading;

    public TunablePose(double x, double y, double headingDegrees) {
        this.x = x;
        this.y = y;
        this.heading = headingDegrees;
    }

    // Converts it from (0, 0) on center of field to (0, 0) on bottom left corner (right blue corner)
    // This is so that we can use the pedro pathing visualizer website to create poses but still
    // be able to debug using FTC dashboard
    public Pose getPose() {
        return new Pose(x, y, Math.toRadians(heading)).getAsFTCStandardCoordinates();
    }
}
