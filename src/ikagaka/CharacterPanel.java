package ikagaka;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bean.CharacterModel;
import bean.Vector2Int;
import bean.WindowInfo;
import handler.InputListener;
import handler.ListenerHandler;
import utils.StreamUtil;
import utils.WindowDetailUtil;

public class CharacterPanel extends JPanel implements ListenerHandler
{
	private List<CharacterModel> characterList;

	private List<CharacterModel> activeCharacterList;

	private CharacterModel clickModel;

	private CharacterMenu menu;

	private boolean isLeftClick = false;

	private Vector2Int clickPosition = new Vector2Int();

	private Vector2Int mousePosition = new Vector2Int();

	private List<WindowInfo> windows = new ArrayList<WindowInfo>();

	private PanelPaintDetail paintDetail;

	public CharacterPanel(List<CharacterModel> characterList)
	{
		this.characterList = characterList;
		this.activeCharacterList = new ArrayList<CharacterModel>();
		this.clickModel = null;
		this.menu = new CharacterMenu(this, this.characterList);
		this.paintDetail = new PanelPaintDetail();
		InputListener listener = new InputListener();
		listener.register(this, this);
		CharacterModel createModel = null;
		String packageId = LoadSaveFile.Instance().load();
		if(packageId != null && !packageId.isEmpty() && this.characterList.stream().anyMatch(x -> x.getPackageId().equals(packageId)))
		{
			createModel = StreamUtil.filterFirst(this.characterList, x -> x.getPackageId().equals(packageId));
		}
		else
		{
			createModel = this.characterList.getFirst();
		}
		createCharacterModel(createModel);
		setOpaque(false); // 背景透明
	}

