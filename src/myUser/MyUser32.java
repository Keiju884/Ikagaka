package myUser;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.win32.StdCallLibrary;

public interface MyUser32 extends StdCallLibrary
{
	MyUser32 INSTANCE = Native.load("user32", MyUser32.class);

	boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer data);

	boolean IsWindowVisible(HWND hWnd);

	int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);

	boolean GetWindowRect(HWND hWnd, RECT rect);
	
	boolean IsIconic(HWND hWnd);
}
