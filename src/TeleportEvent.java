import java.util.*;

class TeleportEvent implements Event{

	public void trigger(){

		Random random = new Random();

		Room curRoom = Gamestate.instance().getCurrentRoom();
		Hashtable<String, Room> r = Gamestate.instance().getDungeon().getAllRooms();
		//Seperating the hashtable into an array list of only the values
		ArrayList<Room> rooms = new ArrayList<Room>(r.values());

		int randNum = 0;
		boolean equal = true;

		//Makes sure you don't teleport into the room you're already in
		while(equal == true){
			randNum = random.nextInt(rooms.size());

			if(rooms.get(randNum).getName().equals(curRoom.getName())){
				equal = true;       
			}else{
				equal = false;
			}
		}
		//Teleports you into the room
		Gamestate.instance().setCurrentRoom(rooms.get(randNum));

	}
}
