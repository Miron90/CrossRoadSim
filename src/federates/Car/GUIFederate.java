package federates.Car;


import federates.GUI.AWT;
import helpers.BaseFederate;
import hla.rti1516e.*;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.*;
import hla.rti1516e.time.HLAfloat64Interval;
import hla.rti1516e.time.HLAfloat64Time;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    protected ObjectClassHandle queueHandle;
    protected AttributeHandle queueIdHandle;
    protected AttributeHandle queueLengthHandle;
    protected AttributeHandle firstCustomerIdHandle;

    protected ObjectClassHandle customerHandle;
    protected AttributeHandle customerIdHandle;
    protected AttributeHandle sausagesHandle;
    protected AttributeHandle cheesehandle;
    protected AttributeHandle beefHandle;
    protected AttributeHandle queueEnterTimeHandle;
    protected AttributeHandle queueLeftTimeHandle;

    protected ObjectClassHandle cashHandle;
    protected AttributeHandle cashIdHandle;
    protected AttributeHandle cashQueueIdHandle;
    protected AttributeHandle quantityOfServedHandle;

    protected InteractionClassHandle queueLeftHandle;
    protected InteractionClassHandle customerServedHandle;


    protected List<GUI> GUIList = new ArrayList<>();
    private boolean queuesNotSet = true;


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
                    (new File("foms/Shop.xml")).toURI().toURL(),
            };

            rtiamb.createFederationExecution( "ShopFederation", modules );
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
                "cash",   // federate type
                "ShopFederation"     // name of federation
        );           // modules we want to add

        log( "Joined Federation as " + federateName );

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
        new AWT();
        /////////////////////////////////////
        // 9. register an object to update //
        /////////////////////////////////////
        /////////////////////////////////////
        // 10. do the main simulation loop //
        /////////////////////////////////////
        // here is where we do the meat of our work. in each iteration, we will
        // update the attribute values of the object we registered, and will
        // send an interaction.

        while( fedamb.federateTime<SIM_TIME )
        {



            double minTime=INFINITY;

            if(minTime==INFINITY | minTime>10) minTime=randomTime();
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
        this.cashHandle = rtiamb.getObjectClassHandle("HLAobjectRoot.Cash");
        this.cashIdHandle = rtiamb.getAttributeHandle(this.cashHandle,"cashId");
        this.cashQueueIdHandle = rtiamb.getAttributeHandle(this.cashHandle,"queueId");
        this.quantityOfServedHandle = rtiamb.getAttributeHandle(this.cashHandle,"quantityOfServed");

        AttributeHandleSet cashAttributes = rtiamb.getAttributeHandleSetFactory().create();
        cashAttributes.add( this.cashIdHandle );
        cashAttributes.add( this.cashQueueIdHandle );
        cashAttributes.add( this.quantityOfServedHandle );

        rtiamb.publishObjectClassAttributes( this.cashHandle, cashAttributes );

        this.customerHandle = rtiamb.getObjectClassHandle("HLAobjectRoot.Customer");
        this.customerIdHandle = rtiamb.getAttributeHandle(this.customerHandle,"customerId");
        this.sausagesHandle = rtiamb.getAttributeHandle(this.customerHandle,"sausages");
        this.cheesehandle = rtiamb.getAttributeHandle(this.customerHandle,"cheese");
        this.beefHandle = rtiamb.getAttributeHandle(this.customerHandle,"beef");
        this.queueEnterTimeHandle = rtiamb.getAttributeHandle(this.customerHandle,"queueEnterTime");
        this.queueLeftTimeHandle = rtiamb.getAttributeHandle(this.customerHandle,"queueLeftTime");

        AttributeHandleSet customerAttributes = rtiamb.getAttributeHandleSetFactory().create();
        customerAttributes.add( this.customerIdHandle );
        customerAttributes.add( this.sausagesHandle );
        customerAttributes.add( this.cheesehandle );
        customerAttributes.add( this.beefHandle );
        customerAttributes.add( this.queueEnterTimeHandle );
        customerAttributes.add( this.queueLeftTimeHandle );

        rtiamb.subscribeObjectClassAttributes( this.customerHandle, customerAttributes );

        this.queueHandle = rtiamb.getObjectClassHandle("HLAobjectRoot.Queue");
        this.queueIdHandle = rtiamb.getAttributeHandle(this.queueHandle,"queueId");
        this.queueLengthHandle = rtiamb.getAttributeHandle(this.queueHandle,"queueLength");
        this.firstCustomerIdHandle = rtiamb.getAttributeHandle(this.queueHandle,"firstCustomerId");

        AttributeHandleSet queueAttributes = rtiamb.getAttributeHandleSetFactory().create();
        queueAttributes.add( this.queueIdHandle );
        queueAttributes.add( this.queueLengthHandle );
        queueAttributes.add( this.firstCustomerIdHandle );

        rtiamb.subscribeObjectClassAttributes( this.queueHandle, queueAttributes );


        this.queueLeftHandle = rtiamb.getInteractionClassHandle("HLAinteractionRoot.LeaveTheQueue" );
        rtiamb.publishInteractionClass(this.queueLeftHandle);

        this.customerServedHandle = rtiamb.getInteractionClassHandle("HLAinteractionRoot.CustomerServed" );
        rtiamb.publishInteractionClass(this.customerServedHandle);

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
        String federateName = "cash";
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
