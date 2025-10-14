package ikagaka;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bean.CharacterDialog;
import bean.CharacterImage;
import bean.CharacterModel;
import bean.Situation;
import bean.Vector2Int;
import utils.LogWriterNew;
import utils.StreamUtil;
import utils.WindowDetailUtil;

public class LoadCharacterFile
{
	public List<CharacterModel> characterList;

	public LoadCharacterFile()
	{
		File characterDir = new File("Character"); // ベースのフォルダ
		characterList = loadAllCharacters(characterDir);
	}

	public static List<CharacterModel> loadAllCharacters(File baseDir)
	{
		List<CharacterModel> characters = new ArrayList<>();

		File[] characterDirs = baseDir.listFiles(File::isDirectory);
		if(characterDirs == null)
		{
			LogWriterNew.writeLog("No Directory:" + baseDir);
			return characters;
		}

		for (File charDir : characterDirs)
		{
			// 各キャラクターのサブフォルダ/
			File spriteDir = new File(charDir, "Sprite");
			File dataDir = new File(charDir, "Date");
			File stageInfoFile = new File(charDir, "StageInfo.xml");

			File characterInfoFile = new File(dataDir, "CharacterInfo.xml");
			File dialogInfoFile = new File(dataDir, "DialogInfo.xml");
			File setDialogFile = new File(dataDir, "SetDialog.xml");

			if(!stageInfoFile.exists())
			{
				LogWriterNew.writeLog("StageInfo.xml が存在しません: " + stageInfoFile);
				continue;
			}
			if(!characterInfoFile.exists())
			{
				LogWriterNew.writeLog("CharacterInfo.xml が存在しません: " + characterInfoFile);
				continue;
			}
			if(!dialogInfoFile.exists())
			{
				LogWriterNew.writeLog("DialogInfo.xml が存在しません: " + dialogInfoFile);
				continue;
			}
			if(!setDialogFile.exists())
			{
				LogWriterNew.writeLog("SetDialog.xml が存在しません: " + setDialogFile);
				continue;
			}
			String packageId = "";
			try
			{
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stageInfoFile);
				Element root = doc.getDocumentElement();
				String s = root.getElementsByTagName("PackageId").item(0).getTextContent().trim();
				if(characters.stream().anyMatch(x -> x.getPackageId().equals(s)))
				{
					LogWriterNew.writeLog("同じpackageIdが既に存在します:" + s);
					continue;
				}
				packageId = s;
			}
			catch (Exception e)
			{
				LogWriterNew.writeLog("StageInfo.xml 読み込み失敗: " + stageInfoFile);
				LogWriterNew.writeLog(e.toString());
				e.printStackTrace();
				continue;
			}

			try
			{
				// Dialog読み込み（ID→DialogMap）
				List<CharacterDialog> dialogList = loadDialog(dialogInfoFile, packageId);
				Map<Situation, List<CharacterDialog>> dialogMap = setSituation(setDialogFile, dialogList);

				// Characterモデル作成
				CharacterModel model = loadCharacterModel(charDir.getName(), packageId, characterInfoFile, spriteDir,
						dialogMap);

				characters.add(model);
				LogWriterNew.writeLog("読み込み成功: " + model.getName());

			}
			catch (Exception e)
			{
				LogWriterNew.writeLog("読み込み失敗: " + charDir.getName());
				LogWriterNew.writeLog(e.toString());
				e.printStackTrace();
			}
		}

