package federates.Statistics;

import hla.rti1516e.ObjectInstanceHandle;

import java.util.Map;


public class Statistics {

    private int[] carOnRoadInQueue= new int[4];
    private int carsThatAreGone;
    private double timeWait;
    private double meanTimeWait;
    private int carsThatWaitInQueue;
    private int carsWasInQueue;

    public int[] getCarOnRoadInQueue() {
        return carOnRoadInQueue;
    }

    public void addCarOnRoadInQueue(int roadId) {
        this.carOnRoadInQueue[roadId]++;
    }

    public void subCarOnRoadInQueue(int roadId) {
        this.carOnRoadInQueue[roadId]--;
    }

    public int getCarsThatAreGone() {
        return carsThatAreGone;
    }

    public void setCarsThatAreGone() {
        this.carsThatAreGone++;
    }

    public void setTimeWait(double timeWait) {
        this.timeWait += timeWait;
        if(this.carsWasInQueue>0) {
            this.meanTimeWait = this.timeWait / this.carsWasInQueue;
        }
    }

    public double getMeanTimeWait() {
        return meanTimeWait;
    }

    public int getCarsThatWaitInQueue() {
        return carsThatWaitInQueue;
    }

    public void addCarsThatWaitInQueue() {
        this.carsThatWaitInQueue++;
        this.carsWasInQueue++;
    }

    public void subCarsThatWaitInQueue() {
        this.carsThatWaitInQueue--;
    }
}
