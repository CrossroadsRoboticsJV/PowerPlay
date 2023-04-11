package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ButtonToggler;
import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DistanceController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;

@TeleOp
public class ClawTestTeleOp extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor colorSensor;
    DistanceSensor rightDistanceSensor;
    Servo leftClaw, rightClaw, clawServo;
    int linearSlideDownPos;

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

        linearSlideDownPos = linearSlide.getCurrentPosition();



    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        ColorSensorController colorController = new ColorSensorController(colorSensor);

        ButtonToggler leftBumper = new ButtonToggler();
        ButtonToggler rightBumper = new ButtonToggler();

        ClawController clawController = new ClawController(leftClaw, rightClaw, clawServo);

        LinearSlideController slideController = new LinearSlideController(linearSlide, linearSlideDownPos);

        DistanceController rightDistanceController = new DistanceController(rightDistanceSensor);

        DriveController driveController = new DriveController(frontLeft, backLeft, frontRight, backRight);
        driveController.init();

        while(!isStopRequested()) {

            slideController.update(gamepad1.left_trigger, gamepad1.right_trigger);

            clawController.checkAndToggle(gamepad1.x);

            if(leftBumper.checkToggle(gamepad1.dpad_left)) {
                clawController.clawLeft();
            }

            if(rightBumper.checkToggle(gamepad1.dpad_right)) {
                clawController.clawRight();
            }

            if(Math.abs(gamepad1.right_stick_x) > 0.1) {
                clawController.adjustClaw(gamepad1.right_stick_x);
            }

            telemetry.addData("Claw Servo Position", clawServo.getPosition());

            telemetry.addData("Left Claw Servo Position", leftClaw.getPosition());
            telemetry.addData("Right Claw Servo Position", rightClaw.getPosition());

            telemetry.update();

        }

    }

}
