package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DistanceController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;

@Autonomous
public class AutoOpModeLeftSide extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor colorSensor;
    DistanceSensor leftDistanceSensor, rightDistanceSensor;
    Servo leftClaw, rightClaw, clawServo;
    int slideDownPos;

    void initiate() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");

        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        leftDistanceSensor = hardwareMap.get(DistanceSensor.class, "leftDistanceSensor");
        rightDistanceSensor = hardwareMap.get(DistanceSensor.class, "rightDistanceSensor");

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
        DistanceController leftDistanceController = new DistanceController(leftDistanceSensor);
        DistanceController rightDistanceController = new DistanceController(rightDistanceSensor);
        driveController.init();

        clawServo.setPosition(0.57);

        clawController.toggleClaw();
        sleep(500);

        slideController.goToPos(LinearSlideController.LinearSlidePosition.MID, 0.6);

        driveController.forwards(0.7, 0.3);
        driveController.forwards(0.2, 0.15);

        sleep(250);
        String color = colorController.readDominantColor();
        sleep(250);

        telemetry.addData("Color", color);
        telemetry.update();

        driveController.forwards(0.6, 0.2);
        driveController.backwards(0.3, 0.2);

        // Put cone on high pole
        slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.7);

        driveController.right(1.1, 0.35);

        float dist = driveController.forwardUntilPoleAndAdjust(rightDistanceController);
        clawController.clawRight();

        sleep(1000);

        slideController.update((float) 0.1, 0);
        sleep(500);
        slideController.update(0, 0);

        clawController.toggleClaw();

        sleep(500);

        slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.2);
        clawController.clawLeft();

        driveController.left(0.1, 0.2);

        driveController.backwards(0.4, 0.2);

//        driveController.forwards(1 - dist, 0.2);
//        driveController.turnLeft(90, 0.2);
//        driveController.forwards(1.7, 0.35);
//
//        clawController.toggleClaw();
//
//        slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.5);
//        driveController.backwards(0.7, 0.35);
//        dist = driveController.backwardUntilPoleAndAdjust(rightDistanceController, true);
//        clawController.clawRight();
//        sleep(500);
//        clawController.toggleClaw();
//        sleep(500);
//        clawController.clawRight();
//
//        driveController.left(0.3, 0.2);
//        driveController.forwards(dist, 0.2);
//
//        // Go to correct square
        if(color.equals("red")) {

            driveController.left(2.1, 0.35);
            driveController.forwards(0.2, 0.2);

        } else if(color.equals("green")) {

            driveController.left(1, 0.35);
            driveController.forwards(0.5, 0.3);
            driveController.backwards(0.3, 0.3);

        } else {

            driveController.forwards(0.2, 0.2);

        }

        slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 0.5);

        sleep(6000);

    }

}
