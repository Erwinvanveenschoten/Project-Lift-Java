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
	// Variabelen voor motoraansturing
	static int liftSnelheid = 0;
	static boolean motorRichting = true;
	
	
	// Wanneer kooiAanvraagVerdiepingX == 0, geen actieve liftaanvraag. Wanneer kooiAanvraagVerdiepingX == 1, wel actieve liftaanvraag.
	static boolean kooiAanvraagVerdieping0 = false;
	static boolean kooiAanvraagVerdieping1 = false;
	static boolean kooiAanvraagVerdieping2 = false;
	
	// Wanneer liftAanvraagverdiepingX == 0 is, geen actieve liftaanvraag. Wanneer liftAanvraagVerdieping == 1 is, wel actieve liftaanvraag.
	static boolean liftAanvraagVerdieping0 = false; 
	static boolean liftAanvraagVerdieping1 = false;
	static boolean liftAanvraagVerdieping2 = false;
	
	// Variabele voor de huidige locatie
	static int huidigeLocatie = 404;
	
	//Functie om de huidige locatie van de lift op te vragen.
	public static void HuidigeLocatie(){
		
		int RawLocatie = lift.getCagePosition();
		int Nieuw;
		switch(RawLocatie){
		case 0: Nieuw = 0; // Te laag
				break;
		case 1: Nieuw = 1; // Exact verdieping 0
				break;
		case 3: Nieuw = 2; // Net boven verdieping 0
				break;
		case 2: Nieuw = 3; // Tussen verdieping 0 en 1
				break;
		case 6: Nieuw = 4; // Net onder verdieping 1
				break;
		case 7: Nieuw = 5; // exact verdieping 1
				break;
		case 5: Nieuw = 6; // Net boven Verdieping 1
				break;
		case 4: Nieuw = 7; // Tussen verdieping 1 en 2
				break;
		case 12: Nieuw = 8; // Net onder verdieping 2
				break;
		case 13: Nieuw = 9; // exact verdieping 2
				break;
		case 15: Nieuw = 10; // Net boven verdieping 2
				break;
		case 14: Nieuw = 11; // Te hoog
				break;
		default: Nieuw = 404; // Alles buiten de genoemde condities
				break;
		}
				System.out.println(RawLocatie);
				huidigeLocatie = Nieuw;		
	}

	//Functie om de actieve liftaanvragen te bepalen.
	public static void kooiAanvraag(){
		
		int raw_aanvraag = kooi.getSwitchValues();
		
		if(raw_aanvraag == 1){
			kooiAanvraagVerdieping0 = true;
		}
		else if (raw_aanvraag == 2){
			kooiAanvraagVerdieping1 = true;
		}
		else if (raw_aanvraag == 4){
			kooiAanvraagVerdieping2 = true;
		}
		
	}
	
	//Functie om de actieve kooiaanvraag te bepalen
	public static void liftAanvraag(){
		int raw_aanvraag = lift.getSwitches();
		
		if(raw_aanvraag == 1){
			liftAanvraagVerdieping0 = true;
			liftAanvraagVerdieping1 = false;
			liftAanvraagVerdieping2 = false;
		}
		else if (raw_aanvraag == 2){
			liftAanvraagVerdieping0 = false;
			liftAanvraagVerdieping1 = true;
			liftAanvraagVerdieping2 = false;
		}
		else if(raw_aanvraag == 3){
			liftAanvraagVerdieping0 = true;
			liftAanvraagVerdieping1 = true;
			liftAanvraagVerdieping2 = false;
		}
		else if (raw_aanvraag == 4){
			liftAanvraagVerdieping0 = false;
			liftAanvraagVerdieping1 = false;
			liftAanvraagVerdieping2 = true;
		}
		else if (raw_aanvraag == 5){
			liftAanvraagVerdieping0 = true;
			liftAanvraagVerdieping1 = false;
			liftAanvraagVerdieping2 = true;
			
		}
		else if (raw_aanvraag == 6){
			liftAanvraagVerdieping0 = false;
			liftAanvraagVerdieping1 = true;
			liftAanvraagVerdieping2 = true;
		}
		else if (raw_aanvraag == 7){
			liftAanvraagVerdieping0 = true;
			liftAanvraagVerdieping1 = true;
			liftAanvraagVerdieping2 = true;
		}
		
	}
	
	//Functie om de leds aan te sturen.
	public static void ledbesturing(int verdieping){
		    
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

	//Functie om de Motor aan te sturen
	public static void motorAansturing(){
		if(huidigeLocatie <= 1){
			motorRichting = true;
			if(liftAanvraagVerdieping1 == true){
				liftSnelheid = 2;
				if (huidigeLocatie == 5){
					liftSnelheid = 0;
					liftAanvraagVerdieping1 = false;
				}
			}
		}
		if (huidigeLocatie == 5){
			if(motorRichting == true){
				if (liftAanvraagVerdieping2 == true){
					liftSnelheid = 2;
					if (huidigeLocatie == 9){
						liftSnelheid = 0;
						liftAanvraagVerdieping2 = false;
					}
				}
			}
			if (liftAanvraagVerdieping0 == true){
				motorRichting = false;
				liftSnelheid = 2;
				if(huidigeLocatie == 1){
					motorRichting = true;
					liftSnelheid = 0;
					liftAanvraagVerdieping0 = false;
				}
			}
				
		}
		if (huidigeLocatie >= 9){
			motorRichting = false;
			if(liftAanvraagVerdieping0 == true || liftAanvraagVerdieping1 == true){
				liftSnelheid = 2;
				if(huidigeLocatie == 5){
					liftSnelheid = 0;
					liftAanvraagVerdieping1 = false;
				}
			}
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
        while (true){
        	HuidigeLocatie();
        	kooiAanvraag();
        	liftAanvraag();
        	motorAansturing();
        	lift.setMotorSpeed(liftSnelheid);
        	lift.setMotorDirection(motorRichting);
        	sleep(1);
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
