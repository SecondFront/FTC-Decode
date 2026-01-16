package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;

/**
 * This class will contain all the sensor-like elements
 * Camera, color sensor, lights, etc
 * // TODO Add camera code, color sensor code, and light code
 */

public class Sensing {
    // Declare hardware
    PwmControl light;

    public Sensing(HardwareMap hw) {
        light = hw.get(PwmControl.class, "light");
    }

}
