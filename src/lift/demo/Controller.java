package lift.demo;

import javax.sql.rowset.CachedRowSet;

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
	
	public static int HuidigeVerdieping(){
		
		int RawLocatie = lift.getCagePosition();
		int Nieuw;
		switch(RawLocatie){
		case 0: Nieuw = 0;
				break;
		case 1: Nieuw = 1;
				break;
		case 2: Nieuw = 3;
				break;
		case 3: Nieuw = 2;
				break;
		case 4: Nieuw = 7;
				break;
		case 5: Nieuw = 6;
				break;
		case 6: Nieuw = 4;
				break;
		case 7: Nieuw = 5;
				break;
		case 12: Nieuw = 8;
				break;
		case 13: Nieuw = 9;
				break;
		case 14: Nieuw = 11;
				break;
		case 15: Nieuw = 10;
				break;
		default: Nieuw = 404;
				break;
		}
				return Nieuw;		
	}

	public static int aanvraag(){
		
		int aanvraag;
		int raw_aanvraag = kooi.getSwitchValues();
		
		if(raw_aanvraag == 1){
			aanvraag = 0;
		}
		else if (raw_aanvraag == 2){
			aanvraag = 1;
		}
		else if (raw_aanvraag == 4){
			aanvraag = 2;
		}
		else{
			aanvraag = -10;
		}
		
		return aanvraag;
	}
	
	public static void leds(int verdieping){
		    
		    
			System.out.println(verdieping);	
			//als de lift op verdieping 0 is laat 0 zien op het display
			if ((verdieping > 0) && (verdieping < 4)){
				kooi.setLeds(63);
			}
			//als de lift op verdieping 1 is laat 1 zien op het display
			else if ((verdieping > 3) && (verdieping < 8)){
				kooi.setLeds(6);
			}
			//als de lift op verdieping 2 is laat 2 zien op het display
			else if ((verdieping > 7) && (verdieping < 11)){
				kooi.setLeds(91);
			}
			//anders word er een E weergegeven van error
			else{
				kooi.setLeds(121);
			}
	}

	
	
	
	
	
	
	
    /* Objecten om met de kooi en lift te communiceren */ 
	static iLiftKooi kooi;
    static iLift lift;

    /**
     * static void main() is de methode waar alle java programma's mee
     * opstarten.
     */
    public static void main( String[] args )
    {
        /* Communicatie objecten aanmaken */
        kooi = Factory.getLiftKooi("sim");
        lift = Factory.getLift();
        /* Voeg de eigen code hier toe */
        while(true){
        	
        int verdieping = HuidigeVerdieping();
        System.out.println(verdieping);
        
        leds(verdieping);
        
        lift.setMotorDirection(true);
        int aanvraag = aanvraag();
        if(aanvraag == 2){
        if (verdieping == 9){
        	lift.setMotorSpeed(0);
        	lift.setDoorOpen();
        }
        else{
            lift.setMotorSpeed(2);
        }
//        int Locatie = HuidigeVerdieping(RawLocatie);    
        
        }
        /* 100 mSec wachten */
        sleep( 100 );
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
