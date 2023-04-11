package HelperClasses;

import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.units.qual.C;

// middle 0.7, left 1, right 0.25

public class ClawController {

    public enum ClawPosition {
        LEFT,
        MIDDLE,
        RIGHT,
    }

    Servo leftServo;
    Servo rightServo;
    Servo clawServo;
    public boolean isOpen = true;
    public ClawPosition clawPosition = ClawPosition.MIDDLE;
    ButtonToggler toggler = new ButtonToggler();

    public ClawController(Servo leftServo, Servo rightServo, Servo clawServo) {
        this.leftServo = leftServo;
        this.rightServo = rightServo;
        this.clawServo = clawServo;
    }

    void openClaw() {
        leftServo.setPosition(0.45);
        rightServo.setPosition(0.5);
    }

    void closeClaw() {
        leftServo.setPosition(0.17);
        rightServo.setPosition(0.78);
    }

    public void clawRight() {

        if(clawPosition == ClawPosition.LEFT) {
            clawPosition = ClawPosition.MIDDLE;
            clawServo.setPosition(0.7);
        } else if(clawPosition == ClawPosition.MIDDLE) {
            clawPosition = ClawPosition.RIGHT;
            clawServo.setPosition(0.25);
        }

    }

    public void clawLeft() {

        if(clawPosition == ClawPosition.RIGHT) {
            clawPosition = ClawPosition.MIDDLE;
            clawServo.setPosition(0.7);
        } else if(clawPosition == ClawPosition.MIDDLE) {
            clawPosition = ClawPosition.LEFT;
            clawServo.setPosition(1);
        }

    }

    void interpretClawPosition() {
        double diff1 = Math.abs(clawServo.getPosition());
        double diff2 = Math.abs(clawServo.getPosition() - 0.7);
        double diff3 = Math.abs(clawServo.getPosition() - 1);

        if(diff1 < diff2 && diff1 < diff3) {
            clawPosition = ClawPosition.RIGHT;
        } else if(diff2 < diff1 && diff2 < diff3) {
            clawPosition = ClawPosition.MIDDLE;
        } else {
            clawPosition = ClawPosition.LEFT;
        }
    }

    public void adjustClaw(float stickPos) {
        clawServo.setPosition(clawServo.getPosition() - (stickPos / 100));
        interpretClawPosition();
    }

    /**
     *
     * @return returns the state of the claw; true for open, false for closed
     */

    public boolean toggleClaw() {
        if(isOpen) {
            closeClaw();
        } else {
            openClaw();
        }

        isOpen = !isOpen;

        return(isOpen);
    }



    public void checkAndToggle(boolean isButtonPressed) {

        if(toggler.checkToggle(isButtonPressed)) {
            toggleClaw();
        }

    }

}
