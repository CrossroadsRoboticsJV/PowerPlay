package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;


@TeleOp
public class MecanumOpMode extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor colorSensor;
    Servo leftClaw, rightClaw;
    int linearSlideDownPos;

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

        linearSlideDownPos = linearSlide.getCurrentPosition();



    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        ColorSensorController colorController = new ColorSensorController(colorSensor);

        ClawController clawController = new ClawController(leftClaw, rightClaw);

        LinearSlideController slideController = new LinearSlideController(linearSlide, linearSlideDownPos);

        DriveController driveController = new DriveController(frontLeft, backLeft, frontRight, backRight);
        driveController.init();

        while(!isStopRequested()) {

            driveController.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1);

            slideController.update(gamepad1.left_trigger, gamepad1.right_trigger);

            clawController.checkAndToggle(gamepad1.x);

            telemetry.addData("Linear Slide Position", linearSlide.getCurrentPosition());
            telemetry.addData("Back Left Motor Position", backLeft.getCurrentPosition());
            telemetry.addData("Back Right Motor Position", backRight.getCurrentPosition());
            telemetry.addData("Front Left Motor Position", frontLeft.getCurrentPosition());
            telemetry.addData("Front Right Motor Position", frontRight.getCurrentPosition());

            telemetry.update();

        }

    }

}
