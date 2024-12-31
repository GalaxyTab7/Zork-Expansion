import java.util.*;
class TakeCommand extends Command{

	private String itemName;

	TakeCommand(String item){
		this.itemName = item;
	}

	String execute(){

		Room room = Gamestate.instance().getCurrentRoom();
		HashSet<Item> set = room.getContents();
		ArrayList<Item> roomItems = new ArrayList<Item>(set);

		//Either takes or denys to take an item depending on if it's in the room
		//Sees if the item is in the room
		boolean result = false;
		for(int i = 0; i < roomItems.size(); i++){
			if(roomItems.get(i).goesBy(this.itemName) == true){
				result = true;
				break;
			}
		}
		if(this.itemName.equals("all"))
		{
			result = true;
		}
		if(result == false){
			return("There is no " + this.itemName + " here.");
		}

		if(this.itemName.equals(null)||this.itemName.length()==0){
			return("Take what?");
		}       

		int totalWeight = 0;
		int roomWeight = 0;

		//Calculates the total weight of the player's inventory
		ArrayList<Item> items = new ArrayList<Item>();

		for (Item i : Gamestate.instance().getInventory())
		{
			items.add(i);
		}


		for(int i = 0; i < items.size(); i++){
			totalWeight = totalWeight + items.get(i).getWeight();
		}   

		//Calculates the total weight of the items in the room

		for(int i = 0; i < roomItems.size(); i++){
			roomWeight = roomWeight + roomItems.get(i).getWeight();
		}

		//If the item is never obtainable
		if (this.itemName.equals("all"))
		{	
			for (Item i : Gamestate.instance().getAllRoomContents().get((Gamestate.instance().getCurrentRoom())))
			{
				if (i.getWeight() == 5000)
				{
					return ("No matter how hard you try the item is unmovable");
				}
			}
		}
		else if (Gamestate.instance().getDungeon().getItem(this.itemName).getWeight() == 5000)
		{
			return ("No matter how hard you try the item is unmovable"); 
		}

		//If the user wants to take all items in the room
		if(this.itemName.equals("all")){
			if(totalWeight + roomWeight > Gamestate.instance().getCapacity()){
				bagRip();
				return("*Rip* You feel your pockets lighten, some of your items might spill onto the floor"); 
			}else if (roomItems.size() == 0)
			{
				return "Nothing in " + Gamestate.instance().getCurrentRoom().getName();
			}
			else{
				String message = "";    
				for(int i = 0; i < roomItems.size(); i++){
					Gamestate.instance().addToInventory(roomItems.get(i));
					Gamestate.instance().removeItemFromRoom(roomItems.get(i) , Gamestate.instance().getCurrentRoom());
					message = message + "Took " + roomItems.get(i).getPrimaryName() + "\n";
				}
				return(message + "All items taken" + super.changeTime(1));
			}
		}

		//Sees if the item is in the room
		result = false;
		for(int i = 0; i < roomItems.size(); i++){
			if(roomItems.get(i).goesBy(this.itemName) == true){
				result = true;
				break;
			}
		}
		//Either takes or denys to take an item depending on if it's in the room
		if(result == false){
			return("There is no " + this.itemName + " here.");
		}

		try{ if(result == true && room.getItemNamed(this.itemName).getWeight() + totalWeight <= Gamestate.instance().getCapacity()){
			Gamestate.instance().addToInventory(room.getItemNamed(this.itemName));
			Gamestate.instance().removeItemFromRoom(room.getItemNamed(this.itemName),room);
		} 
		} catch(NoItemException e){

		}try{
			if(room.getItemNamed(this.itemName).getWeight() >= Gamestate.instance().getCapacity()){
				bagRip();
				return("*Rip* You feel your pockets lighten, some of your items might spill onto the floor");
			} 
		}catch(NoItemException e){
		}

		return(this.itemName + " taken." + super.changeTime(1));
	}



	//Decreases the user's capacity by 5 if they try to carry something heavy
	public static void bagRip(){

		int capacity = Gamestate.instance().getCapacity();
		int inventoryWeight = 0;
		Room currRoom = Gamestate.instance().getCurrentRoom();

		if(Gamestate.instance().getCapacity() == 0){
			Gamestate.instance().changeCapacity(capacity);
		}else{   
			Gamestate.instance().changeCapacity(capacity-5);
			capacity = capacity-5;
		}

		ArrayList<Item> items = new ArrayList<Item>();
		//Mock inventory
		for(Item i : Gamestate.instance().getInventory()){
			items.add(i);
		}
		//Calculating weight for players inventory
		for(int i=0;i<items.size();i++){
			inventoryWeight += items.get(i).getWeight();
		}
		//Drops items that no longer fit in current capacity.
		while(inventoryWeight > capacity && inventoryWeight != 0){
			int i = 0;
			Gamestate.instance().removeFromInventory(items.get(i));
			Gamestate.instance().addItemToRoom(items.get(i),currRoom);
			inventoryWeight -= items.get(i).getWeight(); 

			i++;
		}

	}

}
