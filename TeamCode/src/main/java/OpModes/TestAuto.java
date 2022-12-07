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
public class TestAuto extends LinearOpMode {

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



        telemetry.addLine("Moving one tile forwards...");
        telemetry.update();

        driveController.forwards(1);
        driveController.waitForMotors();



        telemetry.addLine("Moving one tile backwards...");
        telemetry.update();

        driveController.backwards(1);
        driveController.waitForMotors();



        telemetry.addLine("Moving one tile right...");
        telemetry.update();

        driveController.right(1);
        driveController.waitForMotors();



        telemetry.addLine("Moving one tile left...");
        telemetry.update();

        driveController.left(1);
        driveController.waitForMotors();



        telemetry.addLine("Toggling claw (1)...");
        telemetry.update();

        clawController.toggleClaw();
        sleep(500);



        telemetry.addLine("Toggling claw (2)...");
        telemetry.update();

        clawController.toggleClaw();
        sleep(500);



        telemetry.addLine("Toggling claw (3)...");
        telemetry.update();

        clawController.toggleClaw();
        sleep(500);

        telemetry.addLine("Toggling claw (4)...");
        telemetry.update();

        clawController.toggleClaw();
        sleep(500);




    }

}
