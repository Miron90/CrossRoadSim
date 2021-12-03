package federates.Car;

import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.time.HLAfloat64Time;
import org.portico.impl.hla1516e.types.encoding.HLA1516eFloat32BE;
import org.portico.impl.hla1516e.types.encoding.HLA1516eInteger32BE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    List<ObjectInstanceHandle> queueIdList = new ArrayList<>();
    List<ObjectInstanceHandle> customerIdList = new ArrayList<>();

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
        //if( label.equals(WarehouseFederate.READY_TO_RUN) )
            this.isAnnounced = true;
    }

    @Override
    public void federationSynchronized( String label, FederateHandleSet failed )
    {
        log( "Federation Synchronized: " + label );
        //if( label.equals(WarehouseFederate.READY_TO_RUN) )
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
        if(federate.queueHandle.equals(theObjectClass))
            queueIdList.add(theObject);
        else if(federate.customerHandle.equals(theObjectClass))
            customerIdList.add(theObject);
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
        if(queueIdList.contains(theObject)){
            HLAinteger32BE queueId = new HLA1516eInteger32BE();
            HLAinteger32BE queueLength = new HLA1516eInteger32BE();
            HLAinteger32BE firstCutomerId = new HLA1516eInteger32BE();
            for( AttributeHandle attributeHandle : theAttributes.keySet() ) {
                // print the attibute handle
                builder.append("\tattributeHandle=").append(attributeHandle);

                if (attributeHandle.equals(federate.queueIdHandle)) {
                    builder.append(" (QueueId)    ");
                    builder.append(", attributeValue=");
                    try {
                        queueId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(queueId.getValue());
                } else if (attributeHandle.equals(federate.queueLengthHandle)) {
                    builder.append(" (QueueLength)    ");
                    builder.append(", attributeValue=");
                    try {
                        queueLength.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(queueLength.getValue());
                } else if (attributeHandle.equals(federate.firstCustomerIdHandle)) {
                    builder.append(" (FirstCustomerId)    ");
                    builder.append(", attributeValue=");
                    try {
                        firstCutomerId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(firstCutomerId.getValue());
                }



                builder.append("\n");
            }

        }else if(customerIdList.contains(theObject)){
            HLA1516eInteger32BE customerId=new HLA1516eInteger32BE();
            HLA1516eInteger32BE sausagesQuantity=new HLA1516eInteger32BE();
            HLA1516eInteger32BE cheeseQuantity=new HLA1516eInteger32BE();
            HLA1516eInteger32BE beefQuantity=new HLA1516eInteger32BE();
            HLAfloat32BE queueEnterTime=new HLA1516eFloat32BE();
            HLAfloat32BE queueLeftTime=new HLA1516eFloat32BE();
            for( AttributeHandle attributeHandle : theAttributes.keySet() ) {
                // print the attibute handle
                builder.append("\tattributeHandle=").append(attributeHandle);

                if (attributeHandle.equals(federate.customerIdHandle)) {
                    builder.append(" (CustomerId)    ");
                    builder.append(", attributeValue=");
                    try {
                        customerId.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(customerId.getValue());
                } else if (attributeHandle.equals(federate.sausagesHandle)) {
                    builder.append(" (SausagesQuantity)    ");
                    builder.append(", attributeValue=");
                    try {
                        sausagesQuantity.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(sausagesQuantity.getValue());
                } else if (attributeHandle.equals(federate.cheesehandle)) {
                    builder.append(" (CheeseQuantity)    ");
                    builder.append(", attributeValue=");
                    try {
                        cheeseQuantity.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(cheeseQuantity.getValue());
                } else if (attributeHandle.equals(federate.beefHandle)) {
                    builder.append(" (BeefQuantity)    ");
                    builder.append(", attributeValue=");
                    try {
                        beefQuantity.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(beefQuantity.getValue());
                } else if (attributeHandle.equals(federate.queueEnterTimeHandle)) {
                    builder.append(" (QueueEnterTime)    ");
                    builder.append(", attributeValue=");
                    try {
                        queueEnterTime.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(queueEnterTime.getValue());
                } else if (attributeHandle.equals(federate.queueLeftTimeHandle)) {
                    builder.append(" (QueueLeftTime)    ");
                    builder.append(", attributeValue=");
                    try {
                        queueLeftTime.decode(theAttributes.get(attributeHandle));
                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                    builder.append(queueLeftTime.getValue());
                }
                builder.append("\n");
            }
            Map<String,Integer> products = new HashMap<>();
            products.put("sausages",sausagesQuantity.getValue());
            products.put("cheese",cheeseQuantity.getValue());
            products.put("beef",beefQuantity.getValue());
            boolean exists=false;
            int id=-1;

        }

        log( builder.toString() );
    }



    //----------------------------------------------------------
    //                     STATIC METHODS
    //----------------------------------------------------------
}
