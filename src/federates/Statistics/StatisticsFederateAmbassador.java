package federates.Statistics;

import federates.Car.Car;
import federates.Road.Road;
import hla.rti1516e.*;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.time.HLAfloat64Time;
import org.portico.impl.hla1516e.types.encoding.HLA1516eBoolean;
import org.portico.impl.hla1516e.types.encoding.HLA1516eFloat32BE;
import org.portico.impl.hla1516e.types.encoding.HLA1516eInteger32BE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFederateAmbassador extends NullFederateAmbassador {

    //----------------------------------------------------------
    //                    STATIC VARIABLES
    //----------------------------------------------------------

    //----------------------------------------------------------
    //                   INSTANCE VARIABLES
    //----------------------------------------------------------
    private StatisticsFederate federate;

    // these variables are accessible in the package
    protected double federateTime        = 0.0;
    protected double federateLookahead   = 1.0;

    protected boolean isRegulating       = false;
    protected boolean isConstrained      = false;
    protected boolean isAdvancing        = false;

    protected boolean isAnnounced        = false;
    protected boolean isReadyToRun       = false;

    protected boolean isRunning       = true;

    List<ObjectInstanceHandle> carList = new ArrayList<>();


    //----------------------------------------------------------
    //                      CONSTRUCTORS
    //----------------------------------------------------------

    public StatisticsFederateAmbassador(StatisticsFederate federate )
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

        if(carList.contains(theObject)){
            federate.howManyCars++;
            }
            builder.append("\n");

        log( builder.toString() );
    }

    @Override
    public void receiveInteraction( InteractionClassHandle interactionClass,
                                    ParameterHandleValueMap theParameters,
                                    byte[] tag,
                                    OrderType sentOrdering,
                                    TransportationTypeHandle theTransport,
                                    FederateAmbassador.SupplementalReceiveInfo receiveInfo )
            throws FederateInternalError
    {
        // just pass it on to the other method for printing purposes
        // passing null as the time will let the other method know it
        // it from us, not from the RTI
        this.receiveInteraction( interactionClass,
                theParameters,
                tag,
                sentOrdering,
                theTransport,
                null,
                sentOrdering,
                receiveInfo );
    }

    @Override
    public void receiveInteraction( InteractionClassHandle interactionClass,
                                    ParameterHandleValueMap theParameters,
                                    byte[] tag,
                                    OrderType sentOrdering,
                                    TransportationTypeHandle theTransport,
                                    LogicalTime time,
                                    OrderType receivedOrdering,
                                    FederateAmbassador.SupplementalReceiveInfo receiveInfo )
            throws FederateInternalError
    {
        StringBuilder builder = new StringBuilder( "Interaction Received:" );

        // print the handle
        builder.append( " handle=" + interactionClass );

        if(federate.EndCarWaitingOnTrafficHandle.equals(interactionClass)){
            {
                byte[] carId = theParameters.get(federate.carIdHandle);
                byte[] carWait = theParameters.get(federate.carWaitTimeHandle);
                byte[] roadId = theParameters.get(federate.roadIdHandle);
                HLAinteger32BE carIdInt = new HLA1516eInteger32BE();
                HLAfloat32BE carWaitDouble = new HLA1516eFloat32BE();
                HLAinteger32BE roadIdInt = new HLA1516eInteger32BE();
                try {
                    carIdInt.decode(carId);
                    carWaitDouble.decode(carWait);
                    roadIdInt.decode(roadId);
                } catch (DecoderException e) {
                    e.printStackTrace();
                }
                federate.statistics.subCarOnRoadInQueue(roadIdInt.getValue());
                federate.statistics.subCarsThatWaitInQueue();
                federate.statistics.setTimeWait(carWaitDouble.getValue());

            }
        }else if (federate.carWaitingOnTrafficHandle.equals(interactionClass)){
            byte[] carId = theParameters.get(federate.carWaitIdHandle);
            byte[] roadId = theParameters.get(federate.waitRoadIdHandle);
            HLAinteger32BE carIdInt = new HLA1516eInteger32BE();
            HLAinteger32BE roadIdInt = new HLA1516eInteger32BE();
            try {
                carIdInt.decode(carId);
                roadIdInt.decode(roadId);
            } catch (DecoderException e) {
                e.printStackTrace();
            }
            federate.statistics.addCarOnRoadInQueue(roadIdInt.getValue());
            federate.statistics.addCarsThatWaitInQueue();
        }else if(federate.carIsGoneHandle.equals(interactionClass)){
            federate.statistics.setCarsThatAreGone();
        }
        log( builder.toString() );
    }
    //----------------------------------------------------------
    //                     STATIC METHODS
    //----------------------------------------------------------
}
