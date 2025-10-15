package ikagaka;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

import bean.CharacterDialog;
import bean.CharacterImage;
import bean.CharacterModel;
import bean.Vector2Int;
import utils.WindowDetailUtil;

public class PanelPaintDetail
{
	public void paintCharacter(CharacterModel model, Graphics2D g2d)
	{
		Vector2Int pos = model.paintDetail.getPaintPosition();
		g2d.setClip(getPaintClip(model));
		CharacterImage characterImage = model.paintDetail.getNowImage();
		BufferedImage image = characterImage.getImage();
		Vector2Int imageSize = model.paintDetail.getImageSize();
		g2d.drawImage(image, pos.x, pos.y, imageSize.x, imageSize.y, null);
		if(characterImage.isFrontImage())
		{
			BufferedImage frontImage = characterImage.getFrontImage();
			g2d.setClip(null);
			g2d.drawImage(frontImage, pos.x, pos.y, imageSize.x, imageSize.y, null);
		}

	}

	public void paintTake(CharacterModel model, Graphics2D g2d)
	{
		int alpha = 255;
		if(model.talkDetail.isTalkEnd())
		{
			alpha = Math.max(0, model.paintDetail.getTextBoxAlpha());
		}
		int padding = 10; //余白
		Vector2Int textSize = getTextSize(g2d, model.talkDetail.getNowDialog());
		Vector2Int textBoxSize = getTextBoxSize(textSize, model.talkDetail.getNowDialog().text.length, padding);
		int textBoxPosY = model.paintDetail.getPaintTextBoxY() - textBoxSize.y;
		int textBoxRectX = getRectX(model.paintDetail.getCenterPosition().x, textBoxSize.x);
		int textRectX = getRectX(model.paintDetail.getCenterPosition().x, textSize.x);

		paintTextBox(g2d, textBoxRectX, textBoxPosY, textBoxSize, alpha);
		paintText(model.talkDetail.getNowDialog(), g2d, textRectX, textBoxPosY, textSize, alpha);
	}

	private void paintTextBox(Graphics2D g2d, int rectX, int textBoxPosY, Vector2Int textBoxSize, int alpha)
	{
		g2d.setColor(new Color(255, 255, 255, alpha));
		g2d.fillRoundRect(rectX, textBoxPosY, textBoxSize.x, textBoxSize.y, 15, 15);
		g2d.setColor(new Color(0, 0, 0, alpha));
		g2d.setStroke(new BasicStroke(5));
		g2d.drawRoundRect(rectX, textBoxPosY, textBoxSize.x, textBoxSize.y, 15, 15);

	}

	private void paintText(CharacterDialog dialg, Graphics2D g2d, int rectX, int textBoxPosY,
			Vector2Int textSize, int alpha)
	{
		g2d.setFont(dialg.font);
		g2d.setColor(new Color(0, 0, 0, alpha));
		String[] textAry = dialg.text;
		for (int i = 1; i <= textAry.length; i++)
		{
			g2d.drawString(textAry[i - 1], rectX, textBoxPosY + textSize.y * i);
		}
	}

	private Vector2Int getTextBoxSize(Vector2Int textSize, int textLength, int padding)
	{
		int x = textSize.x + padding * 2;
		int y = textSize.y * textLength + padding * 2;
		return new Vector2Int(x, y);
	}

	private Vector2Int getTextSize(Graphics2D g2d, CharacterDialog dialog)
	{
		FontMetrics fm = g2d.getFontMetrics(dialog.font);
		int length = -1;
		for (String s : dialog.text)
		{
			length = fm.stringWidth(s) > length ? fm.stringWidth(s) : length;
		}
		int x = length;
		int y = fm.getHeight();
		return new Vector2Int(x, y);
	}

	private int getRectX(int centerX, int textBoxSizeX)
	{
		return centerX - (textBoxSizeX / 2);
	}

	private Area getPaintClip(CharacterModel model)
	{
		Area areaA = new Area(WindowDetailUtil.getAllScreenBounds());
		Area areaB = WindowDetailUtil.getAllTaskbarArea();
		areaA.subtract(areaB);
		if(model != null && model.getOnWindow() != null)
		{
			areaA.subtract(new Area(model.getOnWindow().rect.toRectangle()));
		}
		return areaA;
	}
}
