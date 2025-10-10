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
		updatePaintPosition();
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
		this.paintDetail = new CharacterPaintDetail(original, original.paintDetail.getImageList(),
				original.paintDetail.textBoxPivot);
		updatePaintPosition();
	}

	public void setStopPostion()
	{
		stopPostion.set(position);
	}

	public void updatePaintPosition()
	{
		int x = (int) (this.getPosition().x - (paintDetail.getNowImage().pivot.x * 1.5 * paintDetail.getImageSizeRate()));
		int y = (int) (this.getPosition().y - paintDetail.getNowImage().pivot.y * paintDetail.getImageSizeRate());
		this.paintDetail.paintPosition = new Vector2Int(x, y);
	}

	public boolean contains(Vector2Int mousePos)
	{
		int alpha = 0;
		try
		{
			alpha = (paintDetail.getNowImage().image.getRGB(mousePos.x - paintDetail.paintPosition.x,
					mousePos.y - paintDetail.paintPosition.y) >> 24)
					& 0xff;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			alpha = 0;
		}
		return (mousePos.x >= paintDetail.paintPosition.x
				&& mousePos.x <= paintDetail.paintPosition.x + paintDetail.getNowImage().size
				&& mousePos.y >= paintDetail.paintPosition.y
				&& mousePos.y <= paintDetail.paintPosition.y + paintDetail.getNowImage().size && alpha > 0);
	}

	/**
	 * Characterの更新
	 */
	public void update()
	{
		updatePaintPosition();
		if(paintDetail.isTextBoxAlphaZero())
		{
			paintDetail.resetTextBoxAlpha();
			talkDetail.timerSetting();
			talkDetail.setTalkEnd(false);
		}
		if(talkDetail.isTalkEnd())
		{
			paintDetail.disTextBoxAlpha();;
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
			talkDetail.addTimer();;
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
		return position;
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