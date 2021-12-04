package federates.GUI;

import federates.Car.Car;
import federates.Road.Road;
import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.time.HLAfloat64Time;
import org.portico.impl.hla1516e.types.encoding.HLA1516eBoolean;
import org.portico.impl.hla1516e.types.encoding.HLA1516eInteger32BE;

import java.util.ArrayList;
import java.util.List;

public class GUIFederateAmbassador extends NullFederateAmbassador {

    //----------------------------------------------------------
    //                    STATIC VARIABLES
    //----------------------------------------------------------

    //----------------------------------------------------------
    //                   INSTANCE VARIABLES
    //----------------------------------------------------------
    private GUIFederate federate;

    // these variables are accessible in the package
    protected double federateTime        = 0.0;
    protected double federateLookahead   = 1.0;

    protected boolean isRegulating       = false;
    protected boolean isConstrained      = false;
    protected boolean isAdvancing        = false;

    protected boolean isAnnounced        = false;
    protected boolean isReadyToRun       = false;

    protected boolean isRunning       = true;

    List<ObjectInstanceHandle> roadList = new ArrayList<>();
    List<ObjectInstanceHandle> carList = new ArrayList<>();

    //----------------------------------------------------------
    //                      CONSTRUCTORS
    //----------------------------------------------------------

    public GUIFederateAmbassador(GUIFederate federate )
    {
        this.federate = federate;
    }

    //----------------------------------------------------------
    //                    INSTANCE METHODS
    //----------------------------------------------------------
    private void log( String message )
    {
        System.out.println( "time: "+federateTime+" | FederateAmbassador: " + message );
    }

    //////////////////////////////////////////////////////////////////////////
    ////////////////////////// RTI Callback Methods //////////////////////////
    //////////////////////////////////////////////////////////////////////////
    @Override
    public void synchronizationPointRegistrationFailed( String label,
                                                        SynchronizationPointFailureReason reason )
    {
        log( "Failed to register sync point: " + label + ", reason="+reason );
    }

    @Override
    public void synchronizationPointRegistrationSucceeded( String label )
    {
        log( "Successfully registered sync point: " + label );
    }

    @Override
    public void announceSynchronizationPoint( String label, byte[] tag )
    {
        log( "Synchronization point announced: " + label );
        if( label.equals(GUIFederate.READY_TO_RUN) )
            this.isAnnounced = true;
    }

    @Override
    public void federationSynchronized( String label, FederateHandleSet failed )
    {
        log( "Federation Synchronized: " + label );
        if( label.equals(GUIFederate.READY_TO_RUN) )
            this.isReadyToRun = true;
    }

    /**
     * The RTI has informed us that time regulation is now enabled.
     */
    @Override
    public void timeRegulationEnabled( LogicalTime time )
    {
        this.federateTime = ((HLAfloat64Time)time).getValue();
        this.isRegulating = true;
    }

    @Override
    public void timeConstrainedEnabled( LogicalTime time )
    {
        this.federateTime = ((HLAfloat64Time)time).getValue();
        this.isConstrained = true;
    }

    @Override
    public void timeAdvanceGrant( LogicalTime time )
    {
        this.federateTime = ((HLAfloat64Time)time).getValue();
        this.isAdvancing = false;
    }

    @Override
    public void discoverObjectInstance( ObjectInstanceHandle theObject,
                                        ObjectClassHandle theObjectClass,
                                        String objectName )
            throws FederateInternalError
    {
        log( "Discoverd Object: handle=" + theObject + ", classHandle=" +
                theObjectClass + ", name=" + objectName );
        if(federate.roadHandle.equals(theObjectClass))
            roadList.add(theObject);
        if(federate.carHandle.equals(theObjectClass))
            carList.add(theObject);
    }

