package utils;

import java.util.List;
import java.util.function.Predicate;

public class StreamUtil
{
	public final static <T> List<T> filterList(List<T> list, Predicate<? super T> predicate)
	{
		return list.stream().filter(predicate).toList();
	}

	public final static <T> T filterFirst(List<T> list, Predicate<? super T> predicate)
	{
		return list.stream().filter(predicate).findFirst().orElse(null);
	}
}
