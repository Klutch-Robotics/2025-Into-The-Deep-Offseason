package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.subsystems.color_sensor.ColorSensor;
import org.firstinspires.ftc.teamcode.subsystems.drive.Drive;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.claw.Claw;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.shoulder_pivot.ShoulderPivot;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist.Wrist;
import org.firstinspires.ftc.teamcode.subsystems.end_effector.wrist_pivot.WristPivot;
import org.firstinspires.ftc.teamcode.subsystems.pink_arm.elevator.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.pink_arm.pivot.Pivot;
import org.firstinspires.ftc.teamcode.subsystems.vision.Vision;

// Easy way to group all the subsystems together
public record Superstructure(
        // Drive
        Drive drive,
        // Pink Arm
        Pivot pivot,
        Elevator elevator,
        // End Effector
        ShoulderPivot shoulderPivot,
        WristPivot wristPivot,
        Wrist wrist,
        Claw claw,
        // Sensors
        Vision vision,
        ColorSensor colorSensor) {
}
