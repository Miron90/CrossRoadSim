package federates.Car;

import helpers.Config;
import hla.rti1516e.ObjectInstanceHandle;

import java.util.Random;

public class Car {

    private int carId;
    private ObjectInstanceHandle carObjectId;
    private int roadId;
    private int roadToGo;
    private boolean onTraffic = false;

    private int initx;
    private int inity;
    private int endx;
    private int endy;
    private int currentx;
    private int currenty;
    private int trafficx;
    private int trafficy;

    public Car(int carId, ObjectInstanceHandle carObjectId, int roadId) {
        this.carId = carId;
        this.carObjectId = carObjectId;
        this.roadId = roadId;
        roadToGo = Config.rand.nextInt(4);
        while(roadToGo==roadId){
            roadToGo = Config.rand.nextInt(4);
        }


    }

    public int getRoadToGo() {
        return roadToGo;
    }

    public void setRoadToGo(int roadToGo) {
        this.roadToGo = roadToGo;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public ObjectInstanceHandle getCarObjectId() {
        return carObjectId;
    }

    public void setCarObjectId(ObjectInstanceHandle carObjectId) {
        this.carObjectId = carObjectId;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public Car(int carId, int roadId, int roadToGo) {
        this.carId = carId;
        this.roadId = roadId;
        this.roadToGo = roadToGo;
        switch(this.roadId){
            case 0:
                currentx=610;
                currenty=0;
                trafficx=initx;
                trafficy=inity+370;
                break;
            case 1:
                currentx=610;
                currenty=0;
                trafficx=initx;
                trafficy=inity+370;
                break;
            case 2:
                currentx=610;
                currenty=0;
                trafficx=initx;
                trafficy=inity+370;
                break;
            case 3:
                currentx=610;
                currenty=0;
                trafficx=initx;
                trafficy=inity+370;
                break;

        }
        switch(roadToGo){
            case 0:
                endx = 610;
                endy=1000;
                break;
            case 1:
                endx = 610;
                endy=1000;
                break;
            case 2:
                endx = 610;
                endy=1000;
                break;
            case 3:
                endx = 610;
                endy=1000;
                break;
        }
    }

    public int getInitx() {
        return initx;
    }

    public void setInitx(int initx) {
        this.initx = initx;
    }

    public int getInity() {
        return inity;
    }

    public void setInity(int inity) {
        this.inity = inity;
    }

    public int getEndx() {
        return endx;
    }

    public void setEndx(int endx) {
        this.endx = endx;
    }

    public int getEndy() {
        return endy;
    }

    public void setEndy(int endy) {
        this.endy = endy;
    }

    public int getCurrentx() {
        return currentx;
    }

    public void setCurrentx(int currentx) {
        this.currentx = currentx;
    }

    public int getCurrenty() {
        return currenty;
    }

    public void setCurrenty(int currenty) {
        this.currenty = currenty;
    }
}
