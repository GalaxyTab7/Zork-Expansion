import java.util.*;

class Monster implements TimedObject
{
	private Room playersLastRoom;
	private Room currentRoom;
	private Path path;
	private int turnTime;
	private int nextTurnTime;
	private String attackDamage;
	private String type;
	private String desc;
	private int stuckTime;
	private int id;
	private static Integer idGenerator = 0;

	public Monster(String type , String desc , String dmg, int turnTime, int pauseTime , String room)
	{
		this.playersLastRoom = Gamestate.instance().getCurrentRoom();
		this.type = type;
		this.desc = desc;
		this.attackDamage = dmg;
		this.turnTime = turnTime;
		this.nextTurnTime = pauseTime + turnTime;
		this.currentRoom = Gamestate.instance().getDungeon().getRoom(room);

		//all hail the glorious path code
		this.path = new Path(currentRoom , this.playersLastRoom);
		this.stuckTime = 0;

		//give monster an Id 
		this.id = Monster.generateID();
	}	

	private static int generateID()
	{
		int temp = Monster.idGenerator.intValue();
		Monster.idGenerator += 1;
		return temp;
		
	}

	public void setCurrentRoom(Room r)
	{
		this.currentRoom = r;
	}
	public int getID()
	{
		return this.id;
	}

	public Monster(Scanner scan) throws BadMonsterConstruction,NoMoreMonsters
	{
		this.playersLastRoom = Gamestate.instance().getCurrentRoom();

		String line = "";
		line = scan.nextLine();

		//end monster construction 
		if (line.equals("==="))
		{
			throw new NoMoreMonsters();
		}
	
		if (! line.substring(0 , 5).equals("type:") )
		{
			System.out.println("Prob at Monster " + this.type + " type:");
			throw new BadMonsterConstruction();
		}
		this.type = line.substring(5 , line.length());

		line = scan.nextLine();
		if (! (line.substring(0,5).equals("desc:")))
		{
			System.out.println("Prob at Monster " + this.type + " desc:");
			throw new BadMonsterConstruction();
		}
		this.desc = line.substring(5 , line.length());	

		line = scan.nextLine();
		if (! line.substring(0,4).equals("dmg:"))
		{
			System.out.println("Prob at Monster " + this.type + " dmg:");
			throw new BadMonsterConstruction();
		}
		
		this.attackDamage = line.substring(4,line.length());

		line = scan.nextLine();
		if (! line.substring(0,9).equals("turnTime:"))
		{
			System.out.println("Prob at Monster " + this.type + " turnTime:");
			throw new BadMonsterConstruction();
		}
		this.turnTime = Integer.valueOf(line.substring(9,line.length()));

		line = scan.nextLine();
		if (! line.substring(0,10).equals("pauseTime:"))
		{
			System.out.println("Prob at Monster " + this.type + " pauseTime:");
			throw new BadMonsterConstruction();
		}
		this.nextTurnTime = this.turnTime + Integer.valueOf(line.substring(10,line.length()));

		line = scan.nextLine();
		if (! line.substring(0,12).equals("currentRoom:"))
		{
			System.out.println("Prob at Monster " + this.type + " currentRoom:");
			throw new BadMonsterConstruction();
		}

		this.currentRoom = Gamestate.instance().getDungeon().getRoom(line.substring(12,line.length()));


		if (this.currentRoom == null)
		{
			System.out.println("Prob at Monster " + this.type + " null room");
			throw new BadMonsterConstruction();
		}
		
                //all hail the glorious path code
                this.path = new Path(this.currentRoom , this.playersLastRoom);
                this.stuckTime = 0;		

		
		//skip over the ---
		line = scan.nextLine();
		if (! line.equals("---"))
		{
			System.out.println("failed to add end monster delimiter at "  + this.type);
			throw new BadMonsterConstruction();
		}

		//generate ID
		this.id = Monster.generateID();
	}

	public Room getCurrentRoom()
	{
		return this.currentRoom;
	}
	public String describePositionRelvativeToPlayer()
	{
		String rtnMe = "You sense a " + this.type + " about ";
		rtnMe += this.path.getDistanceFromPlayer() + " rooms away in the ";
		rtnMe += this.path.getDirFromPlayer() + " direction.";
		return rtnMe;
	}

