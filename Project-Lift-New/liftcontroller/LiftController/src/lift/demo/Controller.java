package lift.demo;

/* geimporteerde packages */
import lift.lib.Factory;
import lift.lib.inf.iLift;
import lift.lib.inf.iLiftKooi;

/**
 *
 * @author JSC
 * @version 0.1
 * 
 */



public class Controller
{
    /* Objecten om met de kooi en lift te communiceren */ 
	static iLiftKooi kooi;
    static iLift lift;
    
    public static void leds(int verdieping)
    {
	    
	    
    	System.out.println(verdieping);	
    	//als de lift op verdieping 0 is laat 0 zien op het display
    	if (verdieping == 1){
    		kooi.setLeds(63);
    	}
    	//als de lift op verdieping 1 is laat 1 zien op het display
    	else if (verdieping == 2){
    		kooi.setLeds(6);
    	}
    	//als de lift op verdieping 2 is laat 2 zien op het display
    	else if (verdieping == 4){
    		kooi.setLeds(91);
    	}
    	//anders word er een E weergegeven van error
    	else{
    		kooi.setLeds(121);
    	}
    }
    
    //Alle functie defenities
    public static int aanvraag(){
    	boolean liftknopbg = false, liftknop1 = false, liftknop2 = false;
    	int antwoord;
    	int x = kooi.getSwitchValues();
    	
    	
    	
        if (x == 1){
        	antwoord = 0;
        	liftknopbg = true;
        }
        else if (x == 2){
        	antwoord = 1;
        	liftknop1 = true;
        }
        else if(x == 4){
        	antwoord = 2;
        	liftknop2 = true;
        }
        else{
        	antwoord = -1;
        }

        	return antwoord;
    }

    public static int liftknopbg(int antwoord)
        if (antwoord == 1){
        	liftknopbg = true;
        }
        return liftknopbg;
    }
    
    public static int liftknop1(int antwoord){
    	if (antwoord == 1){
        	boolean liftknop1 = true;
        	}
    	return liftknop1;
    }
    public static int liftknop2(int antwoord){
    	if (antwoord == 1){
            boolean liftknop2 = true;
           }
    	return liftknop2;
    	}
    /**
     * static void main() is de methode waar alle java programma's mee
     * opstarten.
     */
    public static void main( String[] args )
    {
        /* Communicatie objecten aanmaken */
        kooi = Factory.getLiftKooi("sim");
        lift = Factory.getLift();
        
        while(true){
        int verdieping = lift.getCagePosition();
        leds(verdieping); 
        int  aanvraag = aanvraag();
//        System.out.println(aanvraag);
       
       if(aanvraag == 2){
    	   lift.setMotorSpeed(5);
       }

    	sleep (100);
        /* 100 mSec wachten */
    }
   }
    
  
 
   
    
    /**
     * De methode zorgt dat het huidige proces stopt gedurende de tijd
     * die wordt opgegeven
     * 
     * @param msec Aantal millisec  
     */
    private static void sleep( int msec )
    {
        try
        {
            java.lang.Thread.sleep( msec );
        }
        catch (InterruptedException ex)
        {
            // Doe niets
        }
    }

}
