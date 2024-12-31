import java.util.*;

class DropEvent implements Event{

	private String droppedItem;

	DropEvent(String[] itemName)throws Exception{

		if(itemName.length != 1){
			throw new Exception("Wrong size for attributes");
		}
		this.droppedItem = itemName[0];
	}


	public void trigger(){
		Dungeon dungeon = Gamestate.instance().getDungeon();
		Item item = dungeon.getItem(this.droppedItem);
		ArrayList<Item> inventory = Gamestate.instance().getInventory();

		//Checks to see if the item is in the inventory
		boolean result = false;
		for(int i=0; i < inventory.size();i++){

			if(inventory.get(i).getPrimaryName().equals(item.getPrimaryName())){
				result = true;
			}
		}
		//drops the item if it is in the inventory and puts it in room
		if(result == true){
			Gamestate.instance().removeFromInventory(item);
			Gamestate.instance().addItemToRoom(item , Gamestate.instance().getCurrentRoom());
		}
	}

}
