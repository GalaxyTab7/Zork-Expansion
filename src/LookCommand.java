class LookCommand extends Command{
    
    String execute(){
       
        Room room = Gamestate.instance().getCurrentRoom();
        
        String rtnMe = (room.fullDesc());
	rtnMe += "\n";	
	for (Monster m : Gamestate.instance().getDungeon().getAllMonsters())
	{
		rtnMe += m.conditionalDescriptionRelativeToPlayer() + "\n";
	}	

	return rtnMe;
    }
}