	public String conditionalDescriptionRelativeToPlayer()
	{
		try{
			if (Integer.valueOf(this.path.getDistanceFromPlayer()) <= 7)
			{
				String rtnMe = "You sense a " + this.type + " about ";
				rtnMe += this.path.getDistanceFromPlayer() + " rooms away in the ";
				rtnMe += this.path.getDirFromPlayer() + " direction.";
				return rtnMe;
			}
			else
			{
				return "";
			}
		}

		catch (Exception e)
		{
			String rtnMe = "You sense a " + this.type + " about ";
			rtnMe += this.path.getDistanceFromPlayer() + " rooms away in the ";
			rtnMe += this.path.getDirFromPlayer() + " direction.";	
			return rtnMe;
		}

	}

	public String describeMonster()
	{
		return "A " + this.type + " that looks like " + this.desc;
	}
	public boolean isTime(int time)
	{
		if (this.nextTurnTime <= time)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//for testing
	public Path getPath()
	{
		return this.path;
	}

	public boolean checkStuckStatus()
	{
		return this.path.getStuck();
	}
	public int checkStuckTime()
	{
		return this.stuckTime;
	}

	public String takeTurn()
	{
		if (this.path.getStuck())
		{
			this.path = new Path(this.currentRoom , this.playersLastRoom);
			if (this.path.getStuck() && this.stuckTime >= 5)
			{
				//Teleporting monster to new random room 
				Random random = new Random();

				//getting all the rooms
				Hashtable<String, Room> r = Gamestate.instance().getDungeon().getAllRooms();

				//Seperating the hashtable into an array list of only the values
				ArrayList<Room> rooms = new ArrayList<Room>(r.values());

				int randNum = 0;

				//Makes sure you don't teleport into the room you're already in
				while(true)
				{
					randNum = random.nextInt(rooms.size());

					if(rooms.get(randNum).equals(this.currentRoom) || rooms.get(randNum).equals(this.playersLastRoom))
					{      
					}
					else
					{
						break;
					}
				}

				this.currentRoom = rooms.get(randNum);
				this.path = new Path(this.currentRoom , this.playersLastRoom);

				//vital to update monster turn time before returning
				this.nextTurnTime = Clock.instance().getTime() + this.turnTime; 
				//Here it is also important to update the stuck status of the monster
				this.stuckTime = 0;	
				return "\nYou hear the " + this.type + " free itself and run ...... somewhere.";
			}

			//vital to update monster turn time before returning
			this.stuckTime += 1;
			this.nextTurnTime = Clock.instance().getTime() + this.turnTime; 
			return "You think the " + this.type + " is stuck somehow. Who knows how long that will last.";
		}

		Room pr = Gamestate.instance().getCurrentRoom();
		String rtnMe = "\n";

		if (!(pr.equals(this.playersLastRoom)))
		{
			this.path = new Path(this.currentRoom , pr);
			this.playersLastRoom = pr;	
		}

		if (pr != this.currentRoom)
		{
			this.path = new Path(this.currentRoom , this.playersLastRoom);
			Room nextRoom = this.path.getNextRoomInPath(); 
			if (nextRoom != null)
			{	
				this.currentRoom = nextRoom;	
				rtnMe += "You hear the " + this.type + " moving.\n";
			}
		}
		if (!(this.currentRoom.equals(pr)))
		{	
			rtnMe += this.conditionalDescriptionRelativeToPlayer();
		}

		if (this.currentRoom.equals(pr))
		{
			//damages player when monster enters room
			try
			{
				String[] attrs = {this.attackDamage};
				Event e = EventFactory.instance().makeEvent("wound",attrs);
				e.trigger();
			}
			catch (Exception fuck)
			{
				ArrayList<String> shit = new ArrayList<String>();
				shit.get(1000);
			}
			rtnMe += this.describeMonster() + " attacked you doing " + this.attackDamage + " damage!";
			rtnMe += "\nThe horrible " + this.type + " then disappeared from sight";

			//Teleporting monster to new random room 
			Random random = new Random();

			//getting all the rooms
			Hashtable<String, Room> r = Gamestate.instance().getDungeon().getAllRooms();

			//Seperating the hashtable into an array list of only the values
			ArrayList<Room> rooms = new ArrayList<Room>(r.values());

			int randNum = 0;

			//Makes sure you don't teleport into the room you're already in
			while(true)
			{
				randNum = random.nextInt(rooms.size());

				if(rooms.get(randNum).equals(this.currentRoom) || rooms.get(randNum).equals(this.playersLastRoom))
				{      
				}
				else
				{
					break;
				}
			}

			this.currentRoom = rooms.get(randNum); 
			this.path = new Path(this.currentRoom , this.playersLastRoom);
		}

		//vital to update monster turn time before returning.
		this.nextTurnTime = Clock.instance().getTime() + this.turnTime; 
		return rtnMe;
	}
}
