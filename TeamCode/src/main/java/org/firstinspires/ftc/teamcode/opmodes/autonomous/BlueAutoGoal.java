package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.sequences.DrivingSequences;

import java.util.HashMap;
import java.util.Map;

@Disabled
@TeleOp(name="Blue Auto Goal", group="Linear OpMode")
public class BlueAutoGoal extends MainAuto{
    @Override
    protected void setAlliance() {
        alliance = Alliance.BLUE;
    }

    @Override
    protected void setSequences() {
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

        // Create the driving sequences object with the locations map
        driveSequences = new DrivingSequences(robot.drivetrain.drive, locations);
    }
}
