package bean;

import java.text.MessageFormat;

public class Vector2
{
	public double x;

	public double y;

	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public void set(double x,double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return MessageFormat.format("[{0},{1}]", x,y);
	}
	
	public Vector2Int vector2Int()
	{
		return new Vector2Int((int)x, (int)y);
	}
}
