package federates.GUI;

import hla.rti1516e.ObjectInstanceHandle;

import java.util.Map;

import static helpers.Config.TIME_OF_SERVINF_ONE_PRODUCT;

public class GUI {
    private int cashId;
    private ObjectInstanceHandle id;
    private int queueId;
    private int served=0;
    private boolean open=true;
    private double timeOfServing=-1;
    private double timeToEnd=-1;
    private int servingCustomerId;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public double getTimeOfServing() {
        return timeOfServing;
    }

    public void setTimeOfServing(double federateTime, Map<String, Integer> products) {
        this.timeOfServing=0.;
        for (int quantity: products.values()){
            this.timeOfServing+=quantity*TIME_OF_SERVINF_ONE_PRODUCT;
        }
        timeToEnd=federateTime+timeOfServing;
    }

    public double getTimeToEnd() {
        return timeToEnd;
    }

    public GUI(int id, ObjectInstanceHandle cashObjectHandle) {
        this.cashId=id;
        this.id=cashObjectHandle;
    }

    public int getCashId() {
        return cashId;
    }


    public int getServed() {
        return served;
    }

    public void setServed(int served) {
        this.served += served;
    }

    public ObjectInstanceHandle getId() {
        return id;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public void setTimeOfServing(double i) {
        this.timeOfServing=i;
    }

    public int getServedCustomerId() {
        return servingCustomerId;
    }

    public void setServedCustomerId(int firstCustomerId) {
        this.servingCustomerId=firstCustomerId;
    }
}
