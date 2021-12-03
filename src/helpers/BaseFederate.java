package helpers;

import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.time.HLAfloat64Interval;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

public abstract class BaseFederate {

    protected String who="NoBody ";
    protected RTIambassador rtiamb;

    protected void log(String message)
    {
        System.out.println( who + message );
    }

    protected void waitForUser()
    {
        log( " >>>>>>>>>> Press Enter to Continue <<<<<<<<<<" );
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
        try
        {
            reader.readLine();
        }
        catch( Exception e )
        {
            log( "Error while waiting for user input: " + e.getMessage() );
            e.printStackTrace();
        }
    }

    protected byte[] generateTag()
    {
        return ("(timestamp) "+System.currentTimeMillis()).getBytes();
    }


    protected double randomTime() {
        Random r = new Random();
        return 1 + (9 * r.nextDouble());
    }

}
