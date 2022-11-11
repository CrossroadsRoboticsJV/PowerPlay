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

}
