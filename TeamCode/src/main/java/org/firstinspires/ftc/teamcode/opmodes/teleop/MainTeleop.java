/**
 * @author Jasper Burroughs
 * TeleOp program with field/robot-centric toggle using ODO heading
 */

package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.sequences.*;


@TeleOp(name="Teleop", group="Linear OpMode")
public abstract class MainTeleop extends LinearOpMode {

    // robot object
    protected Robot robot;
    protected Alliance alliance;
    protected DrivingSequences driveSequences;

    // Current action variable to keep track of what actions are running
    Action currentAction = null;

    public enum Alliance {
        RED, BLUE
    }

    double percent;
    boolean fieldCentric = true;
    boolean togglePressed = false;

    /** This method is "abstract" — no body here.
     *  Each subclass must define how the alliance is initialized.
     */
    protected abstract void setAlliance();
    
    protected abstract void setSequences();

    @Override
    public void runOpMode() {

        // Declare Robot class to initialize all the hardware
        robot = new Robot(hardwareMap);

        percent = 0.6;

        setAlliance();
        setSequences();

        while (opModeIsActive()) {
            // Check for button presses to start a new action
            // TODO Adjust what buttons do what
            if (gamepad1.a && currentAction == null) {
                currentAction = driveSequences.goToLinear("mainScorePos");
            }
            
            robot.drivetrain.updatePose();
            
            // Run the current action. If it finishes, it will be set to null.
            actionLoop();
            
            // Only allow driver control if no action is running.
            if (currentAction == null) {
                driveLoop();
            }

            // telemetry
            double heading = Math.toDegrees(robot.drivetrain.getPose().heading.toDouble());
            telemetry.addData("Drive Mode", fieldCentric ? "Field-Centric" : "Robot-Centric");
            telemetry.addData("Slow Mode", percent < 1.0);
            telemetry.addData("Heading", heading);
            telemetry.update();
        }

        //robot.stopAll();
    }

    public void driveLoop() {

        // read joystick inputs
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rot = gamepad1.right_stick_x;

        // toggle field/robot-centric driving TODO I think FTC added a new function this year .wasPressed() or smth
        if (gamepad1.y && !togglePressed) {
            fieldCentric = !fieldCentric;
            togglePressed = true;
        }
        if (!gamepad1.y) togglePressed = false;

        // flip strafe for red alliance
        if (alliance == Alliance.RED) x *= -1;

        double robotX = x;
        double robotY = y;

        // field-centric conversion using ODO heading
        if (fieldCentric) {
            double heading = robot.drivetrain.getPose().heading.toDouble(); // radians
            double tempX = x * Math.cos(heading) - y * Math.sin(heading);
            double tempY = x * Math.sin(heading) + y * Math.cos(heading);

            robotX = tempX;
            robotY = tempY;
        }

        // slow mode
        if (gamepad1.left_trigger > 0.4) percent = 0.35;
        else if (gamepad1.right_trigger > 0.4) percent = 1;
        else percent = 0.6;

        // micro adjustments with dpad
        if (gamepad1.dpad_up) robotY = 0.18;
        if (gamepad1.dpad_down) robotY = -0.18;
        if (gamepad1.dpad_left) robotX = -0.18;
        if (gamepad1.dpad_right) robotX = 0.18;

        // reset ODO heading (gyro)
        if (gamepad1.start && gamepad1.a) robot.drivetrain.updatePose(); // or pinpoint.resetPosAndIMU() if needed

        // send calculated values to drivetrain
        robot.drivetrain.drive(robotY, robotX, rot, percent);
    }

    /**
     * Handles the actions control loop
     */
    public void actionLoop() {
        if (currentAction != null) {
            // Run the action and send telemetry to the dashboard
            if (!currentAction.run(new TelemetryPacket())) {
                // If the action is finished, set it to null
                currentAction = null;
            }
        }
    }
}
