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
	static int motorspeed = 0;
	static boolean motorRichting = true;
	
	// Wanneer liftvraagX == 0 is, geen actieve liftaanvraag. Wanneer liftvraag == 1 is, wel actieve liftaanvraag.
	static boolean liftvraag0 = false; 
	static boolean liftvraag1 = false;
	static boolean liftvraag2 = false;
	
	// Variabele voor de huidige locatie
	static int huidigeLocatie = 1;

    /* Objecten om met de kooi en lift te communiceren */ 
	static iLiftKooi kooi;
    static iLift lift;

    //Main loop waar alle benodigde functies aangeroepen worden in een oneindige lus.
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
        	ledbesturing();
        	lift.setMotorSpeed(motorspeed);
        	lift.setMotorDirection(motorRichting);
        }
    }
	
	//Functie om de huidige locatie van de lift op te vragen.
	public static void HuidigeLocatie(){
		
		int RawLocatie = lift.getCagePosition();
		int Nieuw;
		switch(RawLocatie){
		case 0: Nieuw = 0; // Te laag
				break;
		case 1: Nieuw = 1; // Exact huidigeLocatie0
				break;
		case 3: Nieuw = 2; // Net boven huidigeLocatie0
				break;
		case 2: Nieuw = 3; // Tussen huidigeLocatie0 en 1
				break;
		case 6: Nieuw = 4; // Net onder huidigeLocatie1
				break;
		case 7: Nieuw = 5; // exact huidigeLocatie1
				break;
		case 5: Nieuw = 6; // Net boven huidigeLocatie1
				break;
		case 4: Nieuw = 7; // Tussen huidigeLocatie1 en 2
				break;
		case 12: Nieuw = 8; // Net onder huidigeLocatie2
				break;
		case 13: Nieuw = 9; // exact huidigeLocatie2
				break;
		case 15: Nieuw = 10; // Net boven huidigeLocatie2
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
	public static void ledbesturing(){
		    
			//als de lift op huidigeLocatie0 is laat 0 zien op het display
			if ((huidigeLocatie> 0) && (huidigeLocatie< 4)){
				kooi.setLeds(63);
			}
			
			//als de lift op huidigeLocatie1 is laat 1 zien op het display
			else if ((huidigeLocatie> 3) && (huidigeLocatie< 8)){
				kooi.setLeds(6);
			}
			
			//als de lift op huidigeLocatie2 is laat 2 zien op het display
			else if ((huidigeLocatie> 7) && (huidigeLocatie< 11)){
				kooi.setLeds(91);
			}
			
			//anders word er een E weergegeven van error
			else{
				kooi.setLeds(121);
			}
	}

	//Functie om de Motor aan te sturen
	public static void motorAansturing(){
		if(huidigeLocatie == 1){//als de lift op de beganegrond is word het volgende uitgevoerd:
			if (liftvraag0 == true){//als de liftknp van verdieping ingedrukt is voer het volgende uit:
				liftvraag0 = false;// Het geheugen van de liftknop word uitgezet
				motorspeed = 0;
				lift.setMotorSpeed(motorspeed);
				//zet de liftdeuren open wacht 5 seconde en sluit de liftdeuren daarna weer
				lift.setDoorOpen();
				sleep(5000);
				lift.setDoorClose();
				sleep(5000);				
				motorRichting = true; //zet de richting van de motor weer naar boven
			}
			else if (liftvraag1 == true || liftvraag2 == true){//als de liftknoppen op 1 en 2 zijn inggedrukt ga met 2 snelheid naar boven
				motorspeed = 2;
			}
		}
		if (huidigeLocatie == 2){//motor snelheid blijf 2
			motorspeed = 2;
		}
		if (huidigeLocatie == 3){//versnel de lift met 2 snelheid naar 4
			motorspeed = 4;
		}
		if (huidigeLocatie == 4){// als de lift niet naar de 1e verdieping moet versnel met 2 naar 6
			if((liftvraag2 == true && liftvraag1 == false) || (liftvraag0 == true && motorspeed == 8)){
				motorspeed = 6;
			}
			else if (liftvraag0 == false && motorspeed != 6){//als de lift wel naar de eerste verdieping moet verlaag de snelheid met 2 naar 4
				motorspeed = 2;
			}
		}
		if (huidigeLocatie == 5){// als de lift op de 1e verdieping is voer het volgende uit
			if (motorRichting == true){//als de motor naar boven gaat voer het volgende uit
				if (liftvraag1 == true && motorspeed != 6){//als de lift naar de 1e verdieping moet en niet 6 snelheid heeft voer het volgende uit
					motorspeed = 0;//zet de motor uit
					lift.setMotorSpeed(motorspeed);
					lift.setDoorOpen();//zet de deur open en zet hem na 5 seconden weer dicht
					sleep(5000);
					lift.setDoorClose();
					sleep(5000);
					liftvraag1 = false;//de liftvraag word weer uit gezet
				}
				if(liftvraag2 == true && motorspeed == 6){//als de lift naar verdieping 2 gaat met 6 snelheid verhoog de snelheid naar 8
					motorspeed = 8;
				}
				if (liftvraag2 == true && motorspeed == 0){//als de lift naar verdieping 2 moet maar nog stil staat, start de motor met 2 snelheid
					motorspeed = 2;
				}
				if (motorspeed == 0 && liftvraag2 == false){//als de motor uit staat en de lift niet naar verdieping 2 moet zet de motor richting naar beneden
					motorRichting = false;
				}
			}
			if (motorRichting == false){// als de motorrichting naar beneden staat voer het volgende uit
				if (liftvraag1 == true && motorspeed == 6){// als de motor met 6 snelheid gaat verhoog deze met 2
					motorspeed = 8;
				}
				if (liftvraag1 == true && motorspeed == 2){//als de lift naar verdieping 1 moet en de snelheid is 2 voer het volgende uit
					motorspeed = 0;//zet de motor uit
					lift.setMotorSpeed(motorspeed);
					lift.setDoorOpen();//open de liftdeuren voor 5 seconden en sluit deze weer na 5 seconden
					sleep(5000);
					lift.setDoorClose();
					sleep(5000);
					liftvraag1 = false;//de liftaanvraag is afgehandeld
				}
				if (liftvraag0 == true){//als de lift naar verdieping 0 moet verhoog de snelheid met 2
					motorspeed = 2;
				}
				if (motorspeed == 0 && liftvraag0 == false){//als de lift op verdieping 0 is en er is geen liftvraag op verdieping 0 verander de motor richting naar boven
					motorRichting = true;
				}
				
			}
		}
		if (huidigeLocatie == 6){//als de locatie 6 is voer het volgende uit
			if (motorRichting == true){//als de lift naar boven gaat en de snelheid is 8 verlaag deze naar 6
				if(motorspeed == 8){
					motorspeed = 6;
				}
				else if (motorspeed != 6){//als de snelheid niet 6 is verlaag de snelheid naar 2
					motorspeed = 2;
				}
			}
			if(motorRichting == false){//als de motor naar beneden staat voer het volgende uit
				if(liftvraag0 == true && liftvraag1 == false){//als de lift naar verdieping 0 moet en niet naar 1 verander de snelheid naar 6
					motorspeed = 6;
				}
				else if (liftvraag1 == true){//als de lift naar verdieping 1 moet verlaag de snelheid naar 0
					motorspeed = 2;
				}
			}
		}
		if (huidigeLocatie == 7){//als de lift op positie 7 is verlaag de snelheid naar 4
			motorspeed = 4;
		}
		if (huidigeLocatie == 8){//als de locatie 8 is verlaag de snelheid naar 2
			motorspeed = 2;
		}
		if (huidigeLocatie == 9){//als de lift op locatie 9 is voer het volgende uit
			if (liftvraag2 == true){//als de lift naar 2 moet voer het volgende uit
				motorspeed = 0;//zet de motor uit
				liftvraag2 = false;//schakel die lift aanvraag voor verdieping 2 uit
				lift.setMotorSpeed(motorspeed);
				lift.setDoorOpen();//zet de lift deuren open en sluit ze weer na 5 seconden
				sleep(5000);
				lift.setDoorClose();
				sleep(5000);
				motorRichting = false;//zet de liftrichting naar beneden
			}
			//als de motorrichting naar beneden staat en de lift naar verdieping 1 of 0 moet zet de snelheid op 2
			if (motorRichting == false && (liftvraag1 == true || liftvraag0 == true))
			{
				motorspeed = 2;
			}
		}

	}   

	//Methode om de deuren te openen en te sluiten bij aankomst van juiste verdieping
	public static boolean LiftDeuren(){
		lift.setDoorOpen();
		sleep(5000);
		lift.setDoorClose();
		sleep(5000);
		if(lift.getDoorSignals()==0){
			return true;
		}
		else{
			return false;
		}
		
	}
    
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
