import java.util.*;
class ImproveBagEvent implements Event
{
	private int amount;
	
    ImproveBagEvent(String[] attrs)
	{
		this.amount = Integer.parseInt(attrs[0]);
	}
    

	public void trigger() throws Exception
	{
        int capacity = Gamestate.instance().getCapacity();
        
        Gamestate.instance().changeCapacity(Gamestate.instance().getCapacity() + this.amount);        
	    
	}
}
