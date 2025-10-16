package bean;

import java.text.MessageFormat;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;

import windowsAPI.MyUser32;

public class WindowInfo
{
	public HWND hWnd;
	public RECT rect;
	public String title;
	public double scale;

	public WindowInfo(HWND hWnd, RECT rect, String title, double scale)
	{
		this.hWnd = hWnd;
		this.rect = rect;
		this.title = title;
		this.scale = scale;
	}
	
	public String toString()
	{
		return MessageFormat.format("[{0}:{1}:{2}:{3}]", hWnd, rect,title,scale);
	}

	public boolean isMinimized()
	{
		return MyUser32.INSTANCE.IsIconic(hWnd);
	}
}
