package org.firstinspires.ftc.teamcode.testFiles;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RoadRunner.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.util.HashMap;
import java.util.Map;

@Autonomous(name="AutoTest", group="Autonomous")
public class RoadRunnerTest extends LinearOpMode {


    @Override
    public void runOpMode() {

        // Create a map of named locations for the Blue Alliance
        Map<String, Pose2d> locations = new HashMap<>();

        // Add your locations here with their (x, y, heading) coordinates
        // The heading should be in radians.
        // TODO Add correct locations

        locations.put("start", new Pose2d(31.375, 136.625, Math.toRadians(90)));
        locations.put("mainScorePos", new Pose2d(48, 96, Math.toRadians(136)));
        locations.put("a3Start", new Pose2d(48, 84, Math.toRadians(180)));
        locations.put("a3Finish", new Pose2d(15.375, 84, Math.toRadians(180)));
        locations.put("a2Start", new Pose2d(48, 96, Math.toRadians(136)));


        MecanumDrive drive = new MecanumDrive(hardwareMap, locations.get("start"));

        TrajectoryActionBuilder myAction = drive.actionBuilder(locations.get("start"))
                //.strafeTo(locations.get("mainScorePos").position)
                .splineToSplineHeading(locations.get("mainScorePos"), Math.toRadians(-44))
                .waitSeconds(3.0)
                .splineToSplineHeading(locations.get("a3Start"), Math.toRadians(270));

        waitForStart();
        if(opModeIsActive()) {
            Actions.runBlocking(myAction.build());
            telemetry.addData("position", drive.localizer.getPose());
            telemetry.update();
        }
    }

}
