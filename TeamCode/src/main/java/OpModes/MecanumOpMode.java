package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import HelperClasses.ColorSensorController;


@TeleOp
public class MecanumOpMode extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor leftColor, rightColor;
    Servo leftClaw, rightClaw;

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

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        ColorSensorController leftColorController = new ColorSensorController(leftColor);
        ColorSensorController rightColorController = new ColorSensorController(rightColor);

        boolean xFirstPressed = true;
        boolean isClawOpen = true;

        while(!isStopRequested()) {

            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;

            frontLeft.setPower(v1);
            frontRight.setPower(v2);
            backLeft.setPower(v3 / 2); // to compensate for 2:1 gear ratio on front wheels
            backRight.setPower(v4 / 2);



            if(gamepad1.right_trigger > 0.2) {

                linearSlide.setPower(-gamepad1.right_trigger/2); // raise slide

            } else if(gamepad1.left_trigger > 0.2) {

                linearSlide.setPower(gamepad1.left_trigger/2);

            } else {

                linearSlide.setPower(0);

            }



            if(gamepad1.x) {

                if(xFirstPressed) {

                    xFirstPressed = false;

                    if(isClawOpen) {

                        isClawOpen = false;
                        leftClaw.setPosition(0.2);
                        rightClaw.setPosition(0.8);

                    } else {

                        isClawOpen = true;
                        leftClaw.setPosition(0.8);
                        rightClaw.setPosition(0.2);

                    }

                }

            } else {

                xFirstPressed = true;

            }



            telemetry.addData("Left Sensor Red", leftColor.red());
            telemetry.addData("Left Sensor Green", leftColor.green());
            telemetry.addData("Left Sensor Blue", leftColor.blue());
            telemetry.addData("Left sensor dominant RGB color", leftColorController.readDominantColor());
            telemetry.addData("Right Sensor Red", rightColor.red());
            telemetry.addData("Right Sensor Green", rightColor.green());
            telemetry.addData("Right Sensor Blue", rightColor.blue());
            telemetry.addData("Right sensor dominant RGB color", rightColorController.readDominantColor());

            telemetry.update();

        }

    }

}
