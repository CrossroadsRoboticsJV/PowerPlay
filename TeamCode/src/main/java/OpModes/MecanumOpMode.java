package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import HelperClasses.ButtonToggler;
import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DistanceController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;

@TeleOp
public class MecanumOpMode extends LinearOpMode {

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

            if(gamepad1.right_trigger > 0.5) {
                driveController.drive(gamepad1.left_stick_x, gamepad1.left_stick_y + (gamepad2.left_stick_y/5), gamepad1.right_stick_x + (gamepad2.left_stick_x/5), 1);
            } else {
                driveController.drive(gamepad1.left_stick_x, gamepad1.left_stick_y + (gamepad2.left_stick_y/5), gamepad1.right_stick_x + (gamepad2.left_stick_x/5), 0.7);
            }

            slideController.update(gamepad2.left_trigger, gamepad2.right_trigger);

            clawController.checkAndToggle(gamepad2.x);

            if(leftBumper.checkToggle(gamepad2.dpad_left)) {
                clawController.clawLeft();
            }

            if(rightBumper.checkToggle(gamepad2.dpad_right)) {
                clawController.clawRight();
            }

            if(Math.abs(gamepad2.right_stick_x) > 0.1) {
                clawController.adjustClaw(gamepad2.right_stick_x);
            }

//            telemetry.addData("Claw Servo Position", clawServo.getPosition());
//
//            telemetry.addData("Linear Slide Position", linearSlide.getCurrentPosition());
//            telemetry.addData("Back Left Motor Position", backLeft.getCurrentPosition());
//            telemetry.addData("Back Right Motor Position", backRight.getCurrentPosition());
//            telemetry.addData("Front Left Motor Position", frontLeft.getCurrentPosition());
//            telemetry.addData("Front Right Motor Position", frontRight.getCurrentPosition());
//
//            telemetry.addData("Color Sensor Red", colorController.red());
//            telemetry.addData("Color Sensor Green", colorController.green());
//            telemetry.addData("Color Sensor Blue", colorController.blue());
//
//            telemetry.addData("Right Distance Sensor IN", rightDistanceSensor.getDistance(DistanceUnit.INCH));

//            telemetry.update();

        }

    }

}
