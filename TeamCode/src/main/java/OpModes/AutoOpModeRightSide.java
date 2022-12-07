package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ColorSensorController;
import HelperClasses.LinearSlideController;

@Autonomous
public class AutoOpModeRightSide extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor colorSensor;
    Servo leftClaw, rightClaw;
    int slideDownPos;

    void initiate() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");

        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        slideDownPos = linearSlide.getCurrentPosition();

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        waitForStart();

        ColorSensorController colorController = new ColorSensorController(colorSensor);

        LinearSlideController slideController = new LinearSlideController(linearSlide, slideDownPos);

        leftClaw.setPosition(0.15);
        rightClaw.setPosition(0.8);

        sleep(500);

        slideController.update((float) 0, (float) 0.65);
        sleep(1400);
        slideController.update(0, 0);

        move(0, -0.4, 0);
        sleep(1000);
        stopMotors(slideController);

        leftClaw.setPosition(0.45);
        rightClaw.setPosition(0.5);
        sleep(500);

        move(1, 0, -0.1);
        sleep(1600);
        stopMotors(slideController);

        move(0, 1, 0);
        sleep(1000);
        stopMotors(slideController);

        slideController.update((float) 0, (float) 0.65);
        sleep(1400);
        slideController.update(0, 0);

        move(0, -1.0, 0);
        sleep(1700);

        stopMotors(slideController);
        sleep(250);
        String color = colorController.readDominantColor();
        sleep(250);

        telemetry.addData("Color", color);
        telemetry.update();

        move(0, -1, 0);
        sleep(550);

        if(color.equals("red")) {

            move(-1, 0, 0.1);
            sleep(3000);
//            move(-1, 0, 0);
//            sleep(1214);
//            move(0, -1, 0);
//            sleep(1893);
            stopMotors(slideController);


        } else if(color.equals("green")) {

            stopMotors(slideController);

        } else {

            move(1, 0, -0.1);
            sleep(3000);
//            move(0, 0, -1);
//            sleep(1214);
//            move(0, 1, 0);
//            sleep(1893);
            stopMotors(slideController);

        }

        linearSlide.setPower(-0.25);
        sleep(8000);
        linearSlide.setPower(0);

    }

    void move(double leftX, double leftY, double rightX) {

        double r = Math.hypot(leftX, leftY);
        double robotAngle = Math.atan2(-leftY, leftX) - Math.PI / 4;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        frontLeft.setPower(v1 / 1.5); // slower speeds in auto are more accurate
        frontRight.setPower(v2 / 1.5);
        backLeft.setPower(v3 / 1.5 / 2); // to compensate for 2:1 gear ratio on front wheels
        backRight.setPower(v4 / 1.5 / 2);

    }

    void stopMotors(LinearSlideController slideController) {
        frontLeft.setPower(0); // slower speeds in auto are more accurate
        frontRight.setPower(0);
        backLeft.setPower(0); // to compensate for 2:1 gear ratio on front wheels
        backRight.setPower(0);
        slideController.update(0, 0);
    }

}
