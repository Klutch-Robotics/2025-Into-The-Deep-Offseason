package org.firstinspires.ftc.teamcode.subsystems.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    private final Telemetry telemetry;

    private final Limelight3A limelight;

    private double angle = 0;
    private double tx = 0;
    private double ty = 0;
    private boolean seesPiece = false;
    private double area = 0;

    public Vision(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        limelight = hwMap.get(Limelight3A.class, "limelight");

        limelight.start();
    }

    @Override
    public void periodic() {
        LLResult result = limelight.getLatestResult();

        result.getPythonOutput();

        angle = result.getPythonOutput()[0];
        tx = result.getPythonOutput()[1];
        ty = result.getPythonOutput()[2];
        seesPiece = result.getPythonOutput()[3] == 1.0;
        area = result.getPythonOutput()[4];

        telemetry.addLine("Vision");
        telemetry.addData("Angle", angle);
        telemetry.addData("Tx", tx);
        telemetry.addData("Ty", ty);
    }

    public double getAngle() {
        return angle;
    }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }

    public boolean seesPiece() {
        return seesPiece;
    }

    public double getArea() {
        return area;
    }

    public void setPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
    }

    public static Command setPipeline(Vision vision, int pipeline) {
        return Commands.runOnce(() -> vision.setPipeline(pipeline), vision);
    }

    public static Command setSpecPipeline(Vision vision, Constants.AllianceColor allianceColor) {
        if (allianceColor == Constants.AllianceColor.BLUE) {
            return Vision.setPipeline(vision, 0);
        } else if (allianceColor == Constants.AllianceColor.RED) {
            return Vision.setPipeline(vision, 1);
        } else {
            throw new IllegalArgumentException("Invalid alliance color: " + allianceColor);
        }
    }

    public static Command setSampPipeline(Vision vision, Constants.AllianceColor allianceColor) {
        if (allianceColor == Constants.AllianceColor.BLUE) {
            return Vision.setPipeline(vision, 2);
        } else if (allianceColor == Constants.AllianceColor.RED) {
            return Vision.setPipeline(vision, 3);
        } else {
            throw new IllegalArgumentException("Invalid alliance color: " + allianceColor);
        }
    }

    public static Command setYellowPipeline(Vision vision) {
        return Vision.setPipeline(vision, 4);
    }
}
