package federates.GUI;


import federates.Car.Car;
import federates.Road.Road;
import federates.SpecialCar.SpecialCar;
import helpers.BaseFederate;
import hla.rti1516e.*;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.*;
import hla.rti1516e.time.HLAfloat64Interval;
import hla.rti1516e.time.HLAfloat64Time;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static helpers.Config.*;

public class GUIFederate extends BaseFederate{
//----------------------------------------------------------
    //                    STATIC VARIABLES
    //----------------------------------------------------------
    /** The number of times we will update our attributes and send an interaction */
    public static final int ITERATIONS = 20;

    /** The sync point all federates will sync up on before starting */
    public static final String READY_TO_RUN = "ReadyToRun";

    //----------------------------------------------------------
    //                   INSTANCE VARIABLES
    //----------------------------------------------------------
    private RTIambassador rtiamb;
    private GUIFederateAmbassador fedamb;
    private HLAfloat64TimeFactory timeFactory;
    protected EncoderFactory encoderFactory;

    protected ObjectClassHandle roadHandle;
    protected AttributeHandle roadIdHandle;
    protected AttributeHandle roadLightHandle;

    protected ObjectClassHandle carHandle;
    protected AttributeHandle carIdHandle;
    protected AttributeHandle carRoadHandle;
    protected AttributeHandle carRoadToGoHandle;

    protected ObjectClassHandle specialCarHandle;
    protected AttributeHandle specialCarIdHandle;
    protected AttributeHandle specialCarRoadHandle;
    protected AttributeHandle specialCarRoadToGoHandle;

    protected InteractionClassHandle carEndWaitInTrafficHandle;
    protected InteractionClassHandle carWaitInTrafficHandle;

    protected InteractionClassHandle carIsGoneHandle;

    ArrayList<Road> roadList = new ArrayList<>();
    ArrayList<Car> carList = new ArrayList<>();
    ArrayList<SpecialCar> specialCarList = new ArrayList<>();
    ArrayList<Integer> carOnRoadList = new ArrayList<>();



