class UnknownCommand extends Command{

	private String bogusCommandString;
	private boolean fillInfo;


	UnknownCommand(String s){
		this.fillInfo = false;
		this.bogusCommandString = s;
	}
	UnknownCommand(boolean b , String s)
	{
		this.fillInfo = b;
		this.bogusCommandString = s;
	}

	String execute(){
		if (this.fillInfo)
		{
			return this.bogusCommandString;
		}
		return("I do not recognize the command " + this.bogusCommandString); 
	}

}
