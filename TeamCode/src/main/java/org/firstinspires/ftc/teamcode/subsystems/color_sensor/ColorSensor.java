package org.firstinspires.ftc.teamcode.subsystems.color_sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ColorSensor extends SubsystemBase {
    private final com.qualcomm.robotcore.hardware.ColorSensor colorSensor;
    private final Telemetry telemetry;

    public ColorSensor(HardwareMap hardwareMap, Telemetry telemetry){
        colorSensor = hardwareMap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, "sensor_color");
        this.telemetry = telemetry;
    }

    @Override
    public void periodic() {
        // Don't read the color sensor values every loop, it is too slow
        // Loop times are critical or so they say
    }

    // Todo: Detect yellow
    public boolean isSamplePresent(Constants.AllianceColor allianceColor) {
        if (allianceColor == Constants.AllianceColor.RED) {
            return colorSensor.red() > ColorSensorConstants.RED_THRESH;
        } else if (allianceColor == Constants.AllianceColor.BLUE) {
            return colorSensor.blue() > ColorSensorConstants.BLUE_THRESH;
        } else {
            return false;
        }
    }

    public boolean isSpecPresent(Constants.AllianceColor allianceColor) {
        if (allianceColor == Constants.AllianceColor.RED) {
            return colorSensor.red() > ColorSensorConstants.RED_THRESH;
        } else if (allianceColor == Constants.AllianceColor.BLUE) {
            return colorSensor.blue() > ColorSensorConstants.BLUE_THRESH;
        } else {
            return false;
        }
    }

    public boolean isPiecePresent(Constants.AllianceColor allianceColor) {
        if (allianceColor == Constants.AllianceColor.RED) {
            return colorSensor.red() > ColorSensorConstants.RED_THRESH;
        } else if (allianceColor == Constants.AllianceColor.BLUE) {
            return colorSensor.blue() > ColorSensorConstants.BLUE_THRESH;
        } else {
            return false;
        }
    }
}