import java.util.*;
public class Item
{
	//Inst Vars
	private String primaryName;
	private int weight;

	//Stuff for fillable items
	private boolean isFillable;
	private double maxFill;
	private double currentFill;
	private String fillType;

	//Stuff for hallucinations
	private boolean isHallucination;
	private final boolean canBeHallucination; //final so it cant be changed

	/**
	 * This hashtable contains keys of verbs and values of messages.
	 * So basically, messages.get(verb) returns the message associated with
	 * performing the verb action using the item.
	 * For example, messages.get("Swing") might return "swish" if the item is a
	 * sword.
	 */
	private Hashtable<String,String> messages; 

	/**
	 * This hashset contains values of diffrent ways you can refer to the item
	 * so something like aliases.contains("word") will return true if the word
	 * is in the aliases hashset. Super duper handy imo.
	 * Also, by default the aliases hashset will always contain this.primaryName.
	 */
	private HashSet<String> aliases;

	private Hashtable<String,ArrayList<Event>> triggers;
	//An empty constructor I use for testing and stuff we wont need it
	public Item()
	{
		this.triggers = new Hashtable<String,ArrayList<Event>>();
		this.aliases = new HashSet<String>();
		this.aliases.add("");
		this.messages = new Hashtable<String,String>(); 
		this.primaryName = "";
		this.weight = 0;
		this.isFillable = false;
		this.maxFill = 0.0;
		this.currentFill = 0.0;
		this.fillType = "none";
		this.isHallucination = false;
		this.canBeHallucination = false;

	}

