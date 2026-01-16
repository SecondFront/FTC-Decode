package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.sequences.DrivingSequences;

import java.util.HashMap;
import java.util.Map;

@Disabled
@TeleOp(name="Red Auto Goal", group="Linear OpMode")
public class RedAutoGoal extends MainAuto{
    @Override
    protected void setAlliance() {
        alliance = Alliance.RED;
    }

    @Override
    protected void setSequences() {
        // Create a map of named locations for the Blue Alliance
        Map<String, Pose2d> locations = new HashMap<>();

        // Add your locations here with their (x, y, heading) coordinates
        // The heading should be in radians.
        // TODO Add correct locations
        locations.put("start", new Pose2d(112.625, 136.625, Math.toRadians(90)));
        locations.put("mainScorePos", new Pose2d(96, 96, Math.toRadians(44)));
        locations.put("a3Start", new Pose2d(96, 84, Math.toRadians(0)));
        locations.put("a3Finish", new Pose2d(128.625, 84, Math.toRadians(0)));
        locations.put("a2Start", new Pose2d(96, 96, Math.toRadians(0)));

        // Create the driving sequences object with the locations map
        driveSequences = new DrivingSequences(robot.drivetrain.drive, locations);
    }
}
