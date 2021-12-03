package federates.Road;

import hla.rti1516e.ObjectInstanceHandle;

import java.awt.*;

public class Road {

    private ObjectInstanceHandle roadObjectId;
    private int roadId;
    private boolean light;
    private Color colorOfLight;

    public Road(ObjectInstanceHandle roadObjectHandle, int roadId, boolean light) {
        this.roadObjectId=roadObjectHandle;
        this.roadId = roadId;
        if(light) this.colorOfLight = Color.GREEN; else this.colorOfLight = Color.RED;
        this.light = false;
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
