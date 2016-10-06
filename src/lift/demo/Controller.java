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

    /**
     * static void main() is de methode waar alle java programma's mee
     * opstarten.
     * 
     **/
    /* Objecten om met de kooi en lift te communiceren */ 
	static iLiftKooi kooi;
    static iLift lift;
	
	public static void main( String[] args )
    {
        /* Communicatie objecten aanmaken */
        kooi = Factory.getLiftKooi("sim");
        lift = Factory.getLift();
        while (true){
        	sleep(50);
        	HuidigeLocatie();
        	kooiAanvraag();
        	liftAanvraag();
        	motorAansturing();
        	lift.setMotorSpeed(motorspeed);
        	lift.setMotorDirection(motorRichting);
        }
    }
	
	// Variabelen voor motoraansturing
	static int motorspeed = 0;
	static boolean motorRichting = true;
	
	// Wanneer liftvraagX == 0 is, geen actieve liftaanvraag. Wanneer liftvraag == 1 is, wel actieve liftaanvraag.
	static boolean liftvraag0 = false; 
	static boolean liftvraag1 = false;
	static boolean liftvraag2 = false;
	
	// Variabele voor de huidige locatie
	static int huidigeLocatie = 1;
	
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
			liftvraag0 = true;
		}
		else if (raw_aanvraag == 2){
			liftvraag1 = true;
		}
		else if (raw_aanvraag == 4){
			liftvraag2 = true;
		}
		
	}
	
	//Functie om de actieve kooiaanvraag te bepalen
	public static void liftAanvraag(){
		int raw_aanvraag = lift.getSwitches();
		
		if(raw_aanvraag == 1){
			liftvraag0 = true;
			liftvraag1 = false;
			liftvraag2 = false;
		}
		else if (raw_aanvraag == 2){
			liftvraag0 = false;
			liftvraag1 = true;
			liftvraag2 = false;
		}
		else if(raw_aanvraag == 3){
			liftvraag0 = true;
			liftvraag1 = true;
			liftvraag2 = false;
		}
		else if (raw_aanvraag == 4){
			liftvraag0 = false;
			liftvraag1 = false;
			liftvraag2 = true;
		}
		else if (raw_aanvraag == 5){
			liftvraag0 = true;
			liftvraag1 = false;
			liftvraag2 = true;
			
		}
		else if (raw_aanvraag == 6){
			liftvraag0 = false;
			liftvraag1 = true;
			liftvraag2 = true;
		}
		else if (raw_aanvraag == 7){
			liftvraag0 = true;
			liftvraag1 = true;
			liftvraag2 = true;
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
		if(huidigeLocatie == 1){
			if (liftvraag0 == true){
				liftvraag0 = false;
				motorspeed = 0;
				motorRichting = true;
			}
			else if (liftvraag1 == true || liftvraag2 == true){
				motorspeed = 2;
			}
		}
		if (huidigeLocatie == 2){
			motorspeed = 2;
		}
		if (huidigeLocatie == 3){
			motorspeed = 4;
		}
		if (huidigeLocatie == 4){
			if((liftvraag2 == true && liftvraag1 == false) || (liftvraag0 == true && motorspeed == 8)){
				motorspeed = 6;
			}
			else if (liftvraag0 == false && motorspeed != 6){
				motorspeed = 2;
			}
		}
		if (huidigeLocatie == 5){
			if (motorRichting == true){
				if (liftvraag1 == true && motorspeed != 6){
					motorspeed = 0;
					liftvraag1 = false;
				}
				if(liftvraag2 == true && motorspeed == 6){
					motorspeed = 8;
				}
				if (liftvraag2 == true && motorspeed == 0){
					motorspeed = 2;
				}
				if (motorspeed == 0 && liftvraag2 == false){
					motorRichting = false;
				}
			}
			if (motorRichting == false){
				if (liftvraag1 == true && motorspeed == 6){
					motorspeed = 8;
				}
				if (liftvraag1 == true && motorspeed == 2){
					motorspeed = 0;
					liftvraag1 = false;
				}
				if (liftvraag0 == true){
					motorspeed = 2;
				}
				if (motorspeed == 0 && liftvraag0 == false){
					motorRichting = true;
				}
				
			}
		}
		if (huidigeLocatie == 6){
			if (motorRichting == true){
				if(motorspeed == 8){
					motorspeed = 6;
				}
				else if (motorspeed != 6){
					motorspeed = 2;
				}
			}
			if(motorRichting == false){
				if(liftvraag0 == true && liftvraag1 == false){
					motorspeed = 6;
				}
				else if (liftvraag1 == true){
					motorspeed = 2;
				}
			}
		}
		if (huidigeLocatie == 7){
			motorspeed = 4;
		}
		if (huidigeLocatie == 8){
			motorspeed = 2;
		}
		if (huidigeLocatie == 9){
			if (liftvraag2 == true){
				motorspeed = 0;
				liftvraag2 = false;
				motorRichting = false;
			}
			if (motorRichting == false && (liftvraag1 == true || liftvraag0 == true))
			{
				motorspeed = 2;
			}
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
