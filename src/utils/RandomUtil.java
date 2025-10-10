package utils;

import java.util.List;
import java.util.Random;

public final class RandomUtil
{
	private static Random rand;

	public final static Random random()
	{
		if(rand == null)
		{
			rand = new Random();
		}
		return rand;
	}
	
	public final static int rangePlus(int min, int max)
	{
		return random().nextInt(min,max +1);
	}
	
	public final static double rangePlus(double min,double max)
	{
		return random().nextDouble(min,max +1);
	}
	
	public final static int nextPlus(int num)
	{
		return random().nextInt(num +1);
	}
	
	public final static double nextPlus(double num)
	{
		return random().nextDouble(num +1);
	}
	
	public final static double probability()
	{
		return nextPlus(100f);
	}
	
	public final static <T> T selectOne(List<T> list)
	{
		if(list == null || list.isEmpty())
		{
			throw new IllegalArgumentException("List is null or empty");
		}
		return list.get(random().nextInt(list.size()));
	}
	
	public final static <T> T selectOne(T[] ary)
	{
		if(ary == null || ary.length == 0)
		{
			throw new IllegalArgumentException("Array is null or empty");
		}
		return ary[random().nextInt(ary.length)];
	}
}
