package bean;

import java.awt.image.BufferedImage;

public class CharacterImage
{
	private String imageName;

	private String packageId;

	private BufferedImage image;

	private BufferedImage frontImage;

	private Vector2Int pivot;

	private double scale;

	public CharacterImage(String imageName, String packageId, BufferedImage image, BufferedImage frontImage,
			Vector2Int pivot, double scale)
	{
		this(imageName, packageId, image, pivot, scale);
		this.frontImage = frontImage;
	}

	public CharacterImage(String imageName, String packageId, BufferedImage image, Vector2Int pivot, double scale)
	{
		this.imageName = imageName;
		this.packageId = packageId;
		this.image = image;
		this.pivot = pivot;
		this.scale = scale;
	}

	public boolean isFrontImage()
	{
		return frontImage != null;
	}

	public String getImageName()
	{
		return imageName;
	}

	public String getPackageId()
	{
		return packageId;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public BufferedImage getFrontImage()
	{
		return frontImage;
	}

	public Vector2Int getPivot()
	{
		return pivot;
	}

	public double getScale()
	{
		return scale;
	}
}
