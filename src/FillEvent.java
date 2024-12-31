class FillEvent implements Event {
    private String itemName;
    private int amount;

    public FillEvent(String[] attrs) throws BadEventConstructionException {
        if (attrs.length < 2) {
            throw new BadEventConstructionException();
        }
        try {
            this.amount = Integer.parseInt(attrs[0]);
            this.itemName = attrs[1];
        } catch (NumberFormatException e) {
            throw new BadEventConstructionException();
        }
    }

    public void trigger() throws Exception,CannotFillException 
    {
	double amt = this.amount + 0.0;
	Item i = Gamestate.instance().getDungeon().getItem(this.itemName);
	if (i == null)
	{
		throw new Exception();
	}
	//make sure the item is actually fillable
	if (!(i.getIsFillable()) && amt < 0)
	{
		throw new CannotFillException("This item is not fillable!");
	}
	//For amounts less than zero which drain the item we check do make sure that the drain doesnt take it below 0
	if (amt < 0 && ((i.getCurrentFill() + amt < 0) || i.getCurrentFill() == 0))
	{
		throw new CannotFillException("There isn't enough liquid in the item!");
	}
	//For amounts more than zero which fill the item we check to make sure that the fill doesnt move it beyond max fill
	if (amt > 0 && i.getMaxFill() < amt + i.getCurrentFill())
	{
		throw new CannotFillException("Woah there it's overflowing! you try to stop it but it's filled up where it was before");
	}
		
	i.setCurrentFill(i.getCurrentFill() + amt);
	
    }
}
