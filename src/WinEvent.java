class WinEvent implements Event
{
	public WinEvent(String[] attributes)
	{
	}

	public void trigger()
	{
		System.out.println("Good job you won!!!");
		Gamestate.instance().setPlayStatus(false);
	}
}
