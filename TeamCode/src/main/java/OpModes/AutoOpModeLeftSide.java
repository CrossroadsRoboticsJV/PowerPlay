package OpModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DistanceController;
import HelperClasses.DriveController;
import HelperClasses.IMUController;
import HelperClasses.LinearSlideController;

@Autonomous
public class AutoOpModeLeftSide extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor colorSensor;
    DistanceSensor rightDistanceSensor;
    Servo leftClaw, rightClaw, clawServo;
    int slideDownPos;
    IMUController imuController;

    void initiate() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        linearSlide = hardwareMap.get(DcMotorEx.class, "linearSlide");

        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        rightDistanceSensor = hardwareMap.get(DistanceSensor.class, "rightDistanceSensor");

        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");
        clawServo = hardwareMap.get(Servo.class, "clawServo");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        slideDownPos = linearSlide.getCurrentPosition();

        imuController = new IMUController(hardwareMap.get(BNO055IMU.class, "imu"), telemetry);
        imuController.init();

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();
        waitForStart();

        ColorSensorController colorController = new ColorSensorController(colorSensor);
        ClawController clawController = new ClawController(leftClaw, rightClaw, clawServo);
        LinearSlideController slideController = new LinearSlideController(linearSlide, slideDownPos);
        DriveController driveController = new DriveController(frontLeft, backLeft, frontRight, backRight, imuController);
        DistanceController rightDistanceController = new DistanceController(rightDistanceSensor);
        driveController.init();

        clawServo.setPosition(0.7);

        clawController.toggleClaw();
        sleep(350);

        slideController.goToPos(LinearSlideController.LinearSlidePosition.MID, 0.9);

        driveController.forwards(0.7, 0.4);
        driveController.forwards(0.35, 0.15);

        sleep(250);
        String color = colorController.readDominantColor();
        sleep(250);

        telemetry.addData("Color", color);
        telemetry.update();

        driveController.forwards(0.2, 0.3);
        driveController.right(0.08, 0.2);
        driveController.forwardUntilPoleAndAdjust(rightDistanceController);

        clawController.clawRight();

        sleep(1500);

        slideController.update((float) 0.1, 0);
        sleep(500);
        slideController.update(0, 0);

        clawController.toggleClaw();
        clawController.clawLeft();

        driveController.left(0.08, 0.2);

        sleep(250);

//        slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 1);
////and this really isn't the time for such folly
//        driveController.backwards(0.3, 0.4);
//        driveController.forwards(0.1, 0.3);
//        clawController.toggleClaw();
//        sleep(200);
//        slideController.goToPos(LinearSlideController.LinearSlidePosition.MID, 1);
//        sleep(1000);
//        clawController.clawLeft();
//        sleep(250);
//        clawController.toggleClaw();
//        sleep(250);
//        clawController.clawRight();
//        sleep(250);

        slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 1);
        driveController.backwards(0.4, 0.3);

        driveController.forwards(1.15, 0.4);
        imuController.rotate(-90, 0.4, driveController);
        driveController.left(0.1, 0.2);
        slideController.goToPosInStack(5, 0.8);
        driveController.forwards(0.69, 0.6);
        driveController.forwards(0.2, 0.2);

        clawController.toggleClaw();

        sleep(350);

        slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.7);

        sleep(750);

        driveController.backwards(1, 0.6);
        driveController.backwardUntilPoleAndAdjust(rightDistanceController);
        clawController.clawRight();
        sleep(1250);

        slideController.update((float) 0.1, 0);
        sleep(500);
        slideController.update(0, 0);


        clawController.toggleClaw();

        slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.5);
        sleep(750);
        clawController.clawLeft();


        // park
        clawController.toggleClaw();
        driveController.forwards(0.6, 0.4);

        imuController.rotate(90, 0.5, driveController);
        driveController.backwards(1, 0.6);

//        // Go to correct square
        if(color.equals("red")) {

            driveController.left(1.2, 0.6);

        } else if(color.equals("green")) {

            // already there (in theory)

        } else {

            driveController.right(1.1, 0.5);

        }

        slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 0.8);

        sleep(6000);



    }

}
