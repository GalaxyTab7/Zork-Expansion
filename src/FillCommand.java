class FillCommand extends Command
{
	private Room fromRoom;
	private Room toRoom;
	private Item fromItem;
	private Item toItem;

	public FillCommand(String from , String to) throws Exception
	{
		this.fromItem = Gamestate.instance().getDungeon().getItem(from);
		this.toItem = Gamestate.instance().getDungeon().getItem(to);
		
		this.fromRoom = Gamestate.instance().getDungeon().getRoom(from);
		this.toRoom = Gamestate.instance().getDungeon().getRoom(to);	

		if (this.fromItem == null && this.toItem == null && this.fromRoom == null && this.toRoom == null)
		{
			throw new Exception("No items or rooms to fill from to recognized");
		}

		if (!((this.fromItem != null && this.toItem != null) || (this.fromItem != null && this.toRoom != null) || (this.fromRoom != null && this.toItem != null)))
		{
			throw new Exception("Fillables specified in from or to slot dont exist. Check your spelling dumbo.");
		}
	}

	public String execute()
	{
		if (this.fromItem != null && this.toItem != null)
		{
			try
			{
				Gamestate.instance().getItemInVicinityNamed(fromItem.getPrimaryName());
				Gamestate.instance().getItemInVicinityNamed(toItem.getPrimaryName());
			}
			catch (NoItemException tehe)
			{
				return "One of those items is not in this room";
			}
			try
			{
				this.fromItem.drainToItem(this.toItem);
				return "Filled " + this.toItem.getPrimaryName() + " from " + this.fromItem.getPrimaryName() + "!" + super.changeTime(1);
			}
			catch (CannotFillException e)
			{
				return e.getMessage();
			}
		}	

		if (this.fromItem != null && this.toRoom != null)
		{
			try
                        {
                                Gamestate.instance().getItemInVicinityNamed(fromItem.getPrimaryName());
                        }
                        catch (NoItemException tehe)
                        {
                                return "One of those items is not in this room";
                        }
			try
			{
				this.fromItem.drainToRoom(this.toRoom);
				return "Filled " + this.toRoom.getName() + " from " + this.fromItem.getPrimaryName() + "!" + super.changeTime(1);
			}
			catch (CannotFillException e)
			{
				return e.getMessage();
			}
		}

		if (this.fromRoom != null && this.toItem != null)
		{
                        try
                        {
                                Gamestate.instance().getItemInVicinityNamed(toItem.getPrimaryName());
                        }
                        catch (NoItemException tehe)
                        {
                                return "One of those items is not in this room";
                        }
			try
			{
				this.toItem.fillFromRoom(this.fromRoom);
				return "Filled " + this.toItem.getPrimaryName() + " from " + this.fromRoom.getName() + "!" + super.changeTime(1);
			}
			catch (CannotFillException e)
			{
				return e.getMessage();
			}
		}
		
		//Lol this is goofy as shit
		String[] wellFuck = {};
		System.out.println(wellFuck[100]);
		return "Pov your an unreachable return statment :(";
	}

}
