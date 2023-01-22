package HelperClasses;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSensorController {

    ColorSensor sensor;

    public ColorSensorController(ColorSensor sensor) {
        this.sensor = sensor;
    }

    public int red() {
        return sensor.red();
    }

    public int green() {
        return sensor.green();
    }

    public int blue() {
        return sensor.blue();
    }

    public String readDominantColor() {
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
