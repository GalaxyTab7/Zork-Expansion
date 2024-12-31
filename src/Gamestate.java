import java.util.*;
import java.io.*;
class Gamestate{

	private static Gamestate theInstance= null;
	private Room currentRoom;
	private HashSet<Room> visitedRooms;
	private Dungeon dungeon;
	private int health;
	private int score;
	private boolean playStatus;
	private int capacity;
	private int hydration;

	/**
	 * roomContents is a Hashtable used to refrence the contents of
	 * a room
	 * keys = Room instances
	 * values = HashSets of that Room instance's Items
	 */
	private Hashtable<Room,HashSet<Item>> roomContents;

	/**
	 * inventory is an array list containing all of the items 
	 * currently in the player's posession
	 */	
	private ArrayList<Item> inventory;


	public static synchronized Gamestate instance(){
		if (Gamestate.theInstance == null) {
			Gamestate.theInstance = new Gamestate();
		}
		return Gamestate.theInstance;
	}


	private Gamestate(){
		this.visitedRooms = new HashSet<Room>();
		this.currentRoom = currentRoom;
		this.dungeon = dungeon;
		this.inventory = new ArrayList<Item>();
		this.roomContents = new Hashtable<Room, HashSet<Item>>();
		this.health = 100;
		this.playStatus = true;
		this.score = 0;
		this.capacity = 40;
		this.hydration = 100;
	}


	//This is used for the die event. it returns the value of keepPlaying
	//If keepPlaying is false, the gameloop in Interpreter stops and the game ends. 
	public boolean getPlayStatus()
	{
		return this.playStatus;
	}

	//This is how the die event ends the game. It sets playStatus to false.
	public void setPlayStatus(boolean status)
	{
		this.playStatus = status;
	}

	public void initialize(Dungeon dungeon){
		this.dungeon = dungeon;

		//prevents duplicates
		Clock.instance().clearTimedObjects();

		for (TimedObject m : this.dungeon.getAllMonsters())
		{
			Clock.instance().addTimedObject(m);
		}

	}

	public void setCurrentRoom(Room room){
		this.currentRoom = room;
	}

	public Dungeon getDungeon(){
		return(this.dungeon);
	}

	public Room getCurrentRoom(){
		return(this.currentRoom);
	}

	public void setDungeon(Dungeon d){
		this.dungeon = d;
	}

	void visit(Room room){
		boolean result = false;
		if(visitedRooms.contains(room)){
			result = true;
		}
		if(result == false){
			visitedRooms.add(room);
		}
	}

	int getHealth(){
		return(this.health);
	}

	void changeHealth(int amount){
		this.health -= amount;
		if (this.health <= 0)
		{
			String[] temp = {};
			try
			{
				Event e = EventFactory.instance().makeEvent("die" , temp);
				e.trigger();
			}
			catch (Exception err)
			{
				System.out.println("what");
				err.printStackTrace();
			}
		}
	}

	void changeScore(int amount){
		this.score += amount;
	}

	void setScore(int score)
	{
		this.score = score;
	}

	void setHealth(int health)
	{
		this.health = health;
	}

	int getScore(){
		return(this.score);
	}

	public Hashtable<Room,HashSet<Item>> getAllRoomContents()
	{
		return this.roomContents;
	}

	public int getCapacity()
	{
		return this.capacity;
	}

    public void changeCapacity(int amount){
        this.capacity = amount;
    }

	boolean hasBeenVisited(Room room){
		boolean result = false;
		if(visitedRooms.contains(room)){
			result = true;
		}else{
			result = false;
		}
		return(result);
	}

