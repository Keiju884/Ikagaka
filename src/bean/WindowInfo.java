package bean;

import java.text.MessageFormat;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

import myUser.MyUser32;

public class WindowInfo
{
	public HWND hWnd;
	public RECT rect;
	public String title;

	public WindowInfo(HWND hWnd, RECT rect, String title)
	{
		this.hWnd = hWnd;
		this.rect = rect;
		this.title = title;
	}
	
	public String toString()
	{
		return MessageFormat.format("[{0}:{1}:{2}]", hWnd, rect,title);
	}

	public boolean isMinimized()
	{
		return MyUser32.INSTANCE.IsIconic(hWnd);
	}
}
