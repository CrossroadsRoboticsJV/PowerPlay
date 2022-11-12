package OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import HelperClasses.ColorSensorController;

@Autonomous
public class AutoOpMode extends LinearOpMode {

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

    @Override
    public void runOpMode() throws InterruptedException {

        initiate();

        ColorSensorController leftColorController = new ColorSensorController(leftColor);
        ColorSensorController rightColorController = new ColorSensorController(rightColor);

        // TODO: Position a color sensor by the cone sleeve - use correct sensor below - move to corresponding location

        String color = leftColorController.readDominantColor();

        if(color.equals("red")) {

        } else if(color.equals("green")) {

        } else {

        }

    }

    void move(double leftX, double leftY, double rightX) {

        double r = Math.hypot(leftX, leftY);
        double robotAngle = Math.atan2(-leftY, leftX) - Math.PI / 4;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        frontLeft.setPower(v1 / 3); // slower speeds in auto are more accurate
        frontRight.setPower(v2 / 3);
        backLeft.setPower(v3 / 3 / 2); // to compensate for 2:1 gear ratio on front wheels
        backRight.setPower(v4 / 3 / 2);

    }

}
