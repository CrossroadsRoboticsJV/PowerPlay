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

//@Autonomous
public class AutoOpModeRightSide extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor colorSensor;
    Servo leftClaw, rightClaw, clawServo;
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
        clawServo = hardwareMap.get(Servo.class, "clawServo");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        slideDownPos = linearSlide.getCurrentPosition();

    }

    @Override
    public void runOpMode() throws InterruptedException {


        initiate();
        waitForStart();
        ColorSensorController colorController = new ColorSensorController(colorSensor);
        ClawController clawController = new ClawController(leftClaw, rightClaw, clawServo);
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

        driveController.forwards(0.6, 0.4);
        driveController.backwards(0.3, 0.5);

        // Put cone on high pole
        slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.7);

        driveController.left(1.55, 0.7);

        sleep(1000);

        slideController.update((float) 0.1, 0);
        sleep(500);
        slideController.update(0, 0);

        clawController.toggleClaw();

        sleep(200);

        driveController.right(0.5, 0.7);

        // Go to correct square
        if(color.equals("red")) {

            // Already there!

        } else if(color.equals("green")) {

            driveController.right(1, 0.7);

        } else {

            driveController.right(2.1, 0.7);

        }

        slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 0.5);
        sleep(6000);

    }

}
