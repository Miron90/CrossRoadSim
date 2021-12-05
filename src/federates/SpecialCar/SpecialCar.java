package federates.SpecialCar;

import helpers.Config;
import hla.rti1516e.ObjectInstanceHandle;

import java.util.Map;


public class SpecialCar {

    private int id;
    private ObjectInstanceHandle objectId;
    private int roadId;
    private int roadToGoId;

    private int currentx;
    private int currenty;

    private boolean spin;

    public SpecialCar(int id, ObjectInstanceHandle objectId, int roadId) {
        this.id=id;
        this.objectId=objectId;
        this.roadId=roadId;
        roadToGoId = Config.rand.nextInt(4);
        while(roadToGoId==roadId){
            roadToGoId = Config.rand.nextInt(4);
        }
    }
    public SpecialCar(int id, int roadToGoId, int roadId) {
        this.id=id;
        this.roadId=roadId;
        this.roadToGoId=roadToGoId;
        switch(this.roadId){
            case 0:
                currentx=600-15;
                currenty=-60-20;
                break;
            case 1:
                currentx=1260-20;
                currenty=600-15;
                break;
            case 2:
                currentx=600-15;
                currenty=1260-20;
                break;
            case 3:
                currentx=-60-20;
                currenty=600-15;
                break;
        }
    }



    public int getId() {
        return id;
    }

    public ObjectInstanceHandle getObjectId() {
        return objectId;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getRoadToGoId() {
        return roadToGoId;
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


    public boolean isSpin() {
        return spin;
    }

    public void setSpin(boolean spin) {
        this.spin = spin;
    }

}
