package windowsAPI;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.win32.StdCallLibrary;

public interface MyUser32 extends StdCallLibrary
{
	MyUser32 INSTANCE = Native.load("user32", MyUser32.class);
	
    int GetDpiForWindow(HWND hwnd);

	HWND FindWindowW(WString className, WString windowName);

	boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer data);

	int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);

	boolean GetWindowRect(HWND hWnd, RECT rect);

	boolean IsIconic(HWND hWnd);

	HWND FindWindowExW(HWND hwndParent, int childAfer, WString className, WString windowName);

	HWND GetWindow(HWND hwnd, int i);

	HWND GetForegroundWindow();

	boolean SetForegroundWindow(HWND hwnd);

	boolean IsWindowVisible(HWND hwnd);

	boolean SetCursorPos(int x, int y);

	HWND SetFocus(HWND hwnd);

	long GetMenu(HWND hwnd);

	int GetMenuStringW(long handle, int item, char[] ws, int ccMax, int flags);

	boolean MoveWindow(HWND hwnd, int X, int Y, int nWidth, int nHeight, boolean bRepaint);

	long SendMessageW(WinDef.HWND hwnd, int msg, int wparam, WString lparam);

	long SendMessageA(WinDef.HWND hwnd, int msg, int wparam, Pointer lparam);

	long SendMessageW(WinDef.HWND hwnd, int msg, int wparam, char[] lparam);

	long SendMessageW(WinDef.HWND hwnd, int msg, int wparam, int lparam);

	long SendMessageW(WinDef.HWND hwnd, int msg, int wparam, long lparam);

	long PostMessageW(WinDef.HWND hwnd, int msg, int wparam, int lparam);

	long PostMessageW(WinDef.HWND hwnd, int msg, int wparam, long lparam);
}