	/** 
	 *Format for an individual item. Scaning will always start at the primaryName part.
	 *
	 *primaryName, *aliases
	 *weight
	 * verb:message
	 * *(more verb:message pairs)
	 *---
	 *
	 * The end section delimeter for items is ===
	 */
	public Item(Scanner scan) throws NoItemException,BadEventConstructionException
	{
		this.aliases = new HashSet<String>();
		this.messages = new Hashtable<String,String>(); 
		this.triggers = new Hashtable<String,ArrayList<Event>>();
		String names = scan.nextLine().trim();
		if (names.equals("==="))
		{
			throw new NoItemException();
		}

		//This part adds the aliases and the primary name
		names += ",";
		boolean needPrimary = true;
		while (names.indexOf(",") != -1)
		{
			if (needPrimary)
			{
				needPrimary = false;
				this.primaryName = names.substring(0,names.indexOf(","));
				this.aliases.add(this.primaryName);
				names = names.substring(names.indexOf(",") + 1 , names.length());
			}			
			else
			{
				this.aliases.add( names.substring(0,names.indexOf(",")) );
				names = names.substring(names.indexOf(",") + 1 , names.length());
			}
		}

		//This part gets the weight
		this.weight = Integer.parseInt(scan.nextLine().trim());

		String next = scan.nextLine();

		//fillable item stuff
		boolean test = false;

		//Default values
		boolean isFillableTemp = false;
		double maxFillTemp = 0.0;
		double currentFillTemp = 0.0; 
		String fillTypeTemp = "none";

		if (next.indexOf(":") != -1)
		{
			try
			{
				test = next.substring(0,9).equals("Fillable:");
			}
			catch (Exception e)
			{
			}
			if (test)
			{
				String[] stats = next.substring(9 , next.length()).split(",");
				isFillableTemp = Boolean.parseBoolean(stats[0]);
				fillTypeTemp = stats[1];
				maxFillTemp = Double.parseDouble(stats[2]);
				currentFillTemp = Double.parseDouble(stats[3]);
				next = scan.nextLine();
			}
		}

		//set values
		this.isFillable = isFillableTemp;
		this.fillType = fillTypeTemp;
		this.maxFill = maxFillTemp;
		this.currentFill = currentFillTemp;

		//hallucination stuff
		test = false;

		//Default values
		boolean canBeHallucinationTemp = true;
		boolean isHallucinationTemp = false;

		if (next.indexOf(":") != -1)
		{
			try
			{
				test = next.substring(0,14).equals("Hallucination:");
			}
			catch (Exception e)
			{
			}
			if (test)
			{
				String[] stats = next.substring(14,next.length()).split(",");
				canBeHallucinationTemp = Boolean.parseBoolean(stats[0]);
				isHallucinationTemp = Boolean.parseBoolean(stats[1]);
				next = scan.nextLine();
			}
		}

		//set values
		this.canBeHallucination = canBeHallucinationTemp;
		this.isHallucination = isHallucinationTemp;



		//This part adds the verb message pairs
		while (true)
		{
			if(next.equals("---"))
			{
				break;
			}
			String key = next.substring(0 , next.indexOf(":"));

			if (key.contains("["))
			{
				String temp = key.substring(key.indexOf("[")+1,key.indexOf("]"));
				key = key.substring(0 , next.indexOf("[")); // makes sure the verb is right

				//add the new arraylist to triggers
				ArrayList<Event> addMe = new ArrayList<Event>();
				this.triggers.put(key , addMe);

				String[] arr = temp.split(",");
				for (String str : arr)
				{
					//This does eveything to make the event and put it in the triggers Hashtable
					String eventName = "";
					ArrayList<String> attributes = new ArrayList<String>();

					if (!(str.contains("(")))
					{
						eventName = str.toLowerCase();
					}
					else
					{
						//This seems really convoluted but its mainly so an event can have multiple attributes instead
						//of just one. If it needs to ahve more than one attribute then each should be deliminated
						//by a ~ between the ()'s.
						eventName = str.substring(0,str.indexOf("(")).toLowerCase();
						String anotherTemp = str.substring(str.indexOf("(")+1,str.indexOf(")"));
						String[] anotherArr = anotherTemp.split("~");
						for (String anotherStr : anotherArr)
						{
							attributes.add(anotherStr);
						}
					}

					Event event = null;	
					try
					{
						//The attributes arraylist will always contain the name of the constructing Item in the last pos
						attributes.add(this.primaryName); 
						event = EventFactory.instance().makeEvent(eventName , attributes.toArray(new String[attributes.size()]));
					}
					catch (BadEventConstructionException err)
					{
						throw err;
					}
					catch (IllegalArgumentException  fart)
					{
						System.out.println(fart.getMessage());
						throw new BadEventConstructionException();
					}
					catch (Exception expt)
					{
						throw new BadEventConstructionException();
					}
					//Adds the event to the triggers
					this.triggers.get(key).add(event);
				}	
			}	
			String value = next.substring(next.indexOf(":") + 1 , next.length());
			this.messages.put(key,value);
			next = scan.nextLine();
		}	

	}

	//Fill Event stuff
	public boolean getIsFillable(){
		return this.isFillable;
	}

	public double getCurrentFill(){
		return this.currentFill;
	}

	public double getMaxFill(){
		return this.maxFill;
	}

	public void setCurrentFill(double fill){
		this.currentFill = fill;
	}

	public String getType()
	{
		return this.fillType;
	}


	//Returns whether or not an item is a hallucination
	public boolean hallucinationStatus(){
		return(this.isHallucination);
	}

