package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp
public class MecanumOpMode extends LinearOpMode {

    DcMotorEx frontLeft, frontRight, backLeft, backRight;
    ColorSensor leftColor, rightColor;

    void initiate() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        leftColor = hardwareMap.get(ColorSensor.class, "leftColor");
        rightColor = hardwareMap.get(ColorSensor.class, "rightColor");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        while(!isStopRequested()) {

            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;

            frontLeft.setPower(v1);
            frontRight.setPower(v2);
            backLeft.setPower(v3);
            backRight.setPower(v4);

            telemetry.addData("Left Sensor Red", leftColor.red());
            telemetry.addData("Left Sensor Green", leftColor.green());
            telemetry.addData("Left Sensor Blue", leftColor.blue());
            telemetry.addData("Left sensor dominant RGB color", readDominantColor(leftColor));
            telemetry.addData("Right Sensor Red", rightColor.red());
            telemetry.addData("Right Sensor Green", rightColor.green());
            telemetry.addData("Right Sensor Blue", rightColor.blue());
            telemetry.addData("Right sensor dominant RGB color", readDominantColor(rightColor));

            telemetry.update();

        }

    }

    String readDominantColor(ColorSensor sensor) {
        int r = sensor.red();
        int g = sensor.green();
        int b = sensor.blue();
        if(r > g && r > b) {
            return("red");
        } else if(g > r && g > b) {
            return("green");
        } else {
            return("blue");
        }
    }

}