    @Override
    public void reflectAttributeValues( ObjectInstanceHandle theObject,
                                        AttributeHandleValueMap theAttributes,
                                        byte[] tag,
                                        OrderType sentOrder,
                                        TransportationTypeHandle transport,
                                        SupplementalReflectInfo reflectInfo )
            throws FederateInternalError
    {
        // just pass it on to the other method for printing purposes
        // passing null as the time will let the other method know it
        // it from us, not from the RTI
        reflectAttributeValues( theObject,
                theAttributes,
                tag,
                sentOrder,
                transport,
                null,
                sentOrder,
                reflectInfo );
    }

    @Override
    public void reflectAttributeValues( ObjectInstanceHandle theObject,
                                        AttributeHandleValueMap theAttributes,
                                        byte[] tag,
                                        OrderType sentOrdering,
                                        TransportationTypeHandle theTransport,
                                        LogicalTime time,
                                        OrderType receivedOrdering,
                                        SupplementalReflectInfo reflectInfo )
            throws FederateInternalError
    {
        StringBuilder builder = new StringBuilder( "Reflection for object:" );

        // print the handle
        builder.append( " handle=" + theObject );
        // print the tag
        builder.append( ", tag=" + new String(tag) );
        // print the time (if we have it) we'll get null if we are just receiving
        // a forwarded call from the other reflect callback above


        // print the attribute information
        builder.append( ", attributeCount=" + theAttributes.size() );
        builder.append( "\n" );
        if(roadList.contains(theObject)){
            HLAinteger32BE roadId = new HLA1516eInteger32BE();
            HLA1516eBoolean light = new HLA1516eBoolean();
            for( AttributeHandle attributeHandle : theAttributes.keySet() ) {
                // print the attibute handle
                builder.append("\tattributeHandle=").append(attributeHandle);
                if (attributeHandle.equals(federate.roadIdHandle)) {
                    builder.append(" (roadId)    ");
                    builder.append(", attributeValue=");
                    try {
                        roadId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(roadId.getValue());

                } else if (attributeHandle.equals(federate.roadLightHandle)) {
                    builder.append(" (light)    ");
                    builder.append(", attributeValue=");
                    try {
                        light.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(light.getValue());
                }
            }
            log("cos tam cos "+roadId.getValue() + "    "+light.getValue());
            log("cos tam cos "+federate.roadList.size());
                if(federate.roadList.size()==4){
                    for (int i = 0; i < federate.roadList.size(); i++) {
                        if(federate.roadList.get(i).getRoadId()==roadId.getValue()){
                            federate.roadList.get(i).setLight(light.getValue());
                            break;
                        }
                    }
                }else {
                    federate.roadList.add(new Road(roadId.getValue(), light.getValue()));
                }
                builder.append("\n");
        }else if(carList.contains(theObject)){
            HLAinteger32BE carId = new HLA1516eInteger32BE();
            HLAinteger32BE roadId = new HLA1516eInteger32BE();
            HLAinteger32BE roadToGoId = new HLA1516eInteger32BE();
            for( AttributeHandle attributeHandle : theAttributes.keySet() ) {
                // print the attibute handle
                builder.append("\tattributeHandle=").append(attributeHandle);
                if (attributeHandle.equals(federate.carIdHandle)) {
                    builder.append(" (carId)    ");
                    builder.append(", attributeValue=");
                    try {
                        carId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(carId.getValue());

                } else if (attributeHandle.equals(federate.carRoadHandle)) {
                    builder.append(" (carRoad)    ");
                    builder.append(", attributeValue=");
                    try {
                        roadId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(roadId.getValue());
                }else if (attributeHandle.equals(federate.carRoadToGoHandle)) {
                    builder.append(" (carRoadToGo)    ");
                    builder.append(", attributeValue=");
                    try {
                        roadToGoId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(roadToGoId.getValue());
                }

            }
            federate.carList.add(new Car(carId.getValue(), roadId.getValue(),roadToGoId.getValue(), federate.carOnRoadList.get(roadId.getValue()), federateTime));
            federate.carOnRoadList.set(roadId.getValue(),federate.carOnRoadList.get(roadId.getValue())+1);
            builder.append("\n");
        }

        log( builder.toString() );
    }



    //----------------------------------------------------------
    //                     STATIC METHODS
    //----------------------------------------------------------
}
