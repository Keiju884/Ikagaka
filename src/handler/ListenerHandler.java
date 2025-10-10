package handler;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface ListenerHandler
{
	void onMouseClicked(MouseEvent e);
	void onMousePressed(MouseEvent e);
	void onMouseReleased(MouseEvent e);
    void onMouseWheelMoved(MouseWheelEvent e);
	void onMouseMove(MouseEvent e);
    void onMouseDrag(MouseEvent e);
    void onKey(KeyEvent e);
}
