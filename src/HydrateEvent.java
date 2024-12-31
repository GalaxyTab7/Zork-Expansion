class HydrateEvent implements Event
{
	private int amount;

	public HydrateEvent(String[] attrs)
	{
		this.amount = Integer.parseInt(attrs[0]);
	}

	public void trigger() throws Exception
	{
		Gamestate.instance().changeHydration((int)this.amount);
		if (Gamestate.instance().getHydration() == 0)
		{
			String[] attrs = {};
			try
			{
				Event e = EventFactory.instance().makeEvent("die" , attrs);
				e.trigger();
			}
			catch (Exception e)
			{
				System.out.println(attrs[100]);
			}
		}
	}



}
