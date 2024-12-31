import java.util.*;
class EventFactory {

	private static EventFactory theInstance = null;

	public static EventFactory instance(){
		if(EventFactory.theInstance == null){
			EventFactory.theInstance = new EventFactory();
		}
		return EventFactory.theInstance;
	}

	private EventFactory(){
	}


	public Event makeEvent(String type, String[] attributes) throws BadEventConstructionException , IllegalArgumentException
	{
		Event event = null;
		//The 'attributes' array contains a list of string values that represent the
		//parameters needed to initialize an Event. Each event type interprets the attributes differently
		//based on its specific requirements.

		//For example "improvebag" might use attributes[0] as the amount to increase bag capacity.
		//"teleport" might use attributes[0] as the target room name.
		//Note that if the event is associated with an ItemSpecificCommand, then the event will be hydrated 
		//in the Item constructor and the item constructing the event will always be the last value in the attributes array.
		//For other events that are not always associated with an ItemSpecificCommand, these do not need to be hydrated and can be hardcoded
		//to activate when a certain condition is met. For example, a DieEvent is created and triggered whenever the player's
		//health dips below 0. 


		//We can delete this later its just so we can comment and uncomment code 
		//without having to chane the ifs and else ifs and what not
		if (false)
		{
		}
		
		  else if(type.equals("improvebag"))
		  {
		 	  event = new ImproveBagEvent(attributes); 
		  }
		/**
		  else if(type.equals("overweightbag")){
		  event = new OverweightBagEvent(attributes);

		 **/
		else if(type.equals("transform"))
		{

			try
			{
				event = new TransformEvent(attributes);
			}
			catch (Exception e)
			{
				throw new BadEventConstructionException();
			}
		}

		else if(type.equals("score")){
			event = new ScoreEvent(attributes);
		}

		else if(type.equals("teleport")){
			event = new TeleportEvent();

		}
		else if(type.equals("win")){
			event = new WinEvent(attributes);
		}
		else if(type.equals("disappear")){
			try{
				event = new DisappearEvent(attributes);
			} catch (Exception e){
				throw new BadEventConstructionException();
			}
		}else if(type.equals("drop")){
			try{
				event = new DropEvent(attributes);
			} catch (Exception e){
				throw new BadEventConstructionException();
			}
		}
		else if(type.equals("wound")){
			event = new WoundEvent(attributes);
		}
		else if(type.equals("die")){
			try
			{
				event = new DieEvent(attributes);
			}
			catch (Exception e)
			{
				throw new BadEventConstructionException();
			}

		}
		//for both lock and unlock events, the first value of the attributes
		//array should be the src room and the second value should be an exit direction.
		else if(type.equals("lock")){
			try
			{
				event = new LockEvent(attributes);
			}
			catch (Exception e)
			{
				throw new BadEventConstructionException();
			}
		}else if(type.equals("unlock")){
			try
			{
				event = new UnlockEvent(attributes);
			}
			catch (Exception e)
			{
				throw new BadEventConstructionException();
			}
		}
		else if (type.equals("fill"))
		{
			try
			{
				event = new FillEvent(attributes);
			}
			catch (Exception e)
			{
				throw new BadEventConstructionException();
			}
		}
		else if (type.equals("hydrate"))
		{
			try
			{
				event = new HydrateEvent(attributes);
			}
			catch (Exception e)
			{
				throw new BadEventConstructionException();
			}
		}
		else{   
			throw new IllegalArgumentException("Unknown event type: " + type);
		}

		return event;
	}

}


