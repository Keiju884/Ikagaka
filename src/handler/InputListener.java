package handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class InputListener implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener
{
	private Map<JPanel, ListenerHandler> panelMap = new HashMap<JPanel, ListenerHandler>();

	public void register(JPanel panel, ListenerHandler listener)
	{
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addKeyListener(this);
		panel.addMouseWheelListener(this);
		panel.setFocusable(true);
		this.panelMap.put(panel, listener);
	}

	private ListenerHandler getHandler(Object src)
	{
		if(src instanceof JPanel)
		{
			return panelMap.get((JPanel) src);
		}
		return null;
	}

	public void mouseMoved(MouseEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onMouseMove(e);
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onMouseDrag(e);
		}
	}

	public void mouseClicked(MouseEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onMouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onMousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onMouseReleased(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onKey(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		ListenerHandler h = getHandler(e.getSource());
		if(h != null)
		{
			h.onMouseWheelMoved(e);
		}
	}

}