package HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class LinearSlideController {

    DcMotorEx slideMotor;
    int motorDownPosition;

    // motorDownPosition pos DOWN
    // -1510 pos LOW
    // -2800 pos MID
    // -4100 pos HIGH

    public enum LinearSlidePosition {
        DOWN,
        LOW,
        MID,
        HIGH
    }

    public LinearSlideController(DcMotorEx slideMotor, int motorDownPosition) {
        this.slideMotor = slideMotor;
        this.motorDownPosition = motorDownPosition;
    }

    public void update(float leftTriggerPower, float rightTriggerPower) {

        if(leftTriggerPower > 0.1) {

            slideMotor.setPower(leftTriggerPower);

        } else if(rightTriggerPower > 0.1) {

            slideMotor.setPower(-rightTriggerPower);

        } else {

            slideMotor.setPower(-0.1);

        }

    }

    public void waitForMotor() {
        while(slideMotor.isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("Uncaught", e);
            }
        }
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void goToPos(LinearSlidePosition pos, float power) {
        switch (pos) {
            case DOWN:
                slideMotor.setTargetPosition(motorDownPosition);
            case LOW:
                slideMotor.setTargetPosition(motorDownPosition - 1510);
            case MID:
                slideMotor.setTargetPosition(motorDownPosition - 2800);
            case HIGH:
                slideMotor.setTargetPosition(motorDownPosition - 4100);
            default:
                break;
        }

        slideMotor.setPower(power);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}
