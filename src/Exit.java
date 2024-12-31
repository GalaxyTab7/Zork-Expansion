import java.util.*;
public class Exit{

	private String dir;
	Room src;
	Room dest;
	private boolean isLocked;

	public Exit(String dir, Room src, Room dest){
		this.dir = dir;
		this.src = src;
		this.dest = dest;    
		this.isLocked = false;
	}

	public Exit(Scanner s) throws NoExitException,Exception
	{	
		String first = s.nextLine().trim();

		if(first.equals("===")){
			throw new NoExitException();
		}

		this.src = Gamestate.instance().getDungeon().getRoom(first);

		//direction and if it is locked		
		String line = s.nextLine();
		if (line.indexOf(":") != -1)
		{
			this.isLocked = true;
			this.dir = line.substring(0 , line.indexOf(":"));
		}
		else
		{
			this.isLocked = false;
			this.dir = line;
		}
		
		this.dest = Gamestate.instance().getDungeon().getRoom(s.nextLine().trim());
		s.nextLine().trim();
		if (this.dir == null || this.dest == null || this.src == null)
		{
			throw new Exception();
		}
	}

	String describe(){
		return("You can go " + this.dir + " from " + this.src.getName() + " to " + this.dest.getName()); 
	}

	public String getDir(){
		return(this.dir);
	}
	public Room getSrc(){
		return(this.src);
	}
	public Room getDest(){
		return(this.dest);   
	}
	public boolean getIsLocked()
	{
		return this.isLocked;
	}
	public void setIsLocked(boolean status)
	{
		this.isLocked = status;
	}
}
