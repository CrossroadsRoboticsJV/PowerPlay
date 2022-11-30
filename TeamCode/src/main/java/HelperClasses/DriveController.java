package HelperClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveController {

    DcMotorEx frontLeft, backLeft, frontRight, backRight;
    int tilesToPos = 1050;

    public DriveController(DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
    }

    public void init(boolean encoderMode) {
        if(encoderMode) {
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    /**
     *
     * @return returns false if any motor is busy, returns true otherwise.
     */

    public boolean waitForMotors() {
        if(!frontLeft.isBusy() && !backLeft.isBusy() && !frontRight.isBusy() && !backRight.isBusy()) {
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            return true;
        }
        return false;
    }

    /**
     *
     * @param leftX the x position of the left stick.
     * @param leftY the y position of the left stick.
     * @param rightX the x position of the right stick.
     * @param speedFactor scales the speed of the robot. Should be a double from 0 to 1.
     */

    public void drive(double leftX, double leftY, double rightX, double speedFactor) {

        speedFactor = Math.min(speedFactor, 1);
        speedFactor = Math.max(speedFactor, 0);

        double r = Math.hypot(leftX, leftY);
        double robotAngle = Math.atan2(-leftY, leftX) - Math.PI / 4;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        frontLeft.setPower(v1 * speedFactor);
        frontRight.setPower(v2 * speedFactor);
        backLeft.setPower(v3 * speedFactor / 2); // to compensate for 2:1 gear ratio on front wheels
        backRight.setPower(v4 * speedFactor / 2);
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     * @implNote will only work if the drive controller was initialized with encoder mode as true.
     */

    public void forwards(double tiles) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(0.6);
        frontRight.setPower(0.6);
        backLeft.setPower(0.3);
        backRight.setPower(0.3);
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     * @implNote will only work if the drive controller was initialized with encoder mode as true.
     */

    public void backwards(double tiles) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(0.6);
        frontRight.setPower(0.6);
        backLeft.setPower(0.3);
        backRight.setPower(0.3);
    }

    public void left(double tiles) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(0.6);
        frontRight.setPower(0.6);
        backLeft.setPower(0.3);
        backRight.setPower(0.3);
    }

    public void right(double tiles) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(0.6);
        frontRight.setPower(0.6);
        backLeft.setPower(0.3);
        backRight.setPower(0.3);
    }

}
