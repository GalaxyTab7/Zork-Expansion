class DieEvent implements Event
{
	
	public DieEvent(String[] attributes)
	{
	}

	public void trigger() throws Exception
	{
		System.out.println("You lost the gamble.");
		Gamestate.instance().setPlayStatus(false);
	}
}
