package myUser;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;

public interface WNDENUMPROC extends StdCallLibrary.StdCallCallback
{
	boolean callback(HWND hWnd, Pointer data);
}