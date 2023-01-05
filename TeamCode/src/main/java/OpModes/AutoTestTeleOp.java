package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;


@TeleOp
public class AutoTestTeleOp extends LinearOpMode {

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


            if(gamepad1.y) {
                slideController.goToPos(LinearSlideController.LinearSlidePosition.HIGH, 0.4);
            } else if(gamepad1.b) {
                slideController.goToPos(LinearSlideController.LinearSlidePosition.MID, 0.4);
            } else if(gamepad1.x) {
                slideController.goToPos(LinearSlideController.LinearSlidePosition.LOW, 0.4);
            } else if(gamepad1.a) {
                slideController.goToPos(LinearSlideController.LinearSlidePosition.DOWN, 0.4);
            }



            if(gamepad1.left_bumper) {
                driveController.left(1, 0.5);
            } else if(gamepad1.right_bumper) {
                driveController.right(1, 0.5);
            } else if(gamepad1.right_trigger > 0.5) {
                driveController.forwards(1, 0.5);
            } else if(gamepad1.left_trigger > 0.5) {
                driveController.backwards(1, 0.5);
            }

            telemetry.addData("Linear Slide Position", linearSlide.getCurrentPosition());
            telemetry.addData("Linear Slide Target Position", linearSlide.getTargetPosition());
            telemetry.addData("Linear Slide Down Position", slideController.motorDownPosition);

            telemetry.update();

        }

    }

}
