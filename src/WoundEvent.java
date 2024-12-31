class WoundEvent implements Event{

    private int amount;
    
    public WoundEvent(String[] attributes){
        this.amount = Integer.parseInt(attributes[0]);
    }

    public void trigger(){
        Gamestate gs = Gamestate.instance();
        //health is decreased by the wound amount
        gs.changeHealth(amount);

    }



}
