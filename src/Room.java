import java.util.*;
public class Room{

	private String name;
	private String desc = " ";
	ArrayList<Exit> exits;

	//Stuff for fillable items
	private Hashtable<String , Integer> liquids;
	private boolean isFillable; 	

	public Room(String name){
		this.name = name;
		exits = new ArrayList<Exit>();
		this.liquids = new Hashtable<String , Integer>();
		this.isFillable = false;
	}
	
	public void setIsFillable(boolean status)
	{
		this.isFillable = status;
	}
	public boolean getIsFillable()
	{
		return this.isFillable;
	}

	public Hashtable<String , Integer> getLiquids()
	{
		return this.liquids;
	}

	public Room(Scanner s) throws NoRoomException,Exception
	{
		exits = new ArrayList<Exit>();
		this.name = s.nextLine().trim();
		this.liquids = new Hashtable<String , Integer>();
		this.isFillable = false;

		if(this.name.trim().equals("===")){
			throw new NoRoomException();
		}

		String next = s.nextLine().trim();
		if (next.substring(0,9).equals("Fillable:"))
		{
			this.isFillable = true;
			String[] liquidStuff = next.substring(9 , next.length()).split(",");
			for (String info : liquidStuff)
			{
				String type = info.substring(0 , info.indexOf("["));
				String amount = info.substring(info.indexOf("[") + 1 , info.indexOf("]"));
				int amountD = Integer.parseInt(amount);
				this.liquids.put(type,amountD);
			}
			next = s.nextLine().trim();
		}
		if (next.substring(0 , 9).equals("Contents:"))
		{
			next = next.substring(10,next.length());
			next += ",";
			while (next.indexOf(",") != -1)
			{
				String itemName = next.substring(0 , next.indexOf(","));
				Item i = Gamestate.instance().getDungeon().getItem(itemName);
				if (i == null)
				{
					System.out.println("null field for item " + itemName);
					throw new Exception();
				}
				Gamestate.instance().addItemToRoom(i , this);
				next = next.substring(next.indexOf(",") + 1 , next.length());
			}

			//For the next part
			next = s.nextLine().trim();
		}
		

		this.desc = "";
		while (!next.trim().equals("---")){
			this.desc += next + "\n";
			next = s.nextLine();   
		}
	}


	public String getName(){
		return (this.name);
	}

	//Fully describes the room to get around whether or not it has been visited
	public String fullDesc(){
		String d = "";

		d = this.name + "\n" + this.desc;
		for(Item item:Gamestate.instance().getItemsInRoom(this)){
			d = d + "\nThere is a " + item.getPrimaryName() + " here.";
		}

		for(int i = 0; i < exits.size(); i++){
			d = d + "\n" + exits.get(i).describe();
		}
		return(d);
	}

	public void setDesc(String descr){
		this.desc = descr;
	}
	//adding getContents method that gets the contents of the room
	public HashSet<Item> getContents() {
		return Gamestate.instance().getItemsInRoom(this);
	}
	//adding getItemNamed method
	public Item getItemNamed(String name) throws NoItemException {
		for (Item item : Gamestate.instance().getItemsInRoom(this)) {
			if (item.goesBy(name)){
				//return the found item
				return item;
			}
		}
		//throw exception if there's not a found item
		throw new NoItemException();
	}
	//adding method to add an item to the room
	public void add(Item item) {
		Gamestate.instance().addItemToRoom(item,this);
	}

	//adding remove method to remove an item from the room
	public void remove(Item item) throws NoItemException 
	{
		Gamestate.instance().removeItemFromRoom(item,this);
	}

	String describe()
	{
		String describe = this.name;
		Room room = Gamestate.instance().getDungeon().getRoom(this.name);

		if(Gamestate.instance().hasBeenVisited(room) == true){
			describe = this.name;  
		}else{
			Gamestate.instance().visit(room);
			describe = this.name + "\n" + this.desc;
			
			if (Gamestate.instance().getItemsInRoom(this) != null)
			{
				for(Item item: Gamestate.instance().getItemsInRoom(this)){
					describe = describe + "There is a " + item.getPrimaryName() + " here\n";
				}
			}
			for(int i = 0; i < exits.size(); i++){
				describe = describe + "\n" + exits.get(i).describe();
			}
		}  
		return (describe);
	}

	Room leaveBy(String dir) throws LockedException{
		Room room = null;    
		for (int i = 0; i < exits.size(); i++){
			if(exits.get(i).getDir().equals(dir))
			{
				if (!(exits.get(i).getIsLocked()))
				{
					room = exits.get(i).getDest();
				}
				else
				{
					throw new LockedException(exits.get(i).getDir());
				}
			} 
		}

		return(room);     
	}

	public void addExit(Exit exit){
		exits.add(exit);
	}

	public String toString()
	{
		return this.name;
	}
	
	public Exit getExit(String dir)
	{
		for (int i = 0 ; i < exits.size(); i ++)
		{
			if (exits.get(i).getDir().equals(dir))
			{
				return exits.get(i);
			}
		}
		return null;
	}

	public boolean canGetToRoom(Room r)
	{
		String dir = "";
		for (Exit exit : this.exits)
		{
			dir = exit.getDir();
			try
			{	
				Room adjRoom = this.leaveBy(dir);
				if (adjRoom != null && adjRoom == r)
				{
					return true;
				}
			}
			catch (LockedException e)
			{
			}

		}
		return false;
	}

	public ArrayList<Room> getAllAdjacentRooms()
	{
		ArrayList<Room> rtnMe = new ArrayList<Room>();
		for (Exit exit : this.exits)
		{
			rtnMe.add(exit.getDest());
		}
		return rtnMe;
	}

	ArrayList<Exit> getAllExits()
	{
		return this.exits;
	}
	
	public String dirToGetHereFromRoom(Room r)
	{
		for (Exit exit : r.getAllExits())
		{
			if (exit.getDest().equals(this))
			{
				return exit.getDir();
			}
		}
		return "unknown";
	}
}


