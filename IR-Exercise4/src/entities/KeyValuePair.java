package entities;

public class KeyValuePair<T, U> {
	
	private T key;
	private U value;
	
	public KeyValuePair(T key, U value)
	{
		this.key = key;
		this.value = value;
	}
	
	public KeyValuePair()
	{
		this.key = null;
		this.value = null;
	}
	
	public T getKey()
	{
		return this.key;
	}
	
	public U getValue()
	{
		return this.value;
	}
	
	public void setKey(T key)
	{
		this.key = key;
	}
	
	public void setValue(U value)
	{
		this.value = value;
	}

}
