package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import HelperClasses.ColorSensorController;

@Autonomous
public class AutoOpMode extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor leftColor, rightColor;
    Servo leftClaw, rightClaw;

    void initiate() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");

        leftColor = hardwareMap.get(ColorSensor.class, "leftColor");
        rightColor = hardwareMap.get(ColorSensor.class, "rightColor");

        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        waitForStart();

        ColorSensorController leftColorController = new ColorSensorController(leftColor);
        ColorSensorController rightColorController = new ColorSensorController(rightColor);

        // TODO: Position a color sensor by the cone sleeve - use correct sensor below - move to corresponding location

        leftClaw.setPosition(0.15);
        rightClaw.setPosition(0.8);

        sleep(500);

//        leftClaw.setPosition(0.45);
//        rightClaw.setPosition(0.5);
        linearSlide.setPower(0.5);
        sleep(5750);
        linearSlide.setPower(0.1);

        move(0, -1.0, 0);
        sleep(1500);
//        move(0, -1, 0);
//        sleep(1416);

        stopMotors();
        sleep(250);

//        move(0, 0, -1);
//        sleep(327);
//        move(0, -1, 0);
//        sleep(500);
//        stopMotors();
//        sleep(100);
//
        String color = leftColorController.readDominantColor();

        sleep(250);
//
        telemetry.addData("Color", color);
        telemetry.update();

        move(0, -1, 0);
        sleep(350);

        if(color.equals("red")) {

            move(-1, 0, 0.1);
            sleep(3000);
//            move(-1, 0, 0);
//            sleep(1214);
//            move(0, -1, 0);
//            sleep(1893);
            stopMotors();


        } else if(color.equals("green")) {

            stopMotors();

        } else {

            move(1, 0, -0.1);
            sleep(3000);
//            move(0, 0, -1);
//            sleep(1214);
//            move(0, 1, 0);
//            sleep(1893);
            stopMotors();

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

    void stopMotors() {
        frontLeft.setPower(0); // slower speeds in auto are more accurate
        frontRight.setPower(0);
        backLeft.setPower(0); // to compensate for 2:1 gear ratio on front wheels
        backRight.setPower(0);
    }

}
