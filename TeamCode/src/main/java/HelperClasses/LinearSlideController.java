package HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearSlideController {

    DcMotorEx slideMotor;
    int motorDownPosition;

    public LinearSlideController(DcMotorEx slideMotor, int motorDownPosition) {
        this.slideMotor = slideMotor;
        this.motorDownPosition = motorDownPosition;
    }

    public void update(float leftTriggerPower, float rightTriggerPower) {

        if(rightTriggerPower > 0.1) {

            slideMotor.setPower(rightTriggerPower);

        } else if(leftTriggerPower > 0.1) {

            slideMotor.setPower(-leftTriggerPower);

        } else {

            slideMotor.setPower(0.1);

        }

    }

}
