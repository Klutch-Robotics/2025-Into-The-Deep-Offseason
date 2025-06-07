package org.firstinspires.ftc.teamcode.lib.ftclib.opmode;

import com.qualcomm.hardware.lynx.LynxModule;

import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * As opposed to the general WPILib-style Robot paradigm, FTCLib also offers a command opmode
 * for individual opmodes.
 *
 * @author Jackson
 */
public class CommandOpMode extends TimedRobotOpMode {
//    List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

    @Override
    public void robotInit() {
//        allHubs = hardwareMap.getAll(LynxModule.class);
//
//        for (LynxModule module : allHubs) {
//            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
//        }
    }

    @Override
    public void robotPeriodic() {
//        for (LynxModule module : allHubs) {
//            module.clearBulkCache();
//        }

        CommandScheduler.getInstance().run();
    }

    @Override
    public void enabledInit() {

    }

    @Override
    public void enabledPeriodic() {

    }

    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().reset();
    }
}
