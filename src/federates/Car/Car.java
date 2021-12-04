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
    private boolean sendOnTraffic = false;
    private boolean isInQueue = false;

    private boolean afterTraffic = false;
    private boolean sendAfterTraffic = false;

    private boolean afterCrossRoad = false;
    private boolean sendAfterCrossRoad = false;
    private boolean timeIsSet = false;

    private int endx;
    private int endy;
    private int currentx;
    private int currenty;
    private int trafficx;
    private int trafficy;
    private int beforeNextCarx;
    private int beforeNextCary;

    private int carOnThisRoad;
    private double waitTime;
    private double startWaiting;

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

    public Car(int carId, int roadId, int roadToGo, int carOnThisRoad,double federateTime) {
        this.carId = carId;
        this.roadId = roadId;
        this.roadToGo = roadToGo;
        this.carOnThisRoad = carOnThisRoad;
        switch(this.roadId){
            case 0:
                currentx=535;
                currenty=-60;
                trafficx=currentx;
                trafficy=currenty+520;
                break;
            case 1:
                currentx=1260;
                currenty=535;
                trafficx=currentx-580;
                trafficy=currenty;
                break;
            case 2:
                currentx=610;
                currenty=1260;
                trafficx=currentx;
                trafficy=currenty-580;
                break;
            case 3:
                currentx=-60;
                currenty=610;
                trafficx=currentx+520;
                trafficy=currenty;
                break;

        }
        switch(roadToGo){
            case 0:
                endx = 535;
                endy = 1200;
                break;
            case 1:
                endx = 610;
                endy=1200;
                break;
            case 2:
                endx = 610;
                endy=1200;
                break;
            case 3:
                endx = 610;
                endy=1200;
                break;
        }
    }

    public boolean isOnTraffic() {
        return onTraffic;
    }

    public boolean isInQueue() {
        return isInQueue;
    }

    public boolean isSendAfterCrossRoad() {
        return sendAfterCrossRoad;
    }

    public void setSendAfterCrossRoad(boolean sendAfterCrossRoad) {
        this.sendAfterCrossRoad = sendAfterCrossRoad;
    }

    public void setInQueue(boolean inQueue) {
        isInQueue = inQueue;
    }

    public int getCarOnThisRoad() {
        return carOnThisRoad;
    }

    public void setCarOnThisRoad(int carOnThisRoad) {
        this.carOnThisRoad = carOnThisRoad;
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

    public int getTrafficx() {
        return trafficx;
    }

    public void setTrafficx(int trafficx) {
        this.trafficx = trafficx;
    }

    public int getTrafficy() {
        return trafficy;
    }

    public void setTrafficy(int trafficy) {
        this.trafficy = trafficy;
    }

    public boolean isAfterTraffic() {
        return afterTraffic;
    }

    public void setAfterTraffic(boolean afterTraffic) {
        this.afterTraffic = afterTraffic;
    }

    public boolean isAfterCrossRoad() {
        return afterCrossRoad;
    }

    public void setAfterCrossRoad(boolean afterCrossRoad) {
        this.afterCrossRoad = afterCrossRoad;
    }

    public void setOnTraffic(boolean onTraffic) {
        this.onTraffic = onTraffic;
    }

    public boolean isSendOnTraffic() {
        return sendOnTraffic;
    }

    public void setSendOnTraffic(boolean sendOnTraffic) {
        this.sendOnTraffic = sendOnTraffic;
    }

    public boolean isSendAfterTraffic() {
        return sendAfterTraffic;
    }

    public void setSendAfterTraffic(boolean sendAfterTraffic) {
        this.sendAfterTraffic = sendAfterTraffic;
    }

    public int getBeforeNextCarx() {
        return beforeNextCarx;
    }

    public void setBeforeNextCarx(int beforeNextCarx) {
        this.beforeNextCarx = beforeNextCarx;
    }

    public int getBeforeNextCary() {
        return beforeNextCary;
    }

    public void setBeforeNextCary(int beforeNextCary) {
        this.beforeNextCary = beforeNextCary;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        if(timeIsSet){
            this.waitTime=waitTime-startWaiting;
        }else{
            this.startWaiting=waitTime;
            timeIsSet=true;
        }
    }
}
