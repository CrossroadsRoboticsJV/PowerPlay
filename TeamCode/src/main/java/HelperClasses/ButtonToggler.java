package HelperClasses;

public class ButtonToggler {

    boolean firstPressed = true;

    public boolean checkToggle(boolean isButtonPressed) {

        if(isButtonPressed) {

            if(firstPressed) {

                // Toggle
                firstPressed = false;
                return true;

            }

            firstPressed = false;

        } else {
            firstPressed = true;
        }

        return false;

    }

}