	void store(String saveName) throws Exception{

		if (!(saveName.substring(saveName.length() - 4, saveName.length()).equals(".sav")))
		{
			saveName = saveName + ".sav";
		}

		PrintWriter pw = new PrintWriter(new FileWriter("../files/" + saveName));

		pw.println("Zork++ save data");
		pw.println("Dungeon file: " + Gamestate.instance().getDungeon().getFilename());
		pw.println("Room states:");

		ArrayList<Monster> allMonsters = Gamestate.instance().getDungeon().getAllMonsters();

		Hashtable<String , Room> allRooms = Gamestate.instance().getDungeon().getAllRooms();
		Enumeration<String> keys = allRooms.keys();

		while (keys.hasMoreElements())
		{
			String key = keys.nextElement();
			Room r = allRooms.get(key);
			ArrayList<Monster> hasMonsters = new ArrayList<Monster>();
			ArrayList<Exit> hasLockedExits = new ArrayList<Exit>();
			for (Exit e : r.getAllExits())
			{
				if (e.getIsLocked())
				{
					hasLockedExits.add(e);
				}
			}
			for (Monster m : allMonsters)
			{
				if (m.getCurrentRoom().equals(r))
				{
					hasMonsters.add(m);
				}
			}

			if (((this.roomContents.get(r) != null) && (this.roomContents.get(r).size() != 0)) || hasMonsters.size() != 0 || hasLockedExits.size() != 0 || r.getIsFillable()) 
			{
				pw.println(r.getName() + ":");

				boolean visited = this.hasBeenVisited(r);
				if (visited)
				{
					pw.println("beenHere=true");
				}
				else
				{
					pw.println("beenHere=false");
				}

				String printMe = "Contents: ";
				HashSet<Item> rStuff = this.roomContents.get(r);
				if (rStuff != null)
				{
					Enumeration<Item> stuff = Collections.enumeration(rStuff);
					while (stuff.hasMoreElements())
					{
						printMe += stuff.nextElement().getPrimaryName() + ",";
					}
					printMe = printMe.substring(0 , printMe.length() - 1);
				}	
				if (hasMonsters.size() != 0)
				{
					if (rStuff == null)
					{
						printMe += "Monsters[";
					}
					else
					{
						printMe += ",Monsters[";
					}
					for (Monster m : hasMonsters)
					{
						printMe+= m.getID() + ",";
					}
					printMe = printMe.substring(0 , printMe.length() - 1);
					printMe += "]";
				}

				if ((rStuff != null || hasMonsters.size() != 0) && printMe.length() > 11)
				{
					pw.println(printMe);
				}

				printMe = "lockedExits:";
				for (Exit e : hasLockedExits)
				{
					printMe += e.getDir() + ",";
				}
				printMe = printMe.substring(0 , printMe.length()-1);
				if (printMe.length() > 12)
				{
					pw.println(printMe);
				}


				if (r.getIsFillable())
				{
					printMe = "Fillable:";
					for (String type : r.getLiquids().keySet())
					{
						printMe += type + "[" + r.getLiquids().get(type) + "]" + ",";
					}
					if (printMe.length() > 9)
					{
						printMe = printMe.substring(0 , printMe.length() - 1);
						pw.println(printMe);
					}
				}
				pw.println("---");
			}

			else if (this.hasBeenVisited(r))
			{
				pw.println(r.getName() + ":");
				pw.println("beenHere=true");
				pw.println("---");
			}
		}
		pw.println("===");

		pw.println("Adventurer:");
		pw.println("Current room: " + this.currentRoom.getName());

		String printMe = "Inventory: ";

		for (Item i : this.inventory)
		{
			printMe += i.getPrimaryName() + ",";
		}
		printMe = printMe.substring(0 , printMe.length() - 1); // get rid of last ,

		pw.println(printMe);
		pw.println("Hydration: " + this.hydration);
		pw.println("Health: " + this.health);
		pw.println("Capacity: " + this.capacity);
		pw.println("Score: " + this.score);
		pw.println("Time: " + Clock.instance().getTime());
		
		String morePrint = "";
		for (String name : Gamestate.instance().getDungeon().getAllItems().keySet())
		{
			Item i = Gamestate.instance().getDungeon().getAllItems().get(name);
			morePrint+= i.getPrimaryName() + "[" + i.getCurrentFill() + "]" + ",";
		}
		pw.println(morePrint.substring(0 , morePrint.length() - 1));
		morePrint = "";
		if ((Gamestate.instance().getDungeon().getRoom("Car") != null))
		{
			morePrint += Gamestate.instance().getDungeon().getRoom("Car").getLiquids().get("gas");
			pw.println(morePrint);
		}
		pw.flush();
		pw.close();
	}

