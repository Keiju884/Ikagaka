package bean;

import java.util.List;
import java.util.Map;

public class CharacterModel
{
	private String name;

	private String packageId;

	private Vector2Int position;

	public CharacterPaintDetail paintDetail;

	public CharacterTalkDetail talkDetail;

	public boolean isDragMove;

	private Vector2Int stopPostion;

	private WindowInfo onWindow;

	public CharacterModel(String name, String packageId, Vector2Int position, int textBoxPivot,
			List<CharacterImage> imageList, Map<Situation, List<CharacterDialog>> dialogMap)
	{
		this.name = name;
		this.packageId = packageId;
		this.position = position;
		this.stopPostion = new Vector2Int();
		this.isDragMove = false;
		this.onWindow = null;
		this.talkDetail = new CharacterTalkDetail(this, dialogMap);
		this.paintDetail = new CharacterPaintDetail(this, imageList, textBoxPivot);
		paintDetail.updatePaintPosition();
	}

	public CharacterModel(CharacterModel original)
	{
		this.name = original.name;
		this.packageId = original.packageId;
		this.position = new Vector2Int(original.position);
		this.stopPostion = new Vector2Int();
		this.isDragMove = false;
		this.onWindow = null;
		this.talkDetail = new CharacterTalkDetail(this, original.talkDetail.dialogMap);
		this.paintDetail = new CharacterPaintDetail(this, original.paintDetail.getImageList(),
				original.paintDetail.textBoxPivot);
		paintDetail.updatePaintPosition();
	}

	public void setStopPostion()
	{
		stopPostion.set(position);
	}

	public boolean contains(Vector2Int mousePos)
	{
		int alpha = 0;
		try
		{
			alpha = (paintDetail.getNowImage().image.getRGB(mousePos.x, mousePos.y) >> 24) & 0xff;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			alpha = 0;
		}
		boolean flag1 = mousePos.x >= paintDetail.getPaintPosition().x;
		boolean flag2 = mousePos.x <= paintDetail.getPaintPosition().x + paintDetail.getImageSize().x;
		boolean flag3 = mousePos.y >= paintDetail.getPaintPosition().y;
		boolean flag4 = mousePos.y <= paintDetail.getPaintPosition().y + paintDetail.getImageSize().y;
		boolean flag5 = alpha > 0;
		return flag1 && flag2 && flag3 && flag4 && flag5;
	}

	/**
	 * Characterの更新
	 */
	public void update()
	{
		paintDetail.updatePaintPosition();
		if(paintDetail.isTextBoxAlphaZero())
		{
			paintDetail.resetTextBoxAlpha();
			talkDetail.timerSetting();
			talkDetail.setTalkEnd(false);
		}
		if(talkDetail.isTalkEnd())
		{
			paintDetail.disTextBoxAlpha();
			;
		}
		if(talkDetail.isTalkChangePre())
		{
			talkDetail.changeDialog();
			paintDetail.changeNowImages(talkDetail.getNowDialog().emotion);
			paintDetail.resetTextBoxAlpha();
		}
		if(talkDetail.isTalkEnd())
		{
			talkDetail.timerReset();
		}
		if(!isDragMove)
		{
			setStopPostion();
		}
		if(!talkDetail.stopTalk)
		{
			talkDetail.addTimer();
			;
		}
	}

	public String getName()
	{
		return this.name;
	}

	public String getPackageId()
	{
		return this.packageId;
	}

	public Vector2Int getPosition()
	{
		return this.position;
	}

	public void setPosition(int x, int y)
	{
		this.position.x = x;
		this.position.y = y;
	}

	public void setPosition(Vector2Int vec)
	{
		this.position.x = vec.x;
		this.position.y = vec.y;
	}

	public void movePosition(int x, int y)
	{
		this.position.x += x;
		this.position.y += y;
	}

	public void movePosition(Vector2Int vec)
	{
		this.position.x += vec.x;
		this.position.x += vec.y;
	}

	public Vector2Int getStopPostion()
	{
		return stopPostion;
	}

	public WindowInfo getOnWindow()
	{
		return onWindow;
	}

	public void setOnWindow(WindowInfo onWindow)
	{
		this.onWindow = onWindow;
	}
}