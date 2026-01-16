package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.RoadRunner.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.sequences.DrivingSequences;

/**
 * This class controls controls all of the other subsystems
 */
public class Robot{
    // This is where all the subsystems will be declared
    private GoBildaPinpointDriver pinpoint;
    public Drivetrain drivetrain;
    public Intake intake;
    public Shooter shooter;
    public Sensing sensing;

    /**
     * All subsystems initialized in constructor by passing hw
     * @param hw - the hardware map for the robot
     */
    public Robot(HardwareMap hw, DrivingSequences driveSequences) {
        Pose2d start = driveSequences.getLocations().get("start");
        double x = 0;
        double y = 0;
        double heading = 0;
        if (start != null) {
            x = start.position.x;
            y = start.position.x;
            heading = start.position.x;
            pinpoint.setPosX(start.position.x, INCH);
            pinpoint.setPosY(start.position.y, INCH);
            pinpoint.setHeading(start.heading.toDouble(), AngleUnit.RADIANS);
        }

        // Get the start pose
        pinpoint = hw.get(GoBildaPinpointDriver.class, "pinpoint");
        // Configure the pinpoint localization system

        // This is where all the subsystems will be initialized
        drivetrain = new Drivetrain(hw, new Pose2d(x, y, heading));
        sensing = new Sensing(hw);
        intake = new Intake(hw, sensing);
        shooter = new Shooter(hw);
    }

    public Robot(HardwareMap hw) {
        // Get the start pose
        pinpoint = hw.get(GoBildaPinpointDriver.class, "pinpoint");
        // Configure the pinpoint localization system

        double x = pinpoint.getPosition().getX(INCH);
        double y = pinpoint.getPosition().getY(INCH);
        double heading = pinpoint.getPosition().getHeading(AngleUnit.RADIANS);


        // This is where all the subsystems will be initialized
        drivetrain = new Drivetrain(hw, new Pose2d(x, y, heading));
        sensing = new Sensing(hw);
        intake = new Intake(hw, sensing);
        shooter = new Shooter(hw);
    }

    /**
     * Stops all subsystems for robot's safety during match
     */
    public void stopAll() {
        drivetrain.stop();
        intake.stop();
        shooter.stop();
    }
}