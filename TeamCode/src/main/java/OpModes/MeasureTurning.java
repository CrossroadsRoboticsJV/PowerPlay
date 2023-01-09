package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;

import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.DriveController;
import HelperClasses.LinearSlideController;

@TeleOp
public class MeasureTurning extends LinearOpMode {

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
        List<Integer> diffs = new ArrayList<Integer>();
        diffs.add(frontLeft.getCurrentPosition());
        diffs.add(backLeft.getCurrentPosition());
        diffs.add(frontRight.getCurrentPosition());
        diffs.add(backRight.getCurrentPosition());


        while(!isStopRequested()) {

            driveController.drive(0, 0, gamepad1.right_stick_x, gamepad1.right_stick_x);

            telemetry.addData("Front Left Position:", frontLeft.getCurrentPosition());
            telemetry.addData("Back Left Position:", backLeft.getCurrentPosition());
            telemetry.addData("Front Right Position:", frontRight.getCurrentPosition());
            telemetry.addData("Back Right Position:", backRight.getCurrentPosition());

            telemetry.addData("Front Left Diff:", frontLeft.getCurrentPosition() - diffs.get(0));
            telemetry.addData("Back Left Diff:", backLeft.getCurrentPosition() - diffs.get(1));
            telemetry.addData("Front Right Diff:", frontRight.getCurrentPosition() - diffs.get(2));
            telemetry.addData("Back Right Diff:", backRight.getCurrentPosition() - diffs.get(3));

            telemetry.update();



        }
        
    }

}
