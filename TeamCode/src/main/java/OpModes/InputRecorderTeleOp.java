package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;

import HelperClasses.Timer;

@TeleOp
public class InputRecorderTeleOp extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight;
    ColorSensor leftColor, rightColor;

    void initiate() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        leftColor = hardwareMap.get(ColorSensor.class, "leftColor");
        rightColor = hardwareMap.get(ColorSensor.class, "rightColor");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void runOpMode() throws InterruptedException {

        initiate();
        boolean xFirstPressed = true;
        boolean isRecording = false;
        float[] currentInputs = new float[]{};
        Timer timer = new Timer();

        ArrayList<float[]> inputs = new ArrayList<>();

        // Inputs in format [leftX, leftY, rightX, time]

        while(!isStopRequested()) {

            for(float[] input : inputs) {

                telemetry.addLine("Input:");
                telemetry.addData("leftStickX", input[0]);
                telemetry.addData("leftStickY", input[1]);
                telemetry.addData("rightStickX", input[2]);
                telemetry.addData("timeMillis", input[3]);
                telemetry.addLine("");

            }

            telemetry.addData("time", timer.getTime());

            telemetry.update();

            if(gamepad1.x && xFirstPressed) {

                xFirstPressed = false;

                if(isRecording) {

                    inputs.add(endRecord(currentInputs, timer));
                    isRecording = false;


                } else {

                    currentInputs = new float[]{gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x};
                    timer.resetTimer();
                    move(currentInputs[0], currentInputs[1], currentInputs[2]);
                    isRecording = true;

                }

            } else if(!gamepad1.x) {
                xFirstPressed = true;
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

    float[] endRecord(float[] currentInputs, Timer timer) {

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        return(new float[] {
                currentInputs[0],
                currentInputs[1],
                currentInputs[2],
                timer.getTime()
        });

    }

}
