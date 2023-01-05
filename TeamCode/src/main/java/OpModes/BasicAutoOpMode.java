package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;

@Autonomous
public class BasicAutoOpMode extends LinearOpMode {

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
        ClawController clawController = new ClawController(leftClaw, rightClaw);
        LinearSlideController slideController = new LinearSlideController(linearSlide, slideDownPos);
        DriveController driveController = new DriveController(frontLeft, backLeft, frontRight, backRight);
        driveController.init();

        clawController.toggleClaw();
        sleep(500);

        slideController.goToPos(LinearSlideController.LinearSlidePosition.MID, 0.6);

        driveController.forwards(0.7, 0.6);
        driveController.forwards(0.2, 0.3);

        sleep(250);
        String color = colorController.readDominantColor();
        sleep(250);

        telemetry.addData("Color", color);
        telemetry.update();

        driveController.forwards(0.3, 0.3);

        if(color.equals("red")) {

            driveController.left(1.1, 0.7);

        } else if(color.equals("green")) {

            driveController.forwards(0.8, 0.5);
            driveController.backwards(0.5, 0.5);

        } else {

            driveController.right(1.1, 0.6);

        }

        slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 0.3);
        sleep(5000);

    }

}