	void restore(String filename) throws Exception{
		if (!(filename.substring(0,1).equals("/")))
		{
			filename = "../files/" + filename;
		}
		Scanner scan = new Scanner(new FileReader(filename));

		String version = scan.nextLine();
		if(!version.equals("Zork III save data") && !version.equals("Zork++ save data")){
			throw new IllegalSaveFormatException();
		}
		String file = scan.nextLine();
		file = file.substring(14);
		Dungeon d = new Dungeon(file);
		Gamestate.instance().initialize(d);

		//This part clears the roomContents hashtable to get rid of items in all rooms
		Enumeration<Room> keys = this.roomContents.keys();
		while (keys.hasMoreElements())
		{
			Room key = keys.nextElement();
			roomContents.get(key).clear();
		}

		if (!scan.nextLine().equals("Room states:"))
		{
			throw new IllegalSaveFormatException();
		}

		String fl = scan.nextLine().trim();
		while(!fl.equals("==="))
		{
			fl = fl.substring(0,fl.length()-1);
			Room room = Gamestate.instance().getDungeon().getRoom(fl);

			//unlock all exits
			for (Exit e : room.getAllExits())
			{
				e.setIsLocked(false);
			}

			//clear liquids 
			room.getLiquids().clear();	

			String temp = scan.nextLine();
			temp = temp.substring(temp.indexOf("=") + 1 , temp.length());
			boolean hasVisited = Boolean.parseBoolean(temp); 

			if (hasVisited)
			{
				Gamestate.instance().visit(room);
			}

			String roomStuff = scan.nextLine();

			if (roomStuff.indexOf(":") != -1 && roomStuff.substring(0,9).equals("Contents:"))
			{
				roomStuff = roomStuff.substring(10 , roomStuff.length());
				roomStuff += ",";

				while (roomStuff.indexOf(",") != -1)
				{
					String ItemName = roomStuff.substring(0 , roomStuff.indexOf(","));
					if (ItemName.indexOf("[") != -1)
					{
						if (!(ItemName.substring(0 , ItemName.indexOf("[")).equals("Monsters")))
						{
							System.out.println("Bad monster format");
							throw new IllegalSaveFormatException();
						}
						String[] monsters = ItemName.substring(ItemName.indexOf("[") + 1 , ItemName.indexOf("]")).split(",");
						for (String id : monsters)
						{
							for (Monster m : this.getDungeon().getAllMonsters())
							{
								if (m.getID() == Integer.parseInt(id))
								{
									m.setCurrentRoom(room);
								}
							}
						}
						//This one break is probably the most important line of code in all of Gamestate
						break;
					}
					else
					{
						Item i = Gamestate.instance().getDungeon().getItem(ItemName);
						if (i == null)
						{
							System.out.println("null Item detected");
							throw new IllegalSaveFormatException();
						}
						Gamestate.instance().addItemToRoom(i,room);
						roomStuff = roomStuff.substring(roomStuff.indexOf(",") + 1 , roomStuff.length());
					}
				}
				roomStuff = scan.nextLine();
			}
			if (roomStuff.indexOf(":") != -1 && roomStuff.substring(0,12).equals("lockedExits:"))
			{
				String[] directions = roomStuff.substring(roomStuff.indexOf(":") + 1 , roomStuff.length()).split(",");
				for (String direction : directions)
				{
					room.getExit(direction).setIsLocked(true);
				}
				roomStuff = scan.nextLine();
			}
			if (roomStuff.indexOf(":") != -1 && roomStuff.substring(0,9).equals("Fillable:"))
			{
				room.setIsFillable(true);
				String[] liquidInfo = roomStuff.substring(9 , roomStuff.length()).split(",");
				for (String info : liquidInfo)
				{
					String type = info.substring(0 , info.indexOf("["));
					String amount = info.substring(info.indexOf("[") + 1 , info.indexOf("]"));
					Integer amountD = Integer.parseInt(amount);
					room.getLiquids().put(type , amountD);
				}
				scan.nextLine();
			}	
			fl = scan.nextLine();      
		} 

		if (!(scan.nextLine().equals("Adventurer:")))
		{
			throw new IllegalSaveFormatException();
		}

		String curRoom = scan.nextLine();
		curRoom = (curRoom.substring(14)); 
		Gamestate.instance().setCurrentRoom(Gamestate.instance().getDungeon().getRoom(curRoom));

		String inv = scan.nextLine();

		if (inv.length() > 10)
		{

			inv = inv.substring(inv.indexOf(" ") + 1 , inv.length());
			inv += ",";

			while (inv.indexOf(",") != -1)
			{
				String itemName = inv.substring(0 , inv.indexOf(","));
				Item i = Gamestate.instance().getDungeon().getItem(itemName);
				Gamestate.instance().addToInventory(i);
				inv = inv.substring(inv.indexOf(",") + 1 , inv.length());
			}
		}

		try
		{
			String hydrationStr = scan.nextLine();
			hydrationStr = hydrationStr.substring(hydrationStr.indexOf(":") + 2 , hydrationStr.length());

			String healthStr = scan.nextLine();
			healthStr = healthStr.substring(healthStr.indexOf(":") + 2 , healthStr.length());

			String capacityStr = scan.nextLine();
			capacityStr = capacityStr.substring(capacityStr.indexOf(":") + 2 , capacityStr.length());

			String scoreStr = scan.nextLine();
			scoreStr = scoreStr.substring(scoreStr.indexOf(":") + 2 , scoreStr.length());

			String timeStr = scan.nextLine();
			timeStr = timeStr.substring(timeStr.indexOf(":") + 2 , timeStr.length());

			int scoreInt = Integer.valueOf(scoreStr);
			int healthInt = Integer.valueOf(healthStr);
			int hydrationInt = Integer.valueOf(hydrationStr);
			int capacityInt = Integer.valueOf(capacityStr);
			int timeInt = Integer.valueOf(timeStr);

			Gamestate.instance().setHealth(healthInt);
			Gamestate.instance().setScore(scoreInt);
			Gamestate.instance().setCapacity(capacityInt);
			Gamestate.instance().setHydration(hydrationInt);
			Clock.instance().setTime(timeInt);
		}
		catch (Exception e)
		{
			throw new IllegalSaveFormatException();
		}

		//As the person who wrote this code, it is some of the most shit ass garbage I have ever written. It is 2 am though and I could not
		//care less. Garrett.
		String dumbShit = scan.nextLine();
		String[] fillItemInfo = dumbShit.split(",");
		for (String ItemInfo : fillItemInfo)
		{
			String itemName = ItemInfo.substring(0,ItemInfo.indexOf("["));
			String curFill = ItemInfo.substring(ItemInfo.indexOf("[") + 1 , ItemInfo.indexOf("]"));
			Gamestate.instance().getDungeon().getItem(itemName).setCurrentFill(Double.parseDouble(curFill));
		}
		if (Gamestate.instance().getDungeon().getRoom("Car") != null && scan.hasNextLine())
		{
			String evenWorse = scan.nextLine();
			Gamestate.instance().getDungeon().getRoom("Car").getLiquids().replace("gas" , Integer.parseInt(evenWorse));
		}
	}

