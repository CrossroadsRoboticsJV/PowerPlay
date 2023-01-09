package HelperClasses;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveController {

    DcMotorEx frontLeft, backLeft, frontRight, backRight;
    int tilesToPos = 1050;
    double degreesToPos = 21.825;

    // ~ 55000 position (half for back wheels) for 7 rotations, giving ~ 7857 position for 360 and ~21.825 per 1 degree

    public DriveController(DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
    }

    public void init() {

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setTargetPositionTolerance(25);
        frontLeft.setTargetPositionTolerance(25);
        backRight.setTargetPositionTolerance(25);
        backLeft.setTargetPositionTolerance(25);

    }

    /**
     *
     * @return returns false if any motor is busy, returns true otherwise.
     */

    public void waitForMotors() {
        while(!(!frontLeft.isBusy() && !backLeft.isBusy() && !frontRight.isBusy() && !backRight.isBusy())) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("Uncaught", e);
            }
        }
        stopAndResetEncoders();
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

    public void forwards(double tiles, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos * 2));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos * 2));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power/2);
        backRight.setPower(power/2);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     */

    public void backwards(double tiles, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos * 2) );
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos * 2));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power/2);
        backRight.setPower(power/2);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     */

    public void left(double tiles, double power) {
        frontLeft.setTargetPosition((int) Math.round(frontLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos * 2) * 1.15));
        frontRight.setTargetPosition((int) Math.round(frontRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos * 2) * 1.15));
        backLeft.setTargetPosition((int) Math.round(backLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos) * 1.15));
        backRight.setTargetPosition((int) Math.round(backRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos) * 1.15));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power/2);
        backRight.setPower(power/2);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     */

    public void right(double tiles, double power) {
        frontLeft.setTargetPosition((int) Math.round(frontLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos * 2) * 1.15));
        frontRight.setTargetPosition((int) Math.round(frontRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos * 2) * 1.15));
        backLeft.setTargetPosition((int) Math.round(backLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos) * 1.15));
        backRight.setTargetPosition((int) Math.round(backRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos) * 1.15));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power/2);
        backRight.setPower(power/2);
        setRunToPosition();
        waitForMotors();
    }

    void setRunToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    void stopAndResetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

}
