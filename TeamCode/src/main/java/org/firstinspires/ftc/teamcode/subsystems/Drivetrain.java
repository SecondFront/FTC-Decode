package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;

/**
 * Class to control the drivetrain's movement
 */
public class Drivetrain {
    // Make sure we check that these variables line up with other files for ease
    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;
    public final MecanumDrive drive;


    /**
     * Declare motors for the drivetrain
     * @param hw - hardware map to access the motors
     */
    public Drivetrain(HardwareMap hw, Pose2d startPose) {
        frontLeft = hw.get(DcMotorEx.class, "frontLeft");
        frontRight = hw.get(DcMotorEx.class, "frontRight");
        backLeft = hw.get(DcMotorEx.class, "backLeft");
        backRight = hw.get(DcMotorEx.class, "backRight");

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        drive = new MecanumDrive(hw, startPose);
    }

    /** Update the pose estimate using Road Runner */
    public void updatePose() {
        drive.updatePoseEstimate();
    }

    /** Get the current robot pose from Road Runner */
    public Pose2d getPose() {
        return drive.localizer.getPose();
    }

    /**
     * controls the driving with Roadrunner's function for continuity
     *
     * @param vx forward/backward movement
     * @param vy left/right movement
     * @param omega rotational movement
     * @param percent power percentage to scale movement
     */
    public void drive(double vx, double vy, double omega, double percent) {
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(vx*percent, vy*percent), omega*percent));
    }

    /**
     * Controls the robot drive system using mecanum wheel calculations.
     * Old Driving code
     *
     * @param drive   Forward/backward movement
     * @param strafe  Left/right movement
     * @param rotate  Rotational movement
     * @param percent Power percentage to scale movement
     */
    public void driveOrig(double drive, double strafe, double rotate, double percent) {
        double frontLeftPower = drive + strafe + rotate;
        double frontRightPower = -drive + strafe + rotate;
        double backLeftPower = -drive + strafe - rotate;
        double backRightPower = drive + strafe - rotate;

        // Normalize the values so no value exceeds 1.0
        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        double scale = percent / 100;

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        frontLeft.setPower(scale * frontLeftPower);
        frontRight.setPower(scale * frontRightPower);
        backLeft.setPower(scale * backLeftPower);
        backRight.setPower(scale * backRightPower);
    }

    /**
     * Stops the drivetrain
     */
    public void stop() {
        drive(0.0, 0.0, 0.0, 0.0);
    }
}
