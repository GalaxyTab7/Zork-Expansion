class LockEvent implements Event
{
	private String srcRoom;
	private String dir;

	public LockEvent(String[] attrs) throws Exception
	{
		if (attrs.length != 3)
		{
			throw new Exception();
		}
		this.srcRoom = attrs[0];
		this.dir = attrs[1];
	}



	public void trigger() throws Exception,WrongRoomException,NoExitException
	{
		Room r = null;
		Exit oppExit = null;
		Exit e = null;
		Gamestate instance = Gamestate.instance();

		if (srcRoom.equals("any"))
		{
			r = instance.getCurrentRoom();
		}
		else
		{
			r = instance.getDungeon().getRoom(this.srcRoom);
		}

		if (r != instance.getCurrentRoom())
		{
			throw new WrongRoomException();
		}
		if (r == null)
		{
			throw new Exception();
		}

		//Now get the adjacent room and lock the exit going to r in the opposite direction in which was locked.
		
		Room adjRoom = r.leaveBy(dir);
		if (adjRoom == null)
		{
			throw new NoExitException();
		}
		
		String oppDir = "";

		//E W
		if (dir.equals("w"))
		{
			oppDir = "e";
		}
		else if (dir.equals("e"))
		{
			oppDir = "w";
		}

		//N S
		if (dir.equals("n"))
		{
			oppDir = "s";
		}

		else if (dir.equals("s"))
		{
			oppDir = "n";
		}

		//U D
		if (dir.equals("u"))
		{
			oppDir = "d";
		}
		else if (dir.equals("d"))
		{
			oppDir = "u";
		}

		oppExit = adjRoom.getExit(oppDir);
		e = r.getExit(dir);

		if (e == null && oppExit == null)
		{
			throw new NoExitException();
		}
		else if (e != null && oppExit != null)
		{
			e.setIsLocked(true);
			oppExit.setIsLocked(true);
		}
		else if (e != null && oppExit == null)
		{
			e.setIsLocked(true);
		}
		else
		{
			oppExit.setIsLocked(true);
		}
	
	}
}
