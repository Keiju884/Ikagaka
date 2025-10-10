package bean;

import java.awt.Rectangle;
import java.text.MessageFormat;

public class Vector2Int
{
	public int x;

	public int y;

	public Vector2Int(Rectangle rectangle)
	{
		set(rectangle.x, rectangle.y);
	}

	public Vector2Int(int x, int y)
	{
		set(x, y);
	}

	public Vector2Int(Vector2Int vector)
	{
		set(vector.x, vector.y);
	}

	public Vector2Int()
	{
		set(0, 0);
	}

	public Vector2 vector2(Vector2 vector2)
	{
		return new Vector2(x, y);
	}

	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public String toString()
	{
		return MessageFormat.format("[{0}:{1}]", x, y);
	}

	public void set(Vector2Int vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}
}