	public void update()
	{
		if(activeCharacterList.size() > 0)
		{
			windows = WindowDetailUtil.getWindows();
			for (CharacterModel model : activeCharacterList)
			{
				moveWindow(model);
				model.update();
			}
			CharacterModel clickedModel = clickModel;
			if(clickedModel != null)
			{
				moveCharacter_Mouse(clickedModel);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		if(activeCharacterList.size() > 0)
		{
			for (CharacterModel model : activeCharacterList)
			{
				paintDetail.paintCharacter(model, g2d);
				if(model.talkDetail.isTalk() || (model.talkDetail.isTalkEnd() && model.paintDetail.getTextBoxAlpha() > 0))
				{
					paintDetail.paintTake(model, g2d);
				}
			}
			g2d.dispose();
		}
	}

	public void createCharacterModel(CharacterModel model)
	{
		CharacterModel createModel = new CharacterModel(model);
		activeCharacterList.add(createModel);
	}

	public void removeCharacterModel()
	{
		if(clickModel != null)
		{
			activeCharacterList.remove(clickModel);
		}
		if(activeCharacterList.size() == 0)
		{
			exit();
		}
	}

	public void changeCharacterModel(CharacterModel model)
	{
		if(clickModel != null && !clickModel.getPackageId().equals(model.getPackageId()))
		{
			Vector2Int p = clickModel.getPosition();
			activeCharacterList.remove(clickModel);
			CharacterModel createModel = new CharacterModel(model);
			createModel.setPosition(p);
			activeCharacterList.add(createModel);
			if(clickModel.getPackageId() != null && !clickModel.getPackageId().isEmpty())
			{
				LoadSaveFile.Instance().save(clickModel.getPackageId());
			}
		}

	}
	public void exit()
	{
		Ikagaka.exit(this.activeCharacterList.getLast().getPackageId());
	}

	public void changeCharacterTalkTime(int talkTimeSec)
	{
		if(clickModel != null)
		{
			int time = talkTimeSec * 60;
			clickModel.talkDetail.changeTalkStart(time);
		}
	}

	public void stopTalkTime(boolean isStop)
	{
		if(clickModel != null)
		{
			clickModel.talkDetail.setStopNextTalk(!isStop);
		}
	}

	private void characterListMoveLast(CharacterModel clickedModel)
	{
		if(clickedModel != null)
		{
			activeCharacterList.remove(clickedModel);
			activeCharacterList.add(clickedModel);
		}
	}

	private void moveCharacter_Mouse(CharacterModel model)
	{

		if(model != null && isLeftClick)
		{
			model.isDragMove = true;
			Point p = MouseInfo.getPointerInfo().getLocation();
			int xMoved = p.x - clickPosition.x;
			int yMoved = p.y - clickPosition.y;
			int y = model.getStopPostion().y + yMoved;
			WindowInfo window = mouseOnWindow(p,model.paintDetail.getCenterPosition());
			if(window != null)
			{
				model.setOnWindow(window);
				y = window.rect.top;
			}
			else
			{
				model.setOnWindow(null);
			}
			model.setPosition(model.getStopPostion().x + xMoved, y);
			if(WindowDetailUtil.getWorkScreenBottom().y < y)
			{
				y = WindowDetailUtil.getWorkScreenBottom().y;
				model.setPosition(model.getPosition().x, y);
			}
		}
		else
		{
			model.isDragMove = false;
		}
	}

	private void moveWindow(CharacterModel model)
	{
		WindowInfo onWindow = model.getOnWindow();
		WindowInfo updateWindow = null;
		for (WindowInfo win : this.windows)
		{
			if(model.getOnWindow() != null && model.getOnWindow().hWnd.equals(win.hWnd))
			{
				updateWindow = win;
			}
		}
		if(updateWindow != null && !onWindow.isMinimized())
		{
			int xMoved = updateWindow.rect.toRectangle().x - onWindow.rect.toRectangle().x;
			int yMoved = updateWindow.rect.toRectangle().y - onWindow.rect.toRectangle().y;
			int y = model.getPosition().y + yMoved;
			model.setOnWindow(updateWindow);
			if(WindowDetailUtil.getWorkScreenBottom().y < model.paintDetail.getCenterPosition().y)
			{
				y = WindowDetailUtil.getWorkScreenBottom().y;
			}
			model.setPosition(model.getPosition().x + xMoved, y);
		}
	}

	private WindowInfo mouseOnWindow(Point p, Vector2Int pos)
	{
		WindowInfo window = null;
		for (WindowInfo win : this.windows)
		{
			if(p.x >= win.rect.left && p.x <= win.rect.right)
			{
				if(p.y + 100 > win.rect.top && p.y <= win.rect.top)
				{
					if(pos.y + 100 > win.rect.top && pos.y <= win.rect.top)
					{
						if(window == null || window.rect.top < win.rect.top)
						{
							window = win;
						}
					}
				}
			}
		}
		return window;
	}

	@Override
	public void onMouseMove(MouseEvent e)
	{
		mousePosition.set(e.getX(), e.getY());
	}

	@Override
	public void onMouseDrag(MouseEvent e)
	{
		mousePosition.set(e.getX(), e.getY());
	}

	@Override
	public void onKey(KeyEvent e)
	{

	}

	@Override
	public void onMouseClicked(MouseEvent e)
	{

	}

	@Override
	public void onMousePressed(MouseEvent e)
	{
		clickPosition.set(e.getX(), e.getY());
		for (CharacterModel model : activeCharacterList)
		{
			if(model.contains(clickPosition))
			{
				clickModel = model;
				break;
			}
		}
		if(SwingUtilities.isLeftMouseButton(e))
		{
			isLeftClick = true;
			characterListMoveLast(clickModel);
		}
		if(clickModel != null)
		{
			menu.showMenu(e, clickModel.talkDetail.isStopNextTalk());
		}
	}

	@Override
	public void onMouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isLeftMouseButton(e))
		{
			isLeftClick = false;
		}
		if(clickModel != null)
		{
			menu.showMenu(e, clickModel.talkDetail.isStopNextTalk());
		}
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e)
	{
		if(clickModel != null)
		{
			if(e.getWheelRotation() == -1)
			{
				clickModel.paintDetail.changeImageSizeRate(0.05);
			}
			else if(e.getWheelRotation() == 1)
			{
				clickModel.paintDetail.changeImageSizeRate(-0.05);
			}
		}
	}
}
