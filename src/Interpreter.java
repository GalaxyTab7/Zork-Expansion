import java.util.*;
import java.io.*;
class Interpreter{
	public static void main (String[] args){   

		if(args.length < 1) {
			System.out.println("Usage: Interpreter zorkFile.zork|saveFile.sav");
			System.exit(1);
		}

		String file = args[0] ;
		Scanner scan = new Scanner(System.in);

		if(file.contains(".zork")){
			Dungeon sample = null;
			try {
				sample = new Dungeon(file);
			} catch(Exception e){
				System.out.println("Zork file version not compatible");
				System.out.println("Usage: Interpreter zorkFile.zork|saveFile.sav");
				return;
			}
			System.out.println(sample.getTitle());
			if (sample.getTitle().equals("The Gas Gauntlet"))
			{
			
			System.out.println("Congratulations! You had an incredibly successful week at work and to celebrate a brand new paycheck you hop in your car " + "\n" +
					"and are off to the Vegas Strip to make it big gambling. *BUT* Disaster strikes and your car runs out of gas in the middle of the harsh desert."  +"\n" +
					"Will you succumb to the desert? Or will you bet it all on red and find the gas you need?");
			}
			Gamestate.instance().initialize(sample);
			System.out.println(Gamestate.instance().getDungeon().getEntry().describe());

		}else if(file.contains(".sav")){
			try{
				Gamestate.instance().restore(file);
			}catch(Exception e){
				System.out.println("Save file version not compatible");
				System.out.println("Usage: Interpreter zorkFile.zork|saveFile.sav");
				return;
			}
			System.out.println(Gamestate.instance().getCurrentRoom().describe());    
		}else{
			System.out.println("Usage: Interpreter zorkFile.zork|saveFile.sav");
			System.exit(2);
		}
		String input = "";



		while(Gamestate.instance().getPlayStatus()){
			input = scan.nextLine();
			if (input.equals("q"))
			{
				break;
			}
			CommandFactory c = CommandFactory.instance();
			String result = (c.instance().parse(input).execute());
			if (Gamestate.instance().getDungeon().getRoom("Car") != null && Gamestate.instance().getDungeon().getRoom("Car").getLiquids().get("gas") >= 1)
			{
				result = (c.instance().parse("score").execute());
				System.out.println("You pump your car with gas and speed away into the sunset leaving behind monsters and heading towards slots.");
				System.out.println("Your Score is ... ");
				System.out.println(result);	
				return;
			}
			if (Gamestate.instance().getPlayStatus())
			{
				System.out.println(result);
			}
		}
	}
}       