    ///////////////////////////////////////////////////////////////////////////
    ////////////////////////// Main Simulation Method /////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    /**
     * This is the main simulation loop. It can be thought of as the main method of
     * the federate. For a description of the basic flow of this federate, see the
     * class level comments
     */
    public void runFederate( String federateName ) throws Exception
    {
        who = "GUI";
        /////////////////////////////////////////////////
        // 1 & 2. create the RTIambassador and Connect //
        /////////////////////////////////////////////////
        log( "Creating RTIambassador" );
        rtiamb = RtiFactoryFactory.getRtiFactory().getRtiAmbassador();
        encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();

        // connect
        log( "Connecting..." );
        fedamb = new GUIFederateAmbassador( this );
        rtiamb.connect( fedamb, CallbackModel.HLA_EVOKED );

        //////////////////////////////
        // 3. create the federation //
        //////////////////////////////
        log( "Creating Federation..." );
        // We attempt to create a new federation with the first three of the
        // restaurant FOM modules covering processes, food and drink
        try
        {
            URL[] modules = new URL[]{
                    (new File("foms/CrossRoad.xml")).toURI().toURL(),
            };

            rtiamb.createFederationExecution( "CrossRoadFederation", modules );
            log( "Created Federation" );
        }
        catch( FederationExecutionAlreadyExists exists )
        {
            log( "Didn't create federation, it already existed" );
        }
        catch( MalformedURLException urle )
        {
            log( "Exception loading one of the FOM modules from disk: " + urle.getMessage() );
            urle.printStackTrace();
            return;
        }

        ////////////////////////////
        // 4. join the federation //
        ////////////////////////////
        rtiamb.joinFederationExecution( federateName,            // name for the federate
                "GUI",   // federate type
                "CrossRoadFederation"     // name of federation
        );           // modules we want to add

        log( "Joined Federation as " + federateName );
        AWT Gui = new AWT(this);
        // cache the time factory for easy access
        this.timeFactory = (HLAfloat64TimeFactory)rtiamb.getTimeFactory();

        ////////////////////////////////
        // 5. announce the sync point //
        ////////////////////////////////
        // announce a sync point to get everyone on the same page. if the point
        // has already been registered, we'll get a callback saying it failed,
        // but we don't care about that, as long as someone registered it
        rtiamb.registerFederationSynchronizationPoint( READY_TO_RUN, null );
        // wait until the point is announced
        while( fedamb.isAnnounced == false )
        {
            rtiamb.evokeMultipleCallbacks( 0.1, 0.2 );
        }

        // WAIT FOR USER TO KICK US OFF
        // So that there is time to add other federates, we will wait until the
        // user hits enter before proceeding. That was, you have time to start
        // other federates.
        waitForUser();

        ///////////////////////////////////////////////////////
        // 6. achieve the point and wait for synchronization //
        ///////////////////////////////////////////////////////
        // tell the RTI we are ready to move past the sync point and then wait
        // until the federation has synchronized on
        rtiamb.synchronizationPointAchieved( READY_TO_RUN );
        log( "Achieved sync point: " +READY_TO_RUN+ ", waiting for federation..." );
        while( fedamb.isReadyToRun == false )
        {
            rtiamb.evokeMultipleCallbacks( 0.1, 0.2 );
        }

        /////////////////////////////
        // 7. enable time policies //
        /////////////////////////////
        // in this section we enable/disable all time policies
        // note that this step is optional!
        enableTimePolicy();
        log( "Time Policy Enabled" );

        //////////////////////////////
        // 8. publish and subscribe //
        //////////////////////////////
        // in this section we tell the RTI of all the data we are going to
        // produce, and all the data we want to know about
        publishAndSubscribe();
        log( "Published and Subscribed" );

        for (int i=0; i<4;i++){
            carOnRoadList.add(0);
        }
        while( fedamb.federateTime<SIM_TIME )
        {
            for (int i=0; i<roadList.size();i++){
                if(roadList.get(i).isLight()) {
                    Gui.changeLight(i+1);
                    roadList.get(i).setLight(false);
                    log( "light changed" );
                }
            }

            for (int i=0; i<carList.size();i++){
                if(carList.get(i).isInQueue() && !carList.get(i).isSendOnTraffic()) {
                    sendInteractionWaitOnTraffic(carList.get(i));
                    carList.get(i).setSendOnTraffic(true);
                    log( "send wait interaction" );
                }
                if(!carList.get(i).isInQueue() && carList.get(i).isSendOnTraffic()) {
                    sendInteractionEndWaitOnTraffic(carList.get(i));
                    carList.get(i).setSendOnTraffic(false);
                    log( "send end wait interaction" );
                }
                if(carList.get(i).isAfterCrossRoad() && !carList.get(i).isSendAfterCrossRoad()) {
                    sendInteractionCarIsGone(carList.get(i));
                    carList.get(i).setSendAfterCrossRoad(true);
                    log( "send car is gone interaction" );
                }
            }
            Gui.drawCar();
            Gui.myFrame.myCanvas.repaint();
            double minTime=1;
            advanceTime(minTime);
            log( "Time Advanced to " + fedamb.federateTime );
        }

        //////////////////////////////////////
        // 11. delete the object we created //
        //////////////////////////////////////
//		deleteObject( objectHandle );
//		log( "Deleted Object, handle=" + objectHandle );

        ////////////////////////////////////
        // 12. resign from the federation //
        ////////////////////////////////////
        rtiamb.resignFederationExecution( ResignAction.DELETE_OBJECTS );
        log( "Resigned from Federation" );

        ////////////////////////////////////////
        // 13. try and destroy the federation //
        ////////////////////////////////////////
        // NOTE: we won't die if we can't do this because other federates
        //       remain. in that case we'll leave it for them to clean up
        try
        {
            rtiamb.destroyFederationExecution( "ExampleFederation" );
            log( "Destroyed Federation" );
        }
        catch( FederationExecutionDoesNotExist dne )
        {
            log( "No need to destroy federation, it doesn't exist" );
        }
        catch( FederatesCurrentlyJoined fcj )
        {
            log( "Didn't destroy federation, federates still joined" );
        }
    }

