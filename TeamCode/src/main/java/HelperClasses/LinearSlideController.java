package HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


public class LinearSlideController {

    DcMotorEx slideMotor;
    public int motorDownPosition;

    // motorDownPosition pos DOWN
    // -1510 pos LOW
    // -2900 pos MID
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

        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if(leftTriggerPower > 0.1) {

            if(slideMotor.getCurrentPosition() >= motorDownPosition - 50) {
                slideMotor.setPower(0.4);
            } else if(slideMotor.getCurrentPosition() >= motorDownPosition - 5) {
                slideMotor.setPower(0.1);
            } else {
                slideMotor.setPower(leftTriggerPower);
            }

        } else if(rightTriggerPower > 0.1) {

            if(slideMotor.getCurrentPosition() <= motorDownPosition - 4100 + 50) {
                slideMotor.setPower(-0.5);
            } else if(slideMotor.getCurrentPosition() >= motorDownPosition - 4100 + 5) {
                slideMotor.setPower(-rightTriggerPower);
            } else {
                slideMotor.setPower(-0.1);
            }


        } else {

            slideMotor.setPower(-0.1);

        }

    }

    public void goToPos(LinearSlidePosition pos, double power) {
        switch (pos) {
            case DOWN:
                slideMotor.setTargetPosition(motorDownPosition);
                break;
            case LOW:
                slideMotor.setTargetPosition(motorDownPosition - 1510);
                break;
            case MID:
                slideMotor.setTargetPosition(motorDownPosition - 2900);
                break;
            case HIGH:
                slideMotor.setTargetPosition(motorDownPosition - 4100);
                break;
            default:
                break;
        }

        slideMotor.setPower(power);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void goToPosInStack(int coneNumber, double power) {

        slideMotor.setTargetPosition((int) (motorDownPosition - ((coneNumber - 1) * 170))); // 125 is pos per cone in stack

        slideMotor.setPower(-power);

        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

}
