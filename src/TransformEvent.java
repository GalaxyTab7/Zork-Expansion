import java.util.*;

class TransformEvent implements Event 
{
	private String fromItemName;
	private String toItemName;

	TransformEvent(String[] itemNames) throws Exception
	{
		if (itemNames.length != 2)
		{
			throw new Exception("Wrong Size For Attributes");
		}
		toItemName = itemNames[0];
		fromItemName = itemNames[1];
	}

	public void trigger() throws Exception
	{
		Gamestate instance = Gamestate.instance();
		Dungeon dungeon = instance.getDungeon();

		Item from = dungeon.getItem(this.fromItemName);
		Item to = dungeon.getItem(this.toItemName);

		if (from == null)
		{
			throw new Exception("Null From Item");
		}
		if (to == null)
		{
			throw new Exception("Null To Item");
		}

		//Checks if the from item is in the inventory
		boolean inInv = false;
		ArrayList<Item> inventory = instance.getInventory();
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i).getPrimaryName().equals(from.getPrimaryName())){
				inInv = true;
				break;
			}
		}

		//Make both items disappear from dungeon.
		try
		{
			String[] args = {from.getPrimaryName()};
			Event goAway = EventFactory.instance().makeEvent("disappear",args);
			goAway.trigger();

			String[] argsTo = {to.getPrimaryName()};
			Event goAwayTo = EventFactory.instance().makeEvent("disappear",argsTo);
			goAwayTo.trigger();	
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("Bad Removal");
		}
		//Place to item in inventory or in room depending on where from item was
		if (inInv)
		{
			instance.addToInventory(to);

		}	
		else
		{
			instance.addItemToRoom(to,instance.getCurrentRoom());
		}
	}
} 
