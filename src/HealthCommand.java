class HealthCommand extends Command{

    String execute(){
    
        int health = Gamestate.instance().getHealth();
        int hydration = Gamestate.instance().getHydration();
        String message = "";
        
        if(health >= 100){
            message = "You're in perfect shape.\n";
        }else if(health <= 99 && health >= 80){
            message = "You feel a little hot and shaken up.\n";
        }else if (health <= 79 && health >= 60){
            message = "The heat of the desert is getting to you, you feel eyes constantly watching you.\n";
        }else if (health <= 69 && health >= 40){
            message = "Your vision is blurred and you feel as dry as a raisin.\n";
        }else if (health <= 39 && health >= 1){
            message = "You struggle to move or think about anything other than your tired body.\n";
        }else if (health == 0){
		Gamestate.instance().setPlayStatus(false);
            message = "You have succumbed to the desert.\n"; 
        }

        if (hydration == 100){
            message += "You're fully hydrated.";
	    return message;
        }else if (hydration >= 80){
            message += "You're feeling pretty good on hydration, just a tad thirsty.";
	    return message;
        }else if (hydration >= 60){
            message += "You start to feel thirsty, all you can think about is a drink.";
	    return message;
        }else if (hydration >= 40){
            message += "You've become very thirsty, please find something to drink.";
	    return message;
        }else if (hydration >= 1){
            message += "You are severely dehydrated.";
	    return message;
        }

        return(message);
    }
}
