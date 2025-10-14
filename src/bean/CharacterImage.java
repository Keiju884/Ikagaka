package bean;

import java.awt.image.BufferedImage;

public class CharacterImage
{
	public String imageName;

	public BufferedImage image;

	public Vector2Int pivot;

	public double scale;

	public CharacterImage(String imageName, BufferedImage image, Vector2Int pivot, double scale)
	{
		this.imageName = imageName;
		this.image = image;
		this.pivot = pivot;
		this.scale = scale;
	}
}
