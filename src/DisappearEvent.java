import java.util.*;

class DisappearEvent implements Event{

	private String disappearedItem;

	DisappearEvent(String[] itemName)throws Exception{

		if(itemName.length != 1){
			throw new Exception("Wrong size for attributes");
		}

		this.disappearedItem = itemName[0];
	}


	public void trigger(){

		Dungeon dungeon = Gamestate.instance().getDungeon();
		Item item = dungeon.getItem(this.disappearedItem);
		ArrayList<Item> inventory = Gamestate.instance().getInventory();
		HashSet<Item> roomItems = Gamestate.instance().getItemsInRoom(Gamestate.instance().getCurrentRoom());

		//Checks if the item is in the inventory, and removes it if it is
		boolean result = false;
		for(int i = 0; i < inventory.size(); i++){

			if(inventory.get(i).getPrimaryName().equals(item.getPrimaryName())){
				result = true;     
			}
		}

		if(result == true){
			Gamestate.instance().removeFromInventory(item);
		}

		//Checks if the item is in the room
		else if(roomItems.contains(item)){
			Gamestate.instance().removeItemFromRoom(item, Gamestate.instance().getCurrentRoom());     
		}

		//if neither of the above work, check all rooms and remove item from the one containing it.
		else{
			Room inRoom = null;
			Hashtable<Room,HashSet<Item>> stuff= Gamestate.instance().getAllRoomContents();
			for (Room r : stuff.keySet())
			{
				if (stuff.get(r).contains(item))
				{
					inRoom = r;
					break;
				}
			}

			if (inRoom == null)
			{
				return;
			}
			Gamestate.instance().removeItemFromRoom(item , inRoom);
		}
	}
}
