package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * @author Jasper Burroughs
 * @author Aidan Steele
 * Class to control the shooter functionality
 * We are going to want to change to rpm with PID control eventually - someone can work on that later
 */
public class Shooter {

    // Declare hardware here
    DcMotorEx flywheel;

    /**
     * Initialize hardware needed for the shooter here
     * @param hw - hardware map for motors
     */
    public Shooter(HardwareMap hw) {
        // declare hardware here
        flywheel = hw.get(DcMotorEx.class, "flywheel");
    }

    /**
     * Sets the power of the flywheel
     */
    public void setPower(double power) {
        flywheel.setPower(power);
    }

    /**
     * Sets the velocity of the flywheel
     *
     * @param velocity - velocity to set the flywheel to
     */
    public void setVelocity(double velocity) {
        flywheel.setVelocity(velocity);
    }

    /**
     * Set the power of the flywheel to the parameter
     * @param velocity - velocity to set flywheel
     */
    public Action powerFly(double velocity) {
        return new powerFlywheel(velocity);
    }
    public Action powerDownFly() {
        return new powerDownFlywheel();
    }

    /**
     * Safety stop function
     */
    public void stop() {
        setPower(0);
    }

    /**
     * Function to calculate the velocity of the flywheel
     *
     * @param robotPos - current position of the robot
     * @param targetPos - target position of the robot
     */
    public double calcVelocity(Pose2d robotPos, Pose2d targetPos) {
        // For now we will assume the gear ratio for the motor = 1
        double g = 1; //TODO: Potentially change this value
        // And this is without any calculation of air resistance

        // dY in this case is the vertical distance that the ball MUST travel - this will remain constant
        double dY = 0.9398;
        // theta is the launch angle - this will remain constant
        double theta = Math.toRadians(55);
        // r is the radius of the wheel
        double r = 0.06;


        // dX is the horizontal distance that the ball must travel (the formula below is just the distance between two coords)
        double dX = Math.sqrt(Math.pow(targetPos.position.x - robotPos.position.x, 2) + Math.pow(targetPos.position.y - robotPos.position.y, 2));

        double numerator = 9.8 * Math.pow(dX, 2);
        double denominator = 2 * Math.pow(Math.cos(theta), 2) * (dX*Math.tan(theta)-dY);

        double ballVel = Math.sqrt(numerator/denominator);

        double wheelOmega = ballVel/r;
        return wheelOmega * g; // This will be in radians per second
    }

    /**
     * This class controls the action of shooting the flywheel and implements Road Runner's action class
     */
    public class powerFlywheel implements Action {
        private final double velocity;
        private boolean finished = false;
        private double seconds = 0.5;

        public powerFlywheel(double velocity) {
            this.velocity = velocity;
        }

        private double startTime = -1;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            double currentTime = Actions.now(); // Gets current time in seconds
            // Capture the start time on the very first loop
            if (startTime < 0) {
                startTime = currentTime;
            }

            flywheel.setVelocity(velocity);

            // Check if time has expired
            if (currentTime - startTime < seconds) {
                return true; // "true" means keep running this action
            } else {
                return false; // "false" means the action is finished
            }
        }
    }

    public class powerDownFlywheel implements Action {
        private boolean finished = false;
        private double seconds = 0.6;

        private double startTime = -1;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            double currentTime = Actions.now(); // Gets current time in seconds
            // Capture the start time on the very first loop
            if (startTime < 0) {
                startTime = currentTime;
            }

            // Check if time has expired
            if (currentTime - startTime < seconds) {
                return true; // "true" means keep running this action
            } else {
                flywheel.setPower(0);
                return false; // "false" means the action is finished
            }
        }
    }

}

