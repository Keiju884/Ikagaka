package utils;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.RECT;

import bean.Vector2Int;
import bean.WindowInfo;
import myUser.MyUser32;

public final class WindowDetailUtil
{
	public static Vector2Int getWorkScreenBottom()
	{
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Rectangle fullBounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		int bottomRightX = fullBounds.x + fullBounds.width - screenInsets.right;
		int bottomRightY = fullBounds.y + fullBounds.height - screenInsets.bottom;
		return new Vector2Int(bottomRightX, bottomRightY);
	}

	/**
	 * 全モニターのタスクバー領域を結合して 1つの Rectangle として返します。
	 */
	public static Area getAllTaskbarArea()
	{
		Area taskbarArea = new Area();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();

		for (GraphicsDevice screen : screens)
		{
			GraphicsConfiguration gc = screen.getDefaultConfiguration();
			Rectangle bounds = gc.getBounds();
			Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

			if (insets.top > 0)
				taskbarArea.add(new Area(new Rectangle2D.Double(bounds.x, bounds.y, bounds.width, insets.top)));

			if (insets.bottom > 0)
				taskbarArea.add(new Area(new Rectangle2D.Double(bounds.x, bounds.y + bounds.height - insets.bottom, bounds.width, insets.bottom)));

			if (insets.left > 0)
				taskbarArea.add(new Area(new Rectangle2D.Double(bounds.x, bounds.y, insets.left, bounds.height)));

			if (insets.right > 0)
				taskbarArea.add(new Area(new Rectangle2D.Double(bounds.x + bounds.width - insets.right, bounds.y, insets.right, bounds.height)));
		}

		return taskbarArea;
	}

	public static Rectangle getAllScreenBounds()
	{
		Rectangle allBounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();
		for (GraphicsDevice screen : screens)
		{
			Rectangle bounds = screen.getDefaultConfiguration().getBounds();
			allBounds = allBounds.union(bounds);
		}
		return allBounds;
	}

	public static List<WindowInfo> getWindows()
	{
		List<WindowInfo> list = new ArrayList<>();

		MyUser32 user32 = MyUser32.INSTANCE;

		// --- ウィンドウ列挙（前面から順）---
		user32.EnumWindows((hWnd, data) ->
		{
			if(!user32.IsWindowVisible(hWnd))
				return true;

			RECT rect = new RECT();
			user32.GetWindowRect(hWnd, rect);
			if(rect.left == 0 && rect.right == 0 && rect.top == 0 && rect.bottom == 0)
				return true; // 不正座標は無視

			char[] buffer = new char[512];
			user32.GetWindowTextW(hWnd, buffer, 512);
			String title = Native.toString(buffer);

			// 特定ウィンドウをスキップ
			if(title.contains("如何か"))
				return true;

			list.add(new WindowInfo(hWnd, rect, title));
			return true;
		}, null);

		return list;
	}


}
