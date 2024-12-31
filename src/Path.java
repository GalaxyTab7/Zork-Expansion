import java.util.*;
class Path
{
	private String distance;
	private String dir;
	private static Hashtable<Room,ArrayList<Room>> theGrid = null;
	private ArrayList<Room> thePath;
	private int pathPos;
	private boolean stuck;

	public Path(Room mr , Room pr)
	{
		this.stuck = false;
		this.pathPos = 0;
		if (Path.theGrid == null)
		{
			Path.buildTheGrid();
		}

		this.thePath = buildPath(mr,pr);
		this.dir ="";
		this.distance="";
		this.updateDistance();
		this.updateDir();
		if ( this.thePath == null || !(this.thePath.get(this.thePath.size() - 1).equals(pr)))
		{
			this.stuck = true;
		}
	}

	private void updateDistance()
	{
		if (this.thePath == null)
		{
			this.stuck = true;
			this.distance = "unknown";
			return;
		}
		if (!this.stuck && (Gamestate.instance().getCurrentRoom() != this.thePath.get(this.thePath.size() - 1)))
		{
			this.thePath = Path.buildPath(this.thePath.get(this.pathPos) , Gamestate.instance().getCurrentRoom());
		}
		if (this.thePath.indexOf(Gamestate.instance().getCurrentRoom()) == -1) //This should also never happen unless the monster is stuck
		{
			this.distance = "unknown";
			return;
		}
		this.distance = Integer.toString(this.thePath.size() - this.pathPos - 1);
		if (Integer.parseInt(this.distance) <= 0)
		{
			this.distance = "unknown";
		}
	}

	private void updateDir()
	{
		if (this.thePath == null)
		{
			this.stuck = true;
			this.dir = "unknown";
			return;
		}
		if (!this.stuck && (Gamestate.instance().getCurrentRoom() != this.thePath.get(this.thePath.size() - 1)))
		{
			this.thePath = Path.buildPath(this.thePath.get(this.pathPos - 1) , Gamestate.instance().getCurrentRoom());
		}
		if (this.thePath.size() <= 1) //This shouldnt happen its here just in case
		{
			this.dir = "unknown";
		}
		else
		{
			this.dir = this.thePath.get(thePath.size()-2).dirToGetHereFromRoom(Gamestate.instance().getCurrentRoom()); 
		}
	}
	public boolean getStuck()
	{
		return this.stuck;
	}
	public String toString()
	{
		if (this.thePath == null)
		{
			this.stuck = true;
			return "null ";
		}
		String rtnMe = "";
		for (Room r : this.thePath)
		{
			rtnMe += r.getName() + ", ";
		}
		return rtnMe;
	}

	public String getDistanceFromPlayer()
	{
		this.updateDistance();
		return this.distance;
	}

	public String getDirFromPlayer()
	{
		this.updateDir();
		return this.dir;
	}

	public Room getNextRoomInPath()
	{
		if (this.thePath == null)
		{
			this.stuck = true;
			return null;
		}
		this.pathPos += 1;
		Room r = this.thePath.get(this.pathPos);
		return r;
	}

	public int getPathPos()
	{
		return this.pathPos;
	}
	private static void buildTheGrid()
	{
		//gets all the rooms in the dungeon
		Hashtable<String , Room> allRooms = Gamestate.instance().getDungeon().getAllRooms();
		Path.theGrid = new Hashtable<Room , ArrayList<Room>>();

		//builds out the grid.
		for (String key : allRooms.keySet())
		{
			Room r = allRooms.get(key);
			ArrayList<Room> adjacent = r.getAllAdjacentRooms();
			Path.theGrid.put(r,adjacent);
		}
	}

	private static ArrayList<Room> buildPath(Room mr , Room pr)
	{
		//set up stuff for bft
		ArrayList<Room> queue = new ArrayList<Room>();
		HashSet<Room> visited = new HashSet<Room>();
		ArrayList<ArrayList<Room>> paths = new ArrayList<ArrayList<Room>>();
		queue.add(mr);
		ArrayList<Room> temp = new ArrayList<Room>();
		temp.add(mr);
		paths.add(temp);

		//bft to build out the paths arraylist
		while (queue.size() != 0)
		{
			
			//deque first node and set it as visited
			//wait but now its a stack :-o
			Room curRoom = queue.get(0);
			queue.remove(0);
			visited.add(curRoom);
			
			if (curRoom == null)
			{
				break;
			}

			//I wrote this in a caffeine induced stuper I have no idea how it works but I sure hope it does tehe
			HashSet<ArrayList<Room>> removeFromPaths = new HashSet<ArrayList<Room>>();
			ArrayList<ArrayList<Room>> addToPaths = new ArrayList<ArrayList<Room>>();
			

			for (Room adj : theGrid.get(curRoom))
			{
				for (int i = 0 ; i < paths.size() ; i ++)
				{
					ArrayList<Room> path = paths.get(i);
					if (path.get(path.size() - 1).equals(curRoom))
					{
						if( curRoom.canGetToRoom(adj) && !(path.get(path.size()-1).equals(pr)))
						{
							ArrayList<Room> temp2 = new ArrayList<Room>();	
							for (Room r : path)
							{
								temp2.add(r);
							}
							addToPaths.add(temp2);
							removeFromPaths.add(path);
							temp2.add(adj);
						}
					}
				}
			}

			paths.removeAll(removeFromPaths);
			for (ArrayList<Room> path : addToPaths)
			{
				paths.add(path);
			}

			//enque all non-visited adjacent rooms
			for (Room r : theGrid.get(curRoom))
			{
				if (!(visited.contains(r)))
				{
					queue.add(r);
				}
			}
		}

		//search through paths to get the most optimal path to players current position
		int shortestPathLength = Integer.MAX_VALUE;
		ArrayList<Room> shortestPath = null;
		for (ArrayList<Room> path : paths)
		{
			if (path.get(path.size() - 1).equals(pr))
			{
				if (path.size() < shortestPathLength)
				{
					shortestPath = path;
					shortestPathLength = path.size();
				}
			}
		}

		//finally return the shortest path to player
		return shortestPath;
	}
}
