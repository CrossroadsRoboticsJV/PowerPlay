package HelperClasses;

import static java.lang.Thread.sleep;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DriveController {

    DcMotorEx frontLeft, backLeft, frontRight, backRight;
    IMUController imuController;
    int tilesToPos = 1050;
    double degreesToPos = 10.9;

    // ~ 55000 position (half for back wheels) for 7 rotations, giving ~ 7857 position for 360 and ~21.825 per 1 degree

    public DriveController(DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight, IMUController imu) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;

        this.imuController = imu;
    }

    public DriveController(DcMotorEx frontLeft, DcMotorEx backLeft, DcMotorEx frontRight, DcMotorEx backRight) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;

        this.imuController = null;
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
        backLeft.setPower(v3 * speedFactor);
        backRight.setPower(v4 * speedFactor);
    }

    public void turn(double leftPower, double rightPower) {
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setRunWithoutEncoders();
        frontLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backLeft.setPower(leftPower);
        backRight.setPower(rightPower);
    }

    public void turnRight(double degrees, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) Math.round(degrees * degreesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - (int) Math.round(degrees * degreesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + (int) Math.round(degrees * degreesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() - (int) Math.round(degrees * degreesToPos));
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(-power);
        setRunToPosition();
        waitForMotors();
    }

    public void turnLeft(double degrees, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - (int) Math.round(degrees * degreesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + (int) Math.round(degrees * degreesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - (int) Math.round(degrees * degreesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) Math.round(degrees * degreesToPos));
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     * @implNote will only work if the drive controller was initialized with encoder mode as true.
     */

    public void forwards(double tiles, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     */

    public void backwards(double tiles, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos ));
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        backRight.setTargetPosition(backRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     */

    public void left(double tiles, double power) {
        frontLeft.setTargetPosition((int) Math.round(frontLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos) * 1.15));
        frontRight.setTargetPosition((int) Math.round(frontRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos) * 1.15));
        backLeft.setTargetPosition((int) Math.round(backLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos) * 1.15));
        backRight.setTargetPosition((int) Math.round(backRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos) * 1.15));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
        setRunToPosition();
        waitForMotors();
    }

    /**
     *
     * @param tiles distance in floor tiles to move.
     *
     */

    public void right(double tiles, double power) {
        frontLeft.setTargetPosition((int) Math.round(frontLeft.getCurrentPosition() + (int) Math.round(tiles * tilesToPos) * 1.15));
        frontRight.setTargetPosition((int) Math.round(frontRight.getCurrentPosition() - (int) Math.round(tiles * tilesToPos) * 1.15));
        backLeft.setTargetPosition((int) Math.round(backLeft.getCurrentPosition() - (int) Math.round(tiles * tilesToPos) * 1.15));
        backRight.setTargetPosition((int) Math.round(backRight.getCurrentPosition() + (int) Math.round(tiles * tilesToPos) * 1.15));
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
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

    void setRunWithoutEncoders() {
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public float forwardUntilPoleAndAdjust(DistanceController sensor) {
        setRunWithoutEncoders();
        drive(0, -0.3, 0, 1);
        while(!(sensor.getDistance(DistanceUnit.INCH) < 6)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("Uncaught", e);
            }
        }
        float dist = (float) sensor.getDistance(DistanceUnit.INCH);
        drive(0, 0, 0, 0);

        forwards(0.1, 0.2);

        return dist;
    }

    public float backwardUntilPoleAndAdjust(DistanceController sensor) {
        float startPos = frontLeft.getCurrentPosition();
        setRunWithoutEncoders();
        drive(0, 0.3, 0, 1);
        while(!(sensor.getDistance(DistanceUnit.INCH) < 6)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("Uncaught", e);
            }
        }
        drive(0, 0, 0, 0);

        forwards(0.1, 0.2);

        return((startPos - frontLeft.getCurrentPosition()) / tilesToPos);
    }

}
