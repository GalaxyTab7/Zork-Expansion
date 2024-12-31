import java.util.*;

class ItemSpecificCommand extends Command{

	private String verb;
	private String noun;

	ItemSpecificCommand(String verb, String noun){
		this.verb = verb;
		this.noun = noun;
	}

	String execute(){

		ArrayList<Event> events = null;        
		Item item = null;
		String message = "";

		try{
			item = Gamestate.instance().getItemInVicinityNamed(this.noun);
		}catch(NoItemException e){
			return("There is no " + this.noun + " here.");
		}

		//Collects a list of events and triggers them if there is any to trigger
		try{
			events = item.getEventsForVerb(this.verb);
		}catch(NoSuchVerbException e){
		}

		if(events!=null && events.size() != 0){
			for(int i = 0; i < events.size(); i ++){
				try{
					events.get(i).trigger();	
				}
				catch(WrongRoomException balls)
				{
					message += "Wrong room for key";			
					return message;
				}
				catch(NoExitException wellShucks)
				{
					message += "No exit in that direction";
					return message;
				}
				catch(CannotFillException dang)
				{
					message += dang.getMessage();
					return message;
				}
				catch(Exception e){
					e.printStackTrace();
					ArrayList<String> err = new ArrayList<String>();
					err.get(200);
				}
			}
		}
        
        //Determines whether or not the item is a hallucination. If it is, checks whether its in the room or inventory and dissapears it
        boolean result = item.hallucinationStatus();
        ArrayList<Item> inventory = Gamestate.instance().getInventory();
        HashSet<Item> roomItems = Gamestate.instance().getItemsInRoom(Gamestate.instance().getCurrentRoom());

        if(result == true){
            boolean r = false;    
            //inventory
            for(int i = 0; i < inventory.size(); i ++){
                    if(inventory.get(i).getPrimaryName().equals(item.getPrimaryName())){
                        r = true;
                    }        
                }
            if(r == true){
                Gamestate.instance().removeFromInventory(item);
            }
            //room
            if(roomItems.contains(item)){
                Gamestate.instance().removeItemFromRoom(item, Gamestate.instance().getCurrentRoom());         
            }
            
            return("It disappears right in front of your eyes. The heat of the desert must be getting to you." + super.changeTime(1));
        }

		try{
			message += item.getMessageForVerb(this.verb);
		}catch(NoSuchVerbException e){
			return("You cannot " + this.verb + " the " + this.noun);
		}
		return (message + super.changeTime(1));
	}


}
