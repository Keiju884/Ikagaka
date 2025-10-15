package bean;

import java.util.List;

import utils.StreamUtil;

public class CharacterPaintDetail
{
	public CharacterModel owner;

	private List<CharacterImage> imageList;

	private CharacterImage nowImage;

	public Vector2Int paintPosition;

	private int textBoxAlpha;

	public int textBoxPivot;

	private double imageSizeRate;

	public CharacterPaintDetail(CharacterModel owner, List<CharacterImage> imageList, int textBoxPivot)
	{
		this.owner = owner;
		this.imageList = imageList;
		this.nowImage = getNowImage();
		this.textBoxPivot = textBoxPivot;
		this.imageSizeRate = 1.0;
		this.paintPosition = new Vector2Int();
	}

	public void updatePaintPosition()
	{
		int x = (int) (owner.getPosition().x - (getNowImage().getPivot().x * 1.5 * getImageSizeRate()));
		int y = (int) (owner.getPosition().y - getNowImage().getPivot().y * getImageSizeRate());
		this.paintPosition.set(x, y);
	}

	public boolean isTextBoxAlphaZero()
	{
		return textBoxAlpha <= 0;
	}

	public void disTextBoxAlpha()
	{
		this.textBoxAlpha -= 5;
	}

	public Vector2Int getPaintPosition()
	{
		return paintPosition;
	}

	public int getPaintTextBoxY()
	{
		return (int) (this.getCenterPosition().y - this.textBoxPivot * getImageSizeRate());
	}

	public void setPaintPosition(Vector2Int paintPosition)
	{
		this.paintPosition = paintPosition;
	}

	public CharacterImage getImage(String emotion)
	{
		return StreamUtil.filterFirst(imageList, x -> x.getImageName().equals(emotion));
	}

	public void changeNowImages(String emotion)
	{
		this.nowImage = getImage(emotion);
	}

	public CharacterImage getNowImage()
	{
		if(nowImage == null)
		{
			nowImage = getImage("Normal");
		}
		return nowImage;
	}

	public void resetTextBoxAlpha()
	{
		textBoxAlpha = 255;
	}

	public int getTextBoxAlpha()
	{
		return textBoxAlpha;
	}

	public Vector2Int getCenterPosition()
	{
		int x = getPaintPosition().x + (int) (getNowImage().getPivot().x * getImageSizeRate());
		int y = getPaintPosition().y + (int) (getNowImage().getPivot().y * getImageSizeRate());
		return new Vector2Int(x, y);
	}

	public void changeImageSizeRate(double rate)
	{
		this.imageSizeRate += rate;
		if(this.imageSizeRate <= 0.1)
		{
			this.imageSizeRate = 0.1;
		}
		if(this.imageSizeRate >= 3.0)
		{
			this.imageSizeRate = 3.0;
		}
	}

	public double getImageSizeRate()
	{
		return this.imageSizeRate;
	}

	public Vector2Int getImageSize()
	{
		int w = (int) (getNowImage().getImage().getWidth() * getNowImage().getScale() * getImageSizeRate());
		int h = (int) (getNowImage().getImage().getHeight() * getNowImage().getScale() * getImageSizeRate());
		return new Vector2Int(w, h);
	}

	public List<CharacterImage> getImageList()
	{
		return this.imageList;
	}
}
