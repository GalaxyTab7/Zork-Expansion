import java.util.*;
import java.io.*;
public class Dungeon{

	private String title;
	private String filename;
	private Room entry;
	private Hashtable<String,Room> rooms;
	
	//This Arraylist contains all monsters in the dungeon
	private ArrayList<Monster> monsters;

	//This Hashtable contains all Items in the dungeon
	private Hashtable<String , Item> items; 

	public Dungeon(Room entry, String title){
		this.title = title;
		this.entry = entry;
		this.monsters = new ArrayList<Monster>();
	}

	public ArrayList<Monster> getAllMonsters()
	{
		return this.monsters;
	}

	public Dungeon(String filename) throws Exception{
		Gamestate.instance().setDungeon(this);
		this.rooms = new Hashtable<String,Room>();
		this.items = new Hashtable<String , Item>();
		this.monsters = new ArrayList<Monster>();

		if ((!(filename.substring(0,1).equals("/"))) && (!(filename.substring(0 , 8).equals("../files"))))
		{
			filename = "../files/" + filename;	
		}
		Scanner scan = new Scanner(new FileReader(filename));
		this.filename = filename;
		

		this.title = scan.nextLine().trim();
		String version = scan.nextLine();
		if(!version.trim().equals("Zork III") && !version.trim().equals("Zork ++") && !version.trim().equals("ZorkIII") && !version.trim().equals("Zork++")){
			System.out.println("Bad Zork Version");
			throw new IllegalDungeonFormatException();
		}
		//Make this check to make sure it is ===
		String x = scan.nextLine().trim();
		if (!(x.equals("===")))
		{
			System.out.println("Bad end section delimiter should be (===)");
			throw new IllegalDungeonFormatException();
		}

		//Make this check to make sure it is Items:
		if (!(scan.nextLine().trim().equals("Items:")))
		{
			System.out.println("Problem with Items: formatting");
			throw new IllegalDungeonFormatException();
		}

		//Do Item hydration here
		//Making a new item from a .zork file means:
		//instanciating it
		//adding it to Dungoen Items
		//adding it to Gamestate roomContents
			
		try
		{
			while(true)
			{
				Item i = new Item(scan);
				this.add(i);
			}
		}
		catch (BadEventConstructionException err)
		{
			System.out.println("Problem with Items: construction");
			err.printStackTrace();
			throw new IllegalDungeonFormatException();
		}
		catch (NoItemException e)
		{
		}

		//make sure to skip/check for Rooms: line before continuing
		if (!(scan.nextLine().trim().equals("Rooms:")))
		{
			System.out.println("Problem with Room: formatting");
			throw new IllegalDungeonFormatException();
		}
		this.entry = new Room(scan);
		Gamestate.instance().setCurrentRoom(entry);
		rooms.put(this.entry.getName(),entry);
		//Make Sure to edit Room.java so that room contents are put in the GameState
		//HashTable roomContents
		try {
			while(true){
				Room r = new Room(scan);
				rooms.put(r.getName(),r);
			}
		} catch (NoRoomException e){
		}
		catch (Exception err)
		{
			throw new IllegalDungeonFormatException();
		}	

		if (!(scan.nextLine().trim().equals("Exits:")))
		{
			System.out.println("Problem with Exits: formatting");
			throw new IllegalDungeonFormatException();
		}

		try{
			while(true){
				Exit e = new Exit(scan);
				e.getSrc().addExit(e);
			}
		}catch (NoExitException e){
		} 
		catch (Exception crap)
		{
			System.out.println("Problem with Exits: null field");
			throw new IllegalDungeonFormatException();
		}

		if (scan.hasNext() && scan.nextLine().trim().equals("Monsters:"))
		{
			try
			{
				while (true)
				{
					Monster m = new Monster(scan);
					this.monsters.add(m);
				}
			}
			catch (NoMoreMonsters done)
			{
			}
			catch(BadMonsterConstruction e)
			{
				System.out.println("Problem with Monster: formatting");
            			throw new IllegalDungeonFormatException();
			}
		}
	}

	public void add(Room room){
		rooms.put(room.getName(), room);
	}

	public Room getRoom(String roomName){
		return(rooms.get(roomName));
	}

	public Room getEntry(){
		return(this.entry);
	}

	public String getTitle(){
		return(this.title);
	}

	public String getFilename(){
		return(this.filename);
	}

	public void add(Item i)
	{
		this.items.put(i.getPrimaryName() , i);
	}

	Hashtable<String , Item> getAllItems()
	{
		return this.items;
	}
	
	public Item getItem(String itemName)
	{
		for (String name : this.items.keySet())
		{
			Item i = items.get(name);
			if (i.goesBy(itemName))
			{
				return i;
			}
		}
		return this.items.get(itemName);
	}

	//Use this for persistance only. 
	Hashtable<String , Room> getAllRooms()
	{
		return this.rooms;
	}

	public void removeItem(Item i)
	{
		this.items.remove(i.getPrimaryName());
	}
}
