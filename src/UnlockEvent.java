class UnlockEvent implements Event
{
	private String srcRoom;
	private String dir;

	public UnlockEvent(String[] attrs) throws Exception
	{
		if (attrs.length != 3)
		{
			throw new Exception();
		}
		this.srcRoom = attrs[0];
		this.dir = attrs[1];
	} 

	public void trigger() throws Exception,WrongRoomException
	{
		Room r = null;
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
		e = r.getExit(dir);
		if (e == null)
		{
			throw new NoExitException();
		}
		e.setIsLocked(false);
	}
}
