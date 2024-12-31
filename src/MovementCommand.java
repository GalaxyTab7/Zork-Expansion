class MovementCommand extends Command{

	private String dir;

	MovementCommand(String dir){
		this.dir = dir;
	}

	String execute(){
		String move = " ";
		Dungeon dungeon = Gamestate.instance().getDungeon();
		Room room = Gamestate.instance().getCurrentRoom();

		try
		{
			room = room.leaveBy(this.dir);
			if(room == null)
			{
				move = "Sorry, you can't go " + this.dir + " from this room.";
				return(move);
			}
		}
		catch (LockedException e)
		{
			return e.getMessage();
		}

		Gamestate.instance().setCurrentRoom(dungeon.getRoom(room.getName()));
		move = room.describe();
		try
		{
			String[] attrs = {"-5"};
			Event e = new HydrateEvent(attrs);
			e.trigger();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String[] bad = {};
			System.out.println(bad[2]);
		}

		return(move + super.changeTime(2));
	}
}


