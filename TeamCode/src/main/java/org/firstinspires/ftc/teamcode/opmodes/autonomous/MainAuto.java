/**
 * @author Jasper Burroughs
 * TeleOp program with field/robot-centric toggle using ODO heading
 */

package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.sequences.*;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.Map;


@Autonomous(name="Auto", group="Linear OpMode")
public abstract class MainAuto extends LinearOpMode {

    // robot object
    protected Robot robot;
    protected Alliance alliance;
    protected DrivingSequences driveSequences;

    // Current action variable to keep track of what actions are running
    Action currentAction = null;

    public enum Alliance {
        RED, BLUE
    }

    /** This method is "abstract" — no body here.
     *  Each subclass must define how the alliance is initialized.
     */
    protected abstract void setAlliance();

    protected abstract void setSequences();

    @Override
    public void runOpMode() {

        // Declare Robot class to initialize all the hardware
        robot = new Robot(hardwareMap, driveSequences);
        ScoringSequences scoringSequences= new ScoringSequences(robot.shooter, robot.intake, driveSequences);


        setAlliance();
        setSequences();

        Map<String, Pose2d> locations = driveSequences.getLocations();

        Action autoStrat = new SequentialAction(
                robot.drivetrain.drive.actionBuilder(locations.get("start")).splineToSplineHeading(locations.get("mainScorePos"), Math.toRadians(-44)).build(),
                //scoringSequences.intake(),
                robot.drivetrain.drive.actionBuilder(robot.drivetrain.drive.localizer.getPose()).splineToSplineHeading(locations.get("a3Start"), Math.toRadians(270)).build()
                //scoringSequences.shoot(0.0) //TODO Make this actually work
                //TODO With this pattern and make sure to test
        );

        if (opModeIsActive()) {
            Actions.runBlocking(autoStrat);
        }

        robot.stopAll();
    }
}
