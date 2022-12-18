package utils;

// My favorite class.
public class Pair<T1, T2> {
	public T1 first;
	public T2 second;

	public Pair() {
		first = null;
		second = null;
	}
	
	public Pair(T1 firstInput, T2 secondInput)
	{
		first = firstInput;
		second = secondInput;
	}
}
