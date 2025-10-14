package bean;

import java.util.List;

import utils.StreamUtil;

public class CharacterPaintDetail
{
	public CharacterModel owner;

	private List<CharacterImage> imageList;

	private CharacterImage nowImage;

	private CharacterImage nowFrontImage;

	public Vector2Int paintPosition;

	private int textBoxAlpha;

	public int textBoxPivot;

	private double imageSizeRate;

	public CharacterPaintDetail(CharacterModel owner, List<CharacterImage> imageList, int textBoxPivot)
	{
		this.owner = owner;
		this.imageList = imageList;
		this.nowImage = getNowImage();
		this.nowFrontImage = getNowFrontImage();
		this.textBoxPivot = textBoxPivot;
		this.imageSizeRate = 1.0;
		this.paintPosition = new Vector2Int();
	}

	public void updatePaintPosition()
	{
		int x = (int) (owner.getPosition().x - (getNowImage().pivot.x * 1.5 * getImageSizeRate()));
		int y = (int) (owner.getPosition().y - getNowImage().pivot.y * getImageSizeRate());
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
		return this.getCenterPosition().y - this.textBoxPivot;
	}

	public void setPaintPosition(Vector2Int paintPosition)
	{
		this.paintPosition = paintPosition;
	}

	public CharacterImage getImage(String emotion)
	{
		return StreamUtil.filterFirst(imageList, x -> x.imageName.equals(emotion));
	}

	public CharacterImage getFrontImage(String emotion)
	{
		return StreamUtil.filterFirst(imageList, x -> x.imageName.matches(emotion + "_Front$"));
	}

	public CharacterImage getDefaultImage()
	{
		CharacterImage image = StreamUtil.filterFirst(imageList, x -> x.imageName.equals("Normal"));
		if(image != null)
		{
			return image;
		}
		return null;
	}

	public void changeNowImages(String emotion)
	{
		this.nowImage = getImage(emotion);
		this.nowFrontImage = getFrontImage(emotion);
	}

	public CharacterImage getNowImage()
	{
		if(nowImage == null)
		{
			nowImage = getImage("Normal");
		}
		return nowImage;
	}

	public CharacterImage getNowFrontImage()
	{
		if(nowFrontImage == null)
		{
			nowFrontImage = getFrontImage("Normal");
		}
		return nowFrontImage;
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
		int x = getPaintPosition().x + (int) (getNowImage().pivot.x * getImageSizeRate());
		int y = getPaintPosition().y + (int) (getNowImage().pivot.y * getImageSizeRate());
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
		int w = (int) (getNowImage().image.getWidth() * getNowImage().scale * getImageSizeRate());
		int h = (int) (getNowImage().image.getHeight() * getNowImage().scale * getImageSizeRate());
		return new Vector2Int(w, h);
	}

	public List<CharacterImage> getImageList()
	{
		return this.imageList;
	}
}
