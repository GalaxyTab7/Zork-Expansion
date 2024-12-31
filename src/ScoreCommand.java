class ScoreCommand extends Command{

    String execute(){
  
        int score = Gamestate.instance().getScore();
        String message = "";

        if(score <= 0){
            message = ("Roulette Recruit");
        }else if(score <= 25 && score >= 10){
            message = ("Blackjack Beginner");
        }else if(score <= 50 && score >= 26){
            message = ("Poker Proficient");
        }else if(score <= 75 && score >= 51){
            message = ("Casino Champion");
        }else if(score >= 76){
            message = ("Gambling God");
        }
        
        return("Your current score is: " + score + "\n Rank: " + message);
    }


}
