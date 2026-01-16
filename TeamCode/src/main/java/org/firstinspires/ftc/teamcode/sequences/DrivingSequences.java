package org.firstinspires.ftc.teamcode.sequences;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

import java.util.Arrays;
import java.util.Map;

/**
 * A class to manage and execute driving sequences for the robot.
 */
public class DrivingSequences {

    private final MecanumDrive drive;
    private final Map<String, Pose2d> locations;

    /**
     * Constructs a new DrivingSequences object.
     *
     * @param drive The MecanumDrive instance to use for driving.
     * @param locations A map of named locations (poses).
     */
    public DrivingSequences(MecanumDrive drive, Map<String, Pose2d> locations) {
        this.drive = drive;
        this.locations = locations;
    }

    /**
     * Gets the map of named locations.
     *
     * @return A map of named locations (poses).
     */
    public Map<String, Pose2d> getLocations() {
        return locations;
    }

    /**
     * Creates an action to drive the robot to a named location.
     * This method is non-blocking and returns an Action that can be executed.
     *
     * @param name The name of the location to drive to.
     * @return An {@link Action} that drives the robot to the specified location.
     */
    public Action goToLinear(String name) {
        Pose2d target = locations.get(name);

        if (target == null) {
            throw new IllegalArgumentException("Unknown location name: " + name);
        }

        return drive.actionBuilder(drive.localizer.getPose())
                .strafeToSplineHeading(target.position, target.heading)
                .build();
    }

    public Action goToLinearSlow(String name) {
        Pose2d target = locations.get(name);

        // Create a slower velocity constraint (e.g., 20 inches per second)
        VelConstraint slowVel = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(10.0),
                new AngularVelConstraint(Math.PI / 2)
        ));

        if (target == null) {
            throw new IllegalArgumentException("Unknown location name: " + name);
        }

        return drive.actionBuilder(drive.localizer.getPose())
                .strafeToSplineHeading(target.position, target.heading, slowVel)
                .build();
    }

}
