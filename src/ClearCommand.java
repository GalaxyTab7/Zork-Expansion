class ClearCommand extends Command
{
	public String execute()
	{
		String rtnMe = "";
		for (int i = 0 ; i < 50 ; i ++)
		{
			rtnMe += "\n";
		}
		rtnMe += Gamestate.instance().getCurrentRoom().describe();
		return rtnMe;
	}
}