		return characters;
	}

	private static CharacterModel loadCharacterModel(String folderName, String packageId, File xmlFile, File spriteDir,
			Map<Situation, List<CharacterDialog>> dialogMap)
			throws Exception
	{
		// XML読み込み
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		doc.getDocumentElement().normalize();

		// <Character> 要素を取得
		NodeList charNodes = doc.getElementsByTagName("Character");
		if(charNodes.getLength() == 0)
		{
			throw new RuntimeException("CharacterInfo.xml に <Character> タグが存在しません: " + xmlFile);
		}
		Element charElem = (Element) charNodes.item(0);

		String name = getTextContent(charElem, "Name");
		int textbox = Integer.parseInt(getTextContent(charElem, "TextBoxPivot"));

		// Sprite設定読み込み
		List<CharacterImage> spriteSettings = new ArrayList<CharacterImage>();
		NodeList spriteSettingNodes = charElem.getElementsByTagName("SpriteSetting");
		for (int i = 0; i < spriteSettingNodes.getLength(); i++)
		{
			Element spriteElem = (Element) spriteSettingNodes.item(i);
			String spriteName = spriteElem.getAttribute("Sprite");

			Element settingElem = (Element) spriteElem.getElementsByTagName("Setting").item(0);
			double scale = Double.parseDouble(settingElem.getAttribute("Scale"));
			int pivotX = Integer.parseInt(settingElem.getAttribute("Pivot_X"));
			int pivotY = Integer.parseInt(settingElem.getAttribute("Pivot_Y"));

			spriteSettings.add(new CharacterImage(spriteName, null, new Vector2Int(pivotX, pivotY), scale));
		}

		// 画像読み込み
		List<CharacterImage> imageList = new ArrayList<>();
		File[] imageFiles = spriteDir.listFiles((dir, name1) -> name1.toLowerCase().endsWith(".png"));
		if(imageFiles != null)
		{
			for (File imageFile : imageFiles)
			{
				BufferedImage image = ImageIO.read(imageFile);
				String imageName = imageFile.getName().replace(".png", "");

				CharacterImage setting = StreamUtil.filterFirst(spriteSettings, x -> imageName.contains(x.imageName));
				if(setting != null)
				{
					//image = resizeImage(image, setting);
					int x = (int) Math.round(setting.pivot.x * setting.scale);
					int y = (int) Math.round(setting.pivot.y * setting.scale);
					Vector2Int pivot = new Vector2Int(x, y);
					imageList.add(new CharacterImage(imageName, image, pivot, setting.scale));
				}
			}
		}

		boolean hasNormal = imageFiles != null
				&& Arrays.stream(imageFiles).anyMatch(f -> f.getName().equalsIgnoreCase("normal.png"));
		if(!hasNormal)
		{
			throw new RuntimeException("Normal.png が見つかりません: " + spriteDir.getAbsolutePath());
		}

		return new CharacterModel(name, packageId, WindowDetailUtil.getWorkScreenBottom(), textbox, imageList,
				dialogMap);
	}

	private static List<CharacterDialog> loadDialog(File dialogListXmlFile, String packageId)
			throws Exception
	{
		List<CharacterDialog> dialogList = new ArrayList<CharacterDialog>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(dialogListXmlFile);
		Element root = doc.getDocumentElement();

		NodeList dialogNodes = root.getElementsByTagName("Dialog");
		for (int i = 0; i < dialogNodes.getLength(); i++)
		{
			Element dialogElem = (Element) dialogNodes.item(i);

			int dialogId = Integer.parseInt(dialogElem.getAttribute("ID"));
			String emotion = getTextContent(dialogElem, "Emotion");
			String text = getTextContent(dialogElem, "Text");

			if(text.trim().isEmpty() || emotion.trim().isEmpty())
			{
				continue;
			}

			CharacterDialog dialog = new CharacterDialog(
					dialogId,
					packageId,
					emotion,
					text,
					new Font("ＭＳゴシック", Font.PLAIN, 20));

			dialogList.add(dialog);
		}
		return dialogList;
	}

	public static Map<Situation, List<CharacterDialog>> setSituation(File setDialogFile,
			List<CharacterDialog> dialogList) throws Exception
	{
		Map<Situation, List<CharacterDialog>> dialogMap = new HashMap<Situation, List<CharacterDialog>>();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(setDialogFile);
		Element root = doc.getDocumentElement();

		for (Situation s : Situation.values())
		{
			NodeList situationNodes = root.getElementsByTagName(s.name());
			if(situationNodes.getLength() > 0)
			{
				Element situationElem = (Element) situationNodes.item(0);
				NodeList dialogNodes = situationElem.getElementsByTagName("Dialog");
				for (int i = 0; i < dialogNodes.getLength(); i++)
				{
					int dialogId = Integer.parseInt(dialogNodes.item(i).getTextContent());
					CharacterDialog dialg = StreamUtil.filterFirst(dialogList, x -> x.id == dialogId);
					if(dialg != null)
					{
						if(!dialogMap.containsKey(s))
						{
							List<CharacterDialog> situList = new ArrayList<CharacterDialog>();
							dialogMap.put(s, situList);
						}
						dialogMap.get(s).add(dialg);
					}
				}
			}
		}
		return dialogMap;
	}

	private static String getTextContent(Element parent, String tagName)
	{
		NodeList nodes = parent.getElementsByTagName(tagName);
		if(nodes.getLength() > 0)
		{
			return nodes.item(0).getTextContent().trim();
		}
		return "";
	}

	private static BufferedImage resizeImage(BufferedImage image, CharacterImage setting)
	{
		double scale = setting.scale;

		// 元のサイズ
		int originalWidth = image.getWidth();
		int originalHeight = image.getHeight();

		// 新しいサイズを計算
		int newWidth = (int) Math.round(originalWidth * scale);
		int newHeight = (int) Math.round(originalHeight * scale);

		// pivot座標をスケールに合わせて変更
		setting.pivot.x = (int) Math.round(setting.pivot.x * scale);
		setting.pivot.y = (int) Math.round(setting.pivot.y * scale);

		// 新しい画像を作成
		BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		// 描画品質を向上
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawImage(image, 0, 0, newWidth, newHeight, null);
		g2.dispose();

		return resizedImg;
	}
}