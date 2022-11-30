package HelperClasses;

import com.qualcomm.robotcore.hardware.Servo;

public class ClawController {

    Servo leftServo;
    Servo rightServo;
    public boolean isOpen;
    ButtonToggler toggler = new ButtonToggler();

    public ClawController(Servo leftServo, Servo rightServo) {
        this.leftServo = leftServo;
        this.rightServo = rightServo;
    }

    void openClaw() {
        leftServo.setPosition(0.45);
        rightServo.setPosition(0.5);
    }

    void closeClaw() {
        leftServo.setPosition(0.15);
        rightServo.setPosition(0.8);
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
