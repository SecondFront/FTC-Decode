/**
 * @author Jasper Burroughs
 * This is a test file for basic driving
 */

package org.firstinspires.ftc.teamcode.testFiles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RoadRunner.GoBildaPinpointDriver;

@TeleOp(name="light Test", group="Linear OpMode")
public class lightTest extends LinearOpMode {

    // Robot hardware variables
    Servo light;



    @Override
    public void runOpMode() {

        light = hardwareMap.get(Servo.class, "light");

        waitForStart();

        while (opModeIsActive()) {
            light.getController().setServoPosition(0, 0.556);


        } // End while loop
    } // End runOpMode
}
