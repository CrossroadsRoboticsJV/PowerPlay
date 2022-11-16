package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

import HelperClasses.ButtonToggler;
import HelperClasses.ClawController;
import HelperClasses.ColorSensorController;
import HelperClasses.LinearSlideController;
import HelperClasses.Timer;

@TeleOp
public class InputRecorderTeleOp extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight, linearSlide;
    ColorSensor leftColor, rightColor;
    Servo leftClaw, rightClaw;
    int linearSlideDownPos;

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

        linearSlideDownPos = linearSlide.getCurrentPosition();

    }

    public void runOpMode() throws InterruptedException {

        initiate();
        boolean xFirstPressed = true;
        boolean isRecording = false;
        float[] currentInputs = new float[]{};
        Timer timer = new Timer();

        ArrayList<float[]> inputs = new ArrayList<>();
        // Inputs in format [leftX, leftY, rightX, leftTrigger, rightTrigger, time]

        ButtonToggler toggler = new ButtonToggler();

        ColorSensorController leftColorController = new ColorSensorController(leftColor);
        ColorSensorController rightColorController = new ColorSensorController(rightColor);

        ClawController clawController = new ClawController(leftClaw, rightClaw);

        LinearSlideController slideController = new LinearSlideController(linearSlide, linearSlideDownPos);

        while(!isStopRequested()) {

            for(float[] input : inputs) {

                telemetry.addLine("Input:");
                telemetry.addData("leftStickX", input[0]);
                telemetry.addData("leftStickY", input[1]);
                telemetry.addData("rightStickX", input[2]);
                telemetry.addData("leftTrigger", input[3]);
                telemetry.addData("rightTrigger", input[4]);
                telemetry.addData("timeMillis", input[5]);
                telemetry.addLine("");

            }

            telemetry.addData("time", timer.getTime());
            telemetry.addData("Left Sensor Red", leftColor.red());
            telemetry.addData("Left Sensor Green", leftColor.green());
            telemetry.addData("Left Sensor Blue", leftColor.blue());
            telemetry.addData("Left sensor dominant RGB color", leftColorController.readDominantColor());
            telemetry.addData("Right Sensor Red", rightColor.red());
            telemetry.addData("Right Sensor Green", rightColor.green());
            telemetry.addData("Right Sensor Blue", rightColor.blue());
            telemetry.addData("Right sensor dominant RGB color", rightColorController.readDominantColor());

            telemetry.addData("Linear Slide Position", linearSlide.getCurrentPosition());

            telemetry.update();



            if(toggler.checkToggle(gamepad1.x)) {

                if (isRecording) {

                    inputs.add(endRecord(currentInputs, timer, slideController));
                    isRecording = false;


                } else {

                    currentInputs = new float[]{gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_trigger, gamepad1.right_trigger};
                    timer.resetTimer();
                    move(currentInputs[0], currentInputs[1], currentInputs[2]);
                    slideController.update(currentInputs[3], currentInputs[4]);
                    isRecording = true;

                }

            }

        }

    }

    void move(double leftX, double leftY, double rightX) {

        double r = Math.hypot(leftX, leftY);
        double robotAngle = Math.atan2(-leftY, leftX) - Math.PI / 4;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        frontLeft.setPower(v1 / 1.5); // slower speeds in auto are more accurate
        frontRight.setPower(v2 / 1.5);
        backLeft.setPower(v3 / 1.5 / 2); // to compensate for 2:1 gear ratio on front wheels
        backRight.setPower(v4 / 1.5 / 2);

    }

    float[] endRecord(float[] currentInputs, Timer timer, LinearSlideController slideController) {

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        slideController.update(0, 0);

        return(new float[] {
                currentInputs[0],
                currentInputs[1],
                currentInputs[2],
                currentInputs[3],
                currentInputs[4],
                timer.getTime()
        });

    }

}