    private void sendInteractionEndWaitOnTraffic(Car car) throws NotConnected, FederateNotExecutionMember, NameNotFound, RTIinternalError, InvalidInteractionClassHandle, InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, RestoreInProgress, SaveInProgress {
        ParameterHandleValueMap parameters = rtiamb.getParameterHandleValueMapFactory().create(3);

        ParameterHandle carWaitHandle = rtiamb.getParameterHandle(this.carEndWaitInTrafficHandle, "carId");
        HLAinteger32BE carId = encoderFactory.createHLAinteger32BE(car.getCarId());

        ParameterHandle carWaitTimeHandle = rtiamb.getParameterHandle(this.carEndWaitInTrafficHandle, "carWaitTime");
        HLAfloat32BE carWaitTime = encoderFactory.createHLAfloat32BE((float) car.getWaitTime());

        ParameterHandle roadIdHandle = rtiamb.getParameterHandle(this.carEndWaitInTrafficHandle, "carRoadId");
        HLAinteger32BE roadId = encoderFactory.createHLAinteger32BE( car.getRoadId());

        parameters.put(carWaitHandle, carId.toByteArray());
        parameters.put(carWaitTimeHandle, carWaitTime.toByteArray());
        parameters.put(roadIdHandle, roadId.toByteArray());

        rtiamb.sendInteraction( this.carEndWaitInTrafficHandle, parameters, generateTag() );
    }

    private void sendInteractionWaitOnTraffic(Car car) throws NotConnected, FederateNotExecutionMember, NameNotFound, RTIinternalError, InvalidInteractionClassHandle, InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, RestoreInProgress, SaveInProgress {
        ParameterHandleValueMap parameters = rtiamb.getParameterHandleValueMapFactory().create(2);

        ParameterHandle carWaitHandle = rtiamb.getParameterHandle(this.carWaitInTrafficHandle, "carId");
        HLAinteger32BE carId = encoderFactory.createHLAinteger32BE(car.getCarId());

        ParameterHandle roadIdHandle = rtiamb.getParameterHandle(this.carWaitInTrafficHandle, "carRoadId");
        HLAinteger32BE roadId = encoderFactory.createHLAinteger32BE( car.getRoadId());

        parameters.put(carWaitHandle, carId.toByteArray());
        parameters.put(roadIdHandle, roadId.toByteArray());

        rtiamb.sendInteraction( this.carWaitInTrafficHandle, parameters, generateTag() );
    }

    private void sendInteractionCarIsGone(Car car) throws NotConnected, FederateNotExecutionMember, NameNotFound, RTIinternalError, InvalidInteractionClassHandle, InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, RestoreInProgress, SaveInProgress {
        ParameterHandleValueMap parameters = rtiamb.getParameterHandleValueMapFactory().create(1);

        ParameterHandle carIsGoneHandle = rtiamb.getParameterHandle(this.carIsGoneHandle, "carId");
        HLAinteger32BE carId = encoderFactory.createHLAinteger32BE(car.getCarId());

        parameters.put(carIsGoneHandle, carId.toByteArray());

        rtiamb.sendInteraction( this.carIsGoneHandle, parameters, generateTag() );
    }


    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Helper Methods //////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method will attempt to enable the various time related properties for
     * the federate
     */
    protected void log(String message)
    {
        if(fedamb!=null) System.out.println( "time: "+fedamb.federateTime+" | "+who + message );
        else System.out.println( who + message );
    }

    public double getTime()
    {
        return fedamb.federateTime;
    }

    private void enableTimePolicy() throws Exception
    {
        // NOTE: Unfortunately, the LogicalTime/LogicalTimeInterval create code is
        //       Portico specific. You will have to alter this if you move to a
        //       different RTI implementation. As such, we've isolated it into a
        //       method so that any change only needs to happen in a couple of spots
        HLAfloat64Interval lookahead = timeFactory.makeInterval( fedamb.federateLookahead );

        ////////////////////////////
        // enable time regulation //
        ////////////////////////////
        this.rtiamb.enableTimeRegulation( lookahead );

        // tick until we get the callback
        while( fedamb.isRegulating == false )
        {
            rtiamb.evokeMultipleCallbacks( 0.1, 0.2 );
        }

        /////////////////////////////
        // enable time constrained //
        /////////////////////////////
        this.rtiamb.enableTimeConstrained();

        // tick until we get the callback
        while( fedamb.isConstrained == false )
        {
            rtiamb.evokeMultipleCallbacks( 0.1, 0.2 );
        }
    }

