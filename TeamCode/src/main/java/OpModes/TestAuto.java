package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ColorSensorController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;

public class TestAuto extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor leftColor, rightColor;
    Servo leftClaw, rightClaw;
    int slideDownPos;

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

        slideDownPos = linearSlide.getCurrentPosition();

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        waitForStart();

        ColorSensorController leftColorController = new ColorSensorController(leftColor);
        ColorSensorController rightColorController = new ColorSensorController(rightColor);

        LinearSlideController slideController = new LinearSlideController(linearSlide, slideDownPos);

        DriveController driveController = new DriveController(frontLeft, backLeft, frontRight, backRight);
        driveController.init(true);

        telemetry.addLine("Moving one tile forwards...");
        telemetry.update();

        driveController.forwards(1);

        while(!driveController.waitForMotors()) {
            sleep(10);
        }

        telemetry.addLine("Moving one tile backwards...");
        telemetry.update();

        driveController.backwards(1);

        while(!driveController.waitForMotors()) {
            sleep(10);
        }

        telemetry.addLine("Moving one tile right...");
        telemetry.update();

        driveController.right(1);

        while(!driveController.waitForMotors()) {
            sleep(10);
        }

        telemetry.addLine("Moving one tile left...");
        telemetry.update();

        driveController.left(1);

        while(!driveController.waitForMotors()) {
            sleep(10);
        }

    }

}
