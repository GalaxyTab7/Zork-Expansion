public class LockedException extends Exception
{
	public LockedException(String dir)
	{
		super("The " + dir + " exit is locked!");
	}
}
