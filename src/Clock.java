import java.util.*;
class Clock
{
	private static Clock theInstance = null;
	private int time;
	private ArrayList<TimedObject> timedObjects;

	private Clock()
	{
		this.time = 0;
		this.timedObjects = new ArrayList<TimedObject>();
	}
	
	public void clearTimedObjects()
	{
		this.timedObjects.clear();
	}

	public static synchronized Clock instance()
	{
		if (Clock.theInstance == null)
		{
			Clock.theInstance = new Clock();
		}
		return Clock.theInstance;
	}
	

	public String updateTime(int amount)
	{
		this.time += amount;

		String rtnMe = "";
		int count = 0;
		for (TimedObject obj : this.timedObjects)
		{
			if (obj.isTime(this.time))
			{
				if (count == 0)
				{
					count += 1;
					rtnMe += "\n";
				}
				rtnMe += obj.takeTurn();
				rtnMe += "\n";
			}
		}
		return rtnMe;
	}
	
	public ArrayList<TimedObject> getAllTimedObjects()
	{
		return this.timedObjects;
	}

	public void resetTime()
	{
		this.time = 0;
	}

	public int getTime()
	{
		return this.time;
	}

	public void setTime(int i)
	{
		this.time = i;
	}

	public void addTimedObject(TimedObject obj)
	{
		this.timedObjects.add(obj);
	}

	public void addTimedObject(int pos , TimedObject obj)
	{
		this.timedObjects.add(pos,obj);
	}

	public void removeTimedObject(TimedObject obj) throws NoTimedObjectException
	{
		for (int i = 0 ; i < this.timedObjects.size() ; i ++)
		{
			if (this.timedObjects.get(i).equals(obj))
			{
				this.timedObjects.remove(i);
				return;
			}
		}
	}
}
