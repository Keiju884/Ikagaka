package bean;

import java.time.LocalDateTime;

import utils.RandomUtil;

public enum Situation
{
	Normal,
	Moning,
	Noon,
	Evening,
	Night,
	Midnight;

	public static  Situation getTimeSituation()
	{
		int hour = LocalDateTime.now().getHour();
		if(hour >= 5 && hour <= 10)
		{
			return Situation.Moning;
		}
		else if(hour >= 11 && hour <= 14)
		{
			return Situation.Noon;
		}
		else if(hour >= 15 && hour <=18)
		{
			return Situation.Evening;
		}
		else if(hour >= 19 && hour <= 23)
		{
			return Situation.Night;
		}
		else if(hour >= 0 && hour <= 4)
		{
			return Situation.Midnight;
		}
		return Situation.Normal ;
	}
	
	public static  Situation getTimeorNormalSituation()
	{
		Situation s = RandomUtil.nextPlus(1) != 0 ? Situation.Normal : getTimeSituation();
		return s;
	}
}
