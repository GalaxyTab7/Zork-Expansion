class ScoreEvent implements Event{
    private int score;
    
    //there is an expectation that [0] contains a string that represents the score to be added
    public ScoreEvent(String[] attributes){
        this.score = Integer.parseInt(attributes[0]);
    }
        
    public void trigger(){
        Gamestate.instance().changeScore(score);
    }


}