	//Returns true if the name is in the aliases hashset for the item, otherwise it throws an exception
	public boolean goesBy(String name) 
	{
		if (aliases.contains(name))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//Getters
	public String getPrimaryName()
	{
		return this.primaryName;
	}

	public ArrayList<Event> getEventsForVerb(String verb) throws NoSuchVerbException{

		ArrayList<Event> list = this.triggers.get(verb);

		if(list == null){
			throw new NoSuchVerbException();
		}

		return(list);
	}

	//Returns the message associated with a given verb assuming it exists. If it does not exist, then a NoSuchVerbException is thrown.
	public String getMessageForVerb(String verb) throws NoSuchVerbException
	{
		String rtnMe = messages.get(verb);
		if (rtnMe == null)
		{
			throw new NoSuchVerbException();
		}	
		else
		{
			return rtnMe;
		}
	}

	public int getWeight()
	{
		return this.weight;
	}

	//toString
	public String toString()
	{
		return "A " + this.primaryName + " weighing " + this.weight;
	}

	String getAllAlias()
	{
		return this.aliases.toString();
	}

	public void drainToItem(Item i) throws CannotFillException
	{
		if (this.currentFill > 0 && i.getType().equals(this.fillType))
		{	
			try
			{
				String[] attrs = { (((int)this.currentFill) + ""),i.getPrimaryName()};
				Event event = EventFactory.instance().makeEvent("fill" , attrs);
				event.trigger();
			}	
			catch (CannotFillException e)
			{
				throw e;
			}
			catch (Exception crap)
			{
				//I use these alot as a way of catching mistakes without having to deal with catching actual exceptions
				String[] darn = {};
				System.out.println(darn[100]);
			}
			this.currentFill = 0;
			return;
		}
		if (this.currentFill <= 0)
		{
			throw new CannotFillException("No liquid in item to drain");	
		}
		throw new CannotFillException("You cant put that type of liquid in here");
	}

	public void fillFromItem(Item i) throws CannotFillException
	{
		if (i.getCurrentFill() > 0 && i.getType().equals(this.fillType))
		{
			try
			{
				String[] attrs = {( ((int) i.getCurrentFill()) + "") , this.primaryName};
				Event event = EventFactory.instance().makeEvent("fill" , attrs);
				event.trigger();
			}
			catch (CannotFillException e)
			{
				throw e;
			}
			catch (Exception crap)
			{
				//I use these alot as a way of catching mistakes without having to deal with catching actual exceptions
				String[] darn = {};
				System.out.println(darn[100]);
			}
			i.setCurrentFill(0);
			return;
		}	
		if(i.getCurrentFill() == 0)
		{
			throw new CannotFillException("No Liquid in item to fill from!");
		}
		throw new CannotFillException("You cant put that type of liquid in here!");
	}

	public void fillFromRoom(Room r) throws CannotFillException
	{
		if ( (!(r.getIsFillable())) || r.getLiquids() == null || r.getLiquids().size() == 0 )
		{
			throw new CannotFillException("You cant fill from this room");
		}

		Hashtable<String , Integer> roomLiquids = r.getLiquids();

		for (String type : roomLiquids.keySet())
		{
			if (type.equals(this.fillType))
			{
				if (roomLiquids.get(type) == 0)
				{
					throw new CannotFillException("There is no more " + type + " in the room!");
				}

				try
				{
					String[] attrs = { roomLiquids.get(type).toString() , this.primaryName};
					Event event = EventFactory.instance().makeEvent("fill" , attrs);
					event.trigger();
				}
				catch (CannotFillException e)
				{
					throw e;
				}
				catch (Exception crap)
				{
					//I use these alot as a way of catching mistakes without having to deal with catching actual exceptions
					String[] darn = {};
					System.out.println(darn[100]);
				}
				roomLiquids.remove(type);
				roomLiquids.put(type , 0);
				return;
			}
			throw new CannotFillException("You cant fill " + this.fillType + " from this room!");
		}
	}

	public void drainToRoom(Room r) throws CannotFillException
	{
		if ( (!(r.getIsFillable())) || r.getLiquids() == null || r.getLiquids().size() == 0 )
		{
			throw new CannotFillException("You cant drain anything to this room!");
		}
		if ( r.getLiquids().get(this.fillType) == null)
		{
			throw new CannotFillException("You cant drain " + this.fillType + " to this room!");
		}
		int amt = r.getLiquids().get(this.fillType);
		r.getLiquids().remove(this.fillType);
		r.getLiquids().put(this.fillType , amt + (int) this.currentFill);
		this.currentFill = 0;
	}


}
