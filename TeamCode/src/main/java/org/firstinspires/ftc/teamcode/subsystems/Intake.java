package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/**
 * Class to control intake functionality
 * Green will be 0, purple will be 1
 */
public class Intake {

    // declare hardware here
    CRServo leftIntake, rightIntake, wheel;
    Servo flap;
    public Sensing sensing;
    double intakePower = 1;
    double wheelPower = 1;
    // TODO: Adjust the two values below
    double flapPosUp = 0;
    double flapPosDown = 0;
    public int[] log = {0, 0, 0};
    public double timeInterval = (13.7/30)                                                                  ; //TODO Need to find out the time interval for the intake



    public Intake(HardwareMap hw, Sensing sensing) {
        // initialize hardware here
        this.sensing = sensing;
        leftIntake = hw.get(CRServo.class, "leftIntake");
        rightIntake = hw.get(CRServo.class, "rightIntake");
        wheel = hw.get(CRServo.class, "wheel");
        flap = hw.get(Servo.class, "flap");
        // TODO: Figure out flap direction
    }

    /**
     * Power on the intake
     */
    public void intakeOn() {
        // TODO: May need to reverse these directions
        leftIntake.setPower(intakePower);
        rightIntake.setPower(-intakePower);
    }

    /**
     * Send the intake the other direction
     */
    public void outtake() {
        leftIntake.setPower(-intakePower);
        rightIntake.setPower(intakePower);
    }

    /**
     * Stop the Intake
     */
    public void stopIntake() {
        leftIntake.setPower(0);
        rightIntake.setPower(0);
    }

    /**
     * Power on the wheel
     */
    public void wheelUp() {wheel.setPower(wheelPower);}

    /**
     * Send the wheel the other direction
     */
    public void wheelDown() {wheel.setPower(-wheelPower);}

    /**
     * Stop the wheel
     */
    public void stopWheel() {wheel.setPower(0);}

    /**
     * Lift the flap to eject the ball
     */
    public void flapUp() {
        flap.setPosition(flapPosUp);
        subtractFromLog();
    }

    /**
     * Lower the flap
     */
    public void flapDown() {flap.setPosition(flapPosDown);}

    public Action intakeBall(int color) {return new intakeBall(color);}
    public Action intakeBall() {return new intakeBall(-1);}
    public Action getColor(int color) {return new getColor(color);}
    // Call afterEject immediately after eject
    public Action eject() {return new eject();}
    public Action afterEject() {return new afterEject();}
    public Action monitorIntake() {return new monitorIntake();}

    /**
     * Safety stop function
     */
    public void stop() {
        stopIntake();
        stopWheel();
    }

    /**
     * This class controls the storing of balls in the intake
     */
    public class intakeBall implements Action {

        private double startTime = -1;
        private boolean initialized = false;
        private int color;

        public intakeBall(int color) {
            this.color = color;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            if (!initialized) {
                initialized = true;

//                for (int i:log) {
//                    if (i == 0) {
//                        break;
//                    }
//                    return false;
//                }

                //addToLog(color);      // runs exactly once
                wheel.setPower(wheelPower); // start wheel once
                startTime = Actions.now();
            }


//            if (color == -1) {
//                color = sensing.getColor();
//            }
            double currentTime = Actions.now(); // Gets current time in seconds
            // Capture the start time on the very first loop
            if (startTime < 0) {
                startTime = currentTime;
            }
            // Check if time has expired
            if (currentTime - startTime < timeInterval) {
                return true; // "true" means keep running this action
            } else {
                wheel.setPower(0); // Stop the motor
                return false; // "false" means the action is finished
            }
        }
    }

    public class getColor implements Action {

        private double startTime = -1;
        private int color;

        public getColor(int color) {
            this.color = color;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            int index = 0;
            for (int i=0; i < log.length; i++) {
                if (log[i] == color) {
                    index = i;
                    break;
                }
            }

            for (int i=0; i < index; i++) {
                rotateLog();
            }

            double currentTime = Actions.now(); // Gets current time in seconds
            // Capture the start time on the very first loop
            if (startTime < 0) {
                startTime = currentTime;
            }

            // Check if time has expired
            if (currentTime - startTime < timeInterval*index) {
                wheel.setPower(wheelPower);
                return true; // "true" means keep running this action
            } else {
                wheel.setPower(0); // Stop the motor
                return false; // "false" means the action is finished
            }
        }
    }

    public class afterEject implements Action {
        private double startTime = -1;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            subtractFromLog();

            int index = 0;
            for (int i = 1; i < log.length; i++) {
                if (log[i] == 0) {
                    index = i;
                    break;
                }
            }

            for (int i = 0; i < index; i++) {
                rotateLog();
            }

            double currentTime = Actions.now(); // Gets current time in seconds
            // Capture the start time on the very first loop
            if (startTime < 0) {
                startTime = currentTime;
            }

            // Check if time has expired
            if ((currentTime - startTime) < (timeInterval * index)) {
                wheel.setPower(wheelPower);
                return true; // "true" means keep running this action
            } else {
                wheel.setPower(0); // Stop the motor
                return false; // "false" means the action is finished
            }
        }
    }

    public class eject implements Action {
        private double flapTime = 0.4;
        private double startTime = -1;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            double currentTime = Actions.now(); // Gets current time in seconds
            // Capture the start time on the very first loop
            if (startTime < 0) {
                startTime = currentTime;
            }

            // Check if time has expired
            if (currentTime - startTime < flapTime) {
                flap.setPosition(flapPosUp);
                return true; // "true" means keep running this action
            }
            else if (currentTime - startTime > flapTime && currentTime - startTime < flapTime*2){
                flap.setPosition(flapPosDown);
                return true;
            }else {
                wheel.setPower(0); // Stop the motor
                return false; // "false" means the action is finished
            }
        }
    }

    public class monitorIntake implements Action {
        private double startTime = -1;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {

            double currentTime = Actions.now(); // Gets current time in seconds

//            if (sensing.getDistance() < 1) { // Added () to getDistance TODO
//                return false;
//            }
            return true;
        }
    }

    public void addToLog(int color) {
        int first = log[0];
        int second = log[1];
        log[0] = color;
        log[1] = first;
        log[2] = second;
    }

    public void rotateLog() {
        int first = log[0];
        int second = log[1];
        int third = log[2];
        log[0] = third;
        log[1] = first;
        log[2] = second;
    }

    public void subtractFromLog() {
        log[0] = 0;
    }

}
