package org.firstinspires.ftc.teamcode.subsystems.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    private final Telemetry telemetry;

    private final Limelight3A limelight;

    public Vision(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        limelight = hwMap.get(Limelight3A.class, "limelight");

        limelight.start();
    }


    @Override
    public void periodic() {
        LLResult result = limelight.getLatestResult();

        result.getPythonOutput();

        telemetry.addLine("Vision");
        telemetry.addData("Angle", result.getPythonOutput()[0]);
        telemetry.addData("Tx", result.getPythonOutput()[1]);
        telemetry.addData("Ty", result.getPythonOutput()[2]);

    }

    public double getAngle() {
        return limelight.getLatestResult().getPythonOutput()[0];
    }

    public double getTx() {
        return limelight.getLatestResult().getPythonOutput()[1];
    }

    public double getTy() {
        return limelight.getLatestResult().getPythonOutput()[2];
    }

}
