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

    private boolean afterCrossRoad = false;
    private boolean sendAfterCrossRoad = false;
    private boolean timeIsSet = false;

    private int endx;
    private int endy;
    private int currentx;
    private int currenty;
    private int trafficx;
    private int trafficy;
    private int carMoved=0;
    private int driveTo=0;
    private int[] addedx=new int[10];
    private int[] addedy=new int[10];
    private boolean[] addedsignx= new boolean[10];
    private boolean[] addedsigny=new boolean[10];

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


    public int getCarId() {
        return carId;
    }


    public ObjectInstanceHandle getCarObjectId() {
        return carObjectId;
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


    public boolean[] getAddedsignx() {
        return addedsignx;
    }

    public void setAddedsignx(int addedsignx, boolean sign) {
        this.addedsignx[addedsignx] =sign;
    }

    public boolean[] getAddedsigny() {
        return addedsigny;
    }

    public void setAddedsigny(int addedsigny, boolean sign) {
        this.addedsigny[addedsigny] =sign;
    }

    public int getCurrentx() {
        return currentx;
    }

    public void setCurrentx(int currentx) {
        this.currentx = currentx;
    }

    public int[] getAddedx() {
        return addedx;
    }

    public void addAddedx(int addedx, int index) {
        this.addedx[index]=addedx;
    }

    public int[] getAddedy() {
        return addedy;
    }

    public void addAddedy(int addedy, int index) {
        this.addedy[index]=addedy;
    }

    public int getCurrenty() {
        return currenty;
    }

    public void setCurrenty(int currenty) {
        this.currenty = currenty;
    }

    public int getDriveTo() {
        return driveTo;
    }

    public void setDriveTo(int driveTo) {
        this.driveTo = driveTo;
    }

    public int getTrafficx() {
        return trafficx;
    }


    public int getTrafficy() {
        return trafficy;
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

    public int getCarMoved() {
        return carMoved;
    }

    public void addCarMoved() {
        this.carMoved++;
    }
    public void subCarMoved() {
        this.carMoved--;
    }

    public int getCenterx(){
        if(driveTo==0 || driveTo==2){
            return currentx+20;
        }else{
            return currentx+25;
        }
    }
    public int getCentery(){
        if(driveTo==0 || driveTo==2){
            return currenty+25;
        }else{
            return currenty+20;
        }
    }
}
