import java.util.*;
class CommandFactory {

	private static CommandFactory theInstance = null;

	public static synchronized CommandFactory instance(){
		if(CommandFactory.theInstance == null){
			CommandFactory.theInstance = new CommandFactory();
		}
		return CommandFactory.theInstance;
	}

	private CommandFactory(){
	}

	Command parse(String commandString){
	    Scanner scan = new Scanner(System.in);
        Command c = null;
        String words[] = commandString.split(" ");
        String verb = words[0];
        String noun = "";        

        if(words.length==2){
            noun = words[1];
        }

	//for fill command
	else if (words.length == 3 && verb.toLowerCase().equals("fill"))
	{
		try
		{
			String from = words[1];
			String to = words[2];
			c = new FillCommand(from , to);
			return c;
		}
		catch(Exception e)
		{
			c = new UnknownCommand( true , e.getMessage() ); 
			return c;
		}
	}
        
        if(verb.equals("n")||verb.equals("s")||verb.equals("e")||verb.equals("w")||verb.equals("u")||verb.equals("d")){
             c = new MovementCommand(verb);

        }else if(verb.equals("save")){
            System.out.println("Please enter a save name:");
            String file = scan.nextLine(); 
            c = new SaveCommand(file);
        }else if(verb.equals("look")){
             c = new LookCommand();
        }else if(verb.equals("i")||verb.equals("inventory")){
             c = new InventoryCommand();
        }else if(verb.equals("take")){
             c = new TakeCommand(noun);
        }else if(verb.equals("drop")){
             c = new DropCommand(noun);
        }else if(words.length==2){
            c = new ItemSpecificCommand(verb,noun);
        }else if(verb.equals("health")){
            c = new HealthCommand();
        }else if(verb.equals("score")){
            c = new ScoreCommand();    
        }else if(verb.equals("PeePeePooPoo")){
		c = new DeveloperOnlyCommand();
	}else if(verb.equals("clear")){
		c = new ClearCommand();	
	}else{
             c = new UnknownCommand(commandString);
        }

        return(c);

    	}
    }

