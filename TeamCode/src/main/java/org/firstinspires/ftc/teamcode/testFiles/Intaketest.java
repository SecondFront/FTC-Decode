/**
 * @author Jasper Burroughs
 * This is a test file for basic driving
 */

package org.firstinspires.ftc.teamcode.testFiles;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Sensing;

@TeleOp(name="Intake", group="Linear OpMode")
public class Intaketest extends LinearOpMode {

    // Robot hardware variables
    CRServo leftIntake, rightIntake, wheel;
    Servo flap;
    Sensing sensing;
    Intake intake;


    @Override
    public void runOpMode() {

        // Initialize drivetrain motors
        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");
        wheel = hardwareMap.get(CRServo.class, "wheel");
        flap = hardwareMap.get(Servo.class, "flap");

        // Configure motor directions (ensuring forward movement aligns with joystick input)
        sensing = new Sensing(hardwareMap);
        intake = new Intake(hardwareMap, sensing);

        Gamepad currgamepad1 = new Gamepad();
        Gamepad prevgamepad1 = new Gamepad();

        currgamepad1.copy(gamepad1);
        prevgamepad1.copy(gamepad1);

        waitForStart();

        ElapsedTime timer = new ElapsedTime();
        while (opModeIsActive()) {
            currgamepad1.copy(gamepad1);
            if (gamepad1.aWasPressed()) {
                wheel.setPower(1);
            }
            if (gamepad1.xWasPressed()) {
                wheel.setPower(0);
            }
            if (gamepad1.yWasPressed()) {
                wheel.setPower(-1);
            }
            if (gamepad1.dpadDownWasPressed()) {
                leftIntake.setPower(1);
                rightIntake.setPower(-1);
            }
            if (gamepad1.dpadUpWasPressed()) {
                leftIntake.setPower(-1);
                rightIntake.setPower(1);
            }
            if (gamepad1.dpadLeftWasPressed()) {
                leftIntake.setPower(0);
                rightIntake.setPower(0);
            }

            if (gamepad1.left_bumper) {
                double time = timer.seconds();
                telemetry.addData("timer", time);
                telemetry.update();
            }

            if (gamepad1.rightBumperWasPressed()) {
                telemetry.addLine("test");
                telemetry.update();
                Action action = intake.intakeBall(1);
                Actions.runBlocking(action);
            }

            //flap.setPosition(gamepad1.right_stick_y);
            telemetry.addData("flap", flap.getPosition());
            telemetry.update();

        } // End while loop
    } // End runOpMode


    /**
     * Updates all telemetry data
     *
     * @param fieldCentric boolean for field centric mode
     * @param manual boolean for manual control of the arm
     */
    public void updateTelemetry(boolean fieldCentric, boolean manual) {
        telemetry.addLine("Robot Data");

        telemetry.addLine("\nIntake Data");
        telemetry.update();
    }
}
