package HelperClasses;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSensorController {

    ColorSensor sensor;

    public ColorSensorController(ColorSensor sensor) {
        this.sensor = sensor;
    }

    public String readDominantColor() {
        int r = this.sensor.red();
        int g = this.sensor.green();
        int b = this.sensor.blue();
        if(r > g && r > b) {
            return("red");
        } else if(g > r && g > b) {
            return("green");
        } else {
            return("blue");
        }
    }

}
