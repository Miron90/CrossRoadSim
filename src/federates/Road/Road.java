package federates.Road;

import hla.rti1516e.ObjectInstanceHandle;

public class Road {

    private ObjectInstanceHandle roadObjectId;
    private int roadId;
    private boolean light;

    public Road(ObjectInstanceHandle roadObjectHandle, int roadId, boolean light) {
        this.roadObjectId=roadObjectHandle;
        this.roadId = roadId;
        this.light = light;
    }

    public Road( int roadId, boolean light) {
        this.roadId = roadId;
        this.light = light;
    }

    public ObjectInstanceHandle getRoadObjectId() {
        return roadObjectId;
    }

    public void setRoadObjectId(ObjectInstanceHandle roadObjectId) {
        this.roadObjectId = roadObjectId;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }
}