	public void setCapacity(int i)
	{
		this.capacity = i;
	}

    public int getHydration(){
        return this.hydration;
    }

	public void setHydration(int i)
	{
		this.hydration = i;
	}

    public void changeHydration(int amount){
        if(amount == 111){
            this.hydration = 100; //max hydration level
        }else{
            this.hydration += amount;
            if(this.hydration > 100){
                this.hydration = 100;
            }else if (this.hydration < 0) {
                this.hydration = 0;
            }
        }
    }
	//add item to room
	void addItemToRoom(Item i, Room r){
		if (this.roomContents.get(r) == null)
		{
			this.roomContents.put(r , new HashSet<Item>());
		}

		this.roomContents.get(r).add(i);
	}

	//remove item from room
	void removeItemFromRoom(Item i , Room r){
		this.roomContents.get(r).remove(i);
	}

	//add inventory
	void addToInventory(Item i){
		this.inventory.add(i);
	}

	//get inventory arrayList
	ArrayList<Item> getInventory(){
		return this.inventory;
	}

	//remove item from inventory
	void removeFromInventory(Item i){
		this.inventory.remove(i);
	}

	//returns item from inventory or current room (if any)
	Item getItemInVicinityNamed(String name) throws NoItemException{
		try{
			//check inventory for item
			try
			{
				Item inInv = getItemFromInventoryNamed(name);
				return inInv;
			}
			catch (NoItemException e)
			{
			}

			HashSet<Item> stuffInRoom = this.roomContents.get(this.currentRoom);
			Enumeration<Item> stuff = Collections.enumeration(stuffInRoom);
			Item inRoom = null;

			while (stuff.hasMoreElements())
			{
				Item i = stuff.nextElement();
				if (i.goesBy(name))
				{
					inRoom = i;
				}
			}

			if (inRoom == null)
			{
				throw new NoItemException();
			}

			return inRoom;
		}
		catch(NoItemException e){
			throw e;
		}
	}

	Item getItemFromInventoryNamed(String name) throws NoItemException{
		//iterate over inventory looking for name
		for (Item i : this.inventory){
			if(i.goesBy(name)){
				return i;
			}
		}
		//if we got here then the item isn't in the inventory
		throw new NoItemException();
	}
	//returns HashSet of items from room r
	HashSet<Item> getItemsInRoom(Room r){

		if (this.roomContents.get(r) == null)
		{
			this.roomContents.put(r , new HashSet<Item>());
		}
		return this.roomContents.get(r);
	}
   
}