    /**
     * This method will inform the RTI about the types of data that the federate will
     * be creating, and the types of data we are interested in hearing about as other
     * federates produce it.
     */
    private void publishAndSubscribe() throws RTIexception
    {
////		publish ProductsStrorage object
        this.roadHandle = rtiamb.getObjectClassHandle("HLAobjectRoot.Road");
        this.roadIdHandle = rtiamb.getAttributeHandle(this.roadHandle,"roadId");
        this.roadLightHandle = rtiamb.getAttributeHandle(this.roadHandle,"light");

        AttributeHandleSet roadAttributes = rtiamb.getAttributeHandleSetFactory().create();
        roadAttributes.add( this.roadIdHandle );
        roadAttributes.add( this.roadLightHandle );

        rtiamb.subscribeObjectClassAttributes( this.roadHandle, roadAttributes );

        this.carHandle = rtiamb.getObjectClassHandle("HLAobjectRoot.Car");
        this.carIdHandle = rtiamb.getAttributeHandle(this.carHandle,"carId");
        this.carRoadHandle = rtiamb.getAttributeHandle(this.carHandle,"roadId");
        this.carRoadToGoHandle = rtiamb.getAttributeHandle(this.carHandle,"roadToGo");

        AttributeHandleSet carAttributes = rtiamb.getAttributeHandleSetFactory().create();
        carAttributes.add( this.carIdHandle );
        carAttributes.add( this.carRoadHandle );
        carAttributes.add( this.carRoadToGoHandle );

        rtiamb.subscribeObjectClassAttributes( this.carHandle, carAttributes );

        this.specialCarHandle = rtiamb.getObjectClassHandle("HLAobjectRoot.SpecialCar");
        this.specialCarIdHandle = rtiamb.getAttributeHandle(this.specialCarHandle,"carId");
        this.specialCarRoadHandle = rtiamb.getAttributeHandle(this.specialCarHandle,"roadId");
        this.specialCarRoadToGoHandle = rtiamb.getAttributeHandle(this.specialCarHandle,"roadToGo");

        AttributeHandleSet specialCarAttributes = rtiamb.getAttributeHandleSetFactory().create();
        specialCarAttributes.add( this.specialCarIdHandle );
        specialCarAttributes.add( this.specialCarRoadHandle );
        specialCarAttributes.add( this.specialCarRoadToGoHandle );

        rtiamb.subscribeObjectClassAttributes( this.specialCarHandle, specialCarAttributes );

        this.carEndWaitInTrafficHandle = rtiamb.getInteractionClassHandle("HLAinteractionRoot.EndCarWaitingOnTraffic" );
        rtiamb.publishInteractionClass(this.carEndWaitInTrafficHandle);

        this.carWaitInTrafficHandle = rtiamb.getInteractionClassHandle("HLAinteractionRoot.carWaitingOnTraffic" );
        rtiamb.publishInteractionClass(this.carWaitInTrafficHandle);

        this.carIsGoneHandle = rtiamb.getInteractionClassHandle("HLAinteractionRoot.CarHasGone" );
        rtiamb.publishInteractionClass(this.carIsGoneHandle);

    }

    /**
     * This method will request a time advance to the current time, plus the given
     * timestep. It will then wait until a notification of the time advance grant
     * has been received.
     */
    private void advanceTime( double timestep ) throws RTIexception
    {
        // request the advance
        fedamb.isAdvancing = true;
        HLAfloat64Time time = timeFactory.makeTime( fedamb.federateTime + timestep );
        rtiamb.timeAdvanceRequest( time );

        // wait for the time advance to be granted. ticking will tell the
        // LRC to start delivering callbacks to the federate
        while( fedamb.isAdvancing )
        {
            rtiamb.evokeMultipleCallbacks( 0.1, 0.2 );

        }
    }

    private short getTimeAsShort()
    {
        return (short)fedamb.federateTime;
    }


    //----------------------------------------------------------
    //                     STATIC METHODS
    //----------------------------------------------------------
    public static void main( String[] args )
    {
        // get a federate name, use "exampleFederate" as default
        String federateName = "GUI";
        if( args.length != 0 )
        {
            federateName = args[0];
        }

        try
        {
            // run the example federate
            new GUIFederate().runFederate( federateName );
        }
        catch( Exception rtie )
        {
            // an exception occurred, just log the information and exit
            rtie.printStackTrace();
        }
    }

}
