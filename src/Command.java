abstract class Command{
   abstract String execute();

   String changeTime(int amount)
   {
	return Clock.instance().updateTime(amount);
   }
}
