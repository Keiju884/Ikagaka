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
		LocalDateTime now = LocalDateTime.now();
		if(now.getHour() >= 5 && now.getHour() <= 10)
		{
			return Situation.Moning;
		}
		else if(now.getHour() >= 11 && now.getHour() <= 14)
		{
			return Situation.Noon;
		}
		else if(now.getHour() >= 15 && now.getHour() <=18)
		{
			return Situation.Evening;
		}
		else if(now.getHour() >= 19 && now.getHour() <= 23)
		{
			return Situation.Night;
		}
		else if(now.getHour() >= 0 && now.getHour() <= 4)
		{
			return Situation.Midnight;
		}
		return Situation.Normal ;
	}
	
	public static  Situation getTimeorNormalSituation()
	{
		return RandomUtil.random().nextInt() != 0 ? Situation.Normal : getTimeSituation();
	}
}
