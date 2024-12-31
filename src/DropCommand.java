import java.util.*;
class DropCommand extends Command{

	private String itemName;

	DropCommand(String item){
		this.itemName = item;
	}
	String execute(){

		Room room = Gamestate.instance().getCurrentRoom();
		ArrayList<Item> items = new ArrayList<Item>();
		
		//hard copy arraylist from Gamestate
		for (Item i : Gamestate.instance().getInventory())
		{
			items.add(i);
		}

		//if the user hasn't specified what to drop
		if(this.itemName.equals(null)||this.itemName.length()==0){
			return("Drop what?");
		}

		//if the user wants to drop everything
		if(this.itemName.equals("all")){

			if (items.size() == 0)
			{
				return "Nothing in Inventory";
			}

			String message = "";
			for(int i = 0; i < items.size(); i++){
				Gamestate.instance().addItemToRoom(items.get(i) , room);
				Gamestate.instance().removeFromInventory(items.get(i));
				message = message + "Dropped " + items.get(i).getPrimaryName() + "\n";
			}        

			return(message + super.changeTime(1));
		}

		//Determines if the user has the item in their inventory
		boolean result = false;
		for(int i = 0; i < items.size(); i++){
			if(items.get(i).goesBy(this.itemName) == true){
				result = true;
				break;
			}
		}
		if(result==false){
			return("You are not carrying a " + this.itemName);
		}

		try{
			Item item = Gamestate.instance().getItemFromInventoryNamed(this.itemName);
			Gamestate.instance().removeFromInventory(item);
			room.add(item);
		}catch(NoItemException e){
			return("You are not carrying a " + this.itemName);
		}

		return("Dropped " + this.itemName + super.changeTime(1));
	}


}
