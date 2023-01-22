package HelperClasses;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DistanceController {

    DistanceSensor sensor;
    double lastDistCM;

    public DistanceController(DistanceSensor sensor) {
        this.sensor = sensor;
    }

    public boolean checkDistanceChange() {
        double dist = sensor.getDistance(DistanceUnit.CM);
        if(Math.abs(dist - lastDistCM) > 100) {
            lastDistCM = dist;
            return true;
        }
        lastDistCM = dist;
        return false;
    }

    public double getDistance(DistanceUnit unit) {
        return sensor.getDistance(unit);
    }

}
