import java.util.*;
class DeveloperOnlyCommand extends Command
{
	String execute()
	{
		Gamestate instance = Gamestate.instance();
		String rtnMe = "\nDeveloper Information\n";
		rtnMe += "Health: " + instance.getHealth() + "\n";
		rtnMe += "Hydration " + instance.getHydration() + "\n";
		rtnMe += "Score: " + instance.getScore() + "\n";
        rtnMe += "Capacity: " + instance.getCapacity() + "\n";
		rtnMe += "Location: " + instance.getCurrentRoom() + "\n";
		rtnMe += "Items In Inventory:\n";

		for (Item i : instance.getInventory())
		{
			rtnMe += " " + i.toString() + "\n";
		}

		rtnMe += "Items In All Rooms\n";
		Hashtable<Room , HashSet<Item>> stuff = instance.getAllRoomContents();	
		for (Room r : stuff.keySet())
		{
			HashSet<Item> things = stuff.get(r);
			
			rtnMe += r.toString() + ": ";
			rtnMe += things.toString() + "\n";
		}
		
		rtnMe += "Fillable room info\n";
		for (String name : instance.getDungeon().getAllRooms().keySet())
		{
			Room tehe = instance.getDungeon().getAllRooms().get(name);
			rtnMe += tehe.getName();
			rtnMe += " " + tehe.getIsFillable() + " " + tehe.getLiquids();
			rtnMe += "\n";
		}

		rtnMe += "Time: " + Clock.instance().getTime()+"\n"; 
		rtnMe += "Timed Objects: " + Clock.instance().getAllTimedObjects() + "\n"; 
		for (Monster m : instance.getDungeon().getAllMonsters())
		{
			rtnMe += m.describeMonster() + " curloc: " + m.getCurrentRoom() + " pathPos " + m.getPath().getPathPos() + " path: " + m.getPath() + "stuck: " + m.checkStuckStatus() + m.checkStuckTime() + " ID: " + m.getID() +"\n";

		}

		rtnMe += "Fillable Item Info:" + "\n";
		for (Room r : stuff.keySet())
		{
			HashSet<Item> things = stuff.get(r);

			for (Item thing : things)
			{
				rtnMe += thing.getPrimaryName() + " current fill: " + thing.getCurrentFill() + " max fill: " + thing.getMaxFill();
				rtnMe += "\n";
			}
		}
		
		return rtnMe;
	}	
}
