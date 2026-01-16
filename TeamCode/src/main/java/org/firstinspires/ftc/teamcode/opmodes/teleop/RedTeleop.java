package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.sequences.DrivingSequences;

import java.util.HashMap;
import java.util.Map;

@TeleOp(name="Red Teleop", group="Linear OpMode")
public class RedTeleop extends MainTeleop{
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
        locations.put("start", new Pose2d(0, 0, Math.toRadians(90)));
        locations.put("mainScorePos", new Pose2d(0, 0, Math.toRadians(90)));

        // Create the driving sequences object with the locations map
        driveSequences = new DrivingSequences(robot.drivetrain.drive, locations);
    }
}
