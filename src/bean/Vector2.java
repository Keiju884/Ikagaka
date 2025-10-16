package bean;

import java.awt.Rectangle;
import java.text.MessageFormat;

public class Vector2
{
	public double x;

	public double y;

	public Vector2(Rectangle rectangle)
	{
		set(rectangle.x, rectangle.y);
	}

	public Vector2(double x, double y)
	{
		set(x, y);
	}

	public Vector2(Vector2 vector)
	{
		set(vector.x, vector.y);
	}

	public Vector2()
	{
		set(0.0, 0.0);
	}

	public Vector2Int vector2Int(Vector2Int vector2)
	{
		return new Vector2Int((int)x, (int)y);
	}

	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public void set(Vector2 vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
	
	public void add(Vector2 vec)
	{
		this.x += vec.x;
		this.y += vec.y;
	}
	
	public Vector2 addVector(Vector2 vec)
	{
		return new Vector2(this.x + vec.x,this.y + vec.y);
	}
	
	public Vector2 addVector(double x , double y)
	{
		return new Vector2(this.x + x,this.y + y);
	}
	

	public String toString()
	{
		return MessageFormat.format("[{0}:{1}]", x, y);
	}
}
