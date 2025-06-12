package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.lib.ftclib.opmode.CommandOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "😡RedBucketAuto", group = "Auto")
public class RedBucketAuto extends CommandOpMode {
    private Telemetry robotTelemetry;
    private RobotContainer robotContainer;

    @Override
    public void robotInit() {
        robotTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robotContainer = new RobotContainer(hardwareMap, telemetry, gamepad1, gamepad2, 1, Constants.AllianceColor.RED); //Uses heavily modified untested hardware
    }

    @Override
    public void robotPeriodic() {
        super.robotPeriodic();
        robotTelemetry.update();

    }

    @Override
    public void enabledInit() {
        robotContainer.getAutoCommand(1).schedule();
    }

}