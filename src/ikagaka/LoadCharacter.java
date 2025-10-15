package ikagaka;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import bean.CharacterDialog;
import bean.CharacterImage;
import bean.CharacterModel;
import bean.Situation;
import bean.Vector2Int;
import utils.LogWriterNew;
import utils.WindowDetailUtil;
import xml.CharacterInfoXml;
import xml.DialogListXml;
import xml.SetDialogXml;
import xml.SpriteDataXml;
import xml.SpriteSettingXml;

public class LoadCharacter
{
	private static final File CHARACTER_DIR = new File("Character");

	private static LoadCharacter load;

	private LoadCharacter()
	{

	}

	public static LoadCharacter Instance()
	{
		if(load == null)
		{
			load = new LoadCharacter();
		}
		return load;
	}

	public List<CharacterModel> loadAllCharacter()
	{

		List<CharacterModel> list = new ArrayList<>();

		File[] characterDirs = CHARACTER_DIR.listFiles(File::isDirectory);
		if(characterDirs == null)
		{
			LogWriterNew.writeLog("No Directory:" + CHARACTER_DIR, true);
			return list;
		}
		for (File charDir : characterDirs)
		{
			CharacterModel model = createCharacter(charDir);
			if(model != null)
			{
				list.add(model);
			}
		}
		return list;
	}

	private static CharacterModel createCharacter(File charDir)
	{
		File spriteDir = new File(charDir, "Sprite");
		File dataDir = new File(charDir, "Date");
		File characterInfoFile = new File(charDir, "CharacterInfo.xml");

		File spriteInfoFile = new File(dataDir, "SpriteInfo.xml");
		File dialogInfoFile = new File(dataDir, "DialogInfo.xml");
		File setDialogFile = new File(dataDir, "SetDialog.xml");

		if(!characterInfoFile.exists())
		{
			LogWriterNew.writeLog("CharacterInfo.xml が存在しません: " + characterInfoFile, true);
			return null;
		}
		if(!spriteInfoFile.exists())
		{
			LogWriterNew.writeLog("SpriteInfo.xml が存在しません: " + spriteInfoFile, true);
			return null;
		}
		if(!dialogInfoFile.exists())
		{
			LogWriterNew.writeLog("DialogInfo.xml が存在しません: " + dialogInfoFile, true);
			return null;
		}
		if(!setDialogFile.exists())
		{
			LogWriterNew.writeLog("SetDialog.xml が存在しません: " + setDialogFile, true);
			return null;
		}
		try
		{
			CharacterInfoXml characterInfo = loadXml(characterInfoFile, CharacterInfoXml.class);

			SpriteDataXml spriteDataXml = loadXml(spriteInfoFile, SpriteDataXml.class);

			String packageId = characterInfo.packageId;

			List<CharacterImage> imageList = createSprites(spriteDataXml, spriteDir, packageId);

			List<CharacterDialog> dialogList = createDialogs(dialogInfoFile, packageId);

			Map<Situation, List<CharacterDialog>> dialogMap = setDialogMap(setDialogFile, dialogList);

			CharacterModel model = new CharacterModel(characterInfo.name, packageId,
					WindowDetailUtil.getWorkScreenBottom(),
					spriteDataXml.textBoxPivot, imageList, dialogMap);

			LogWriterNew.writeLog("読み込み成功: " + model.getName(), false);

			return model;
		}
		catch (Exception e)
		{
			LogWriterNew.writeLog("読み込み失敗: " + characterInfoFile, true);
			LogWriterNew.writeLog(e.getClass().getSimpleName() + " - " + e.getMessage(), true);
			return null;
		}
	}

	private static List<CharacterDialog> createDialogs(File dialogInfoFile, String packageId) throws Exception
	{
		// DialogInfo.xml を読み込む
		DialogListXml dialogList = loadXml(dialogInfoFile, DialogListXml.class);

		// 各DialogXmlをCharacterDialogに変換
		return dialogList.getDialogs().stream()
				.map(d -> new CharacterDialog(
						d.getId(),
						packageId,
						d.getEmotion(),
						d.getText(),
						new Font("ＭＳゴシック", Font.PLAIN, 20)))
				.toList();
	}

	private static Map<Situation, List<CharacterDialog>> setDialogMap(File setDialogFile,
			List<CharacterDialog> dialogList) throws Exception
	{
		Map<Situation, List<CharacterDialog>> map = new EnumMap<>(Situation.class);

		SetDialogXml setDialogXml = loadXml(setDialogFile, SetDialogXml.class);

		// --- Normal（必須）チェック ---
		if(setDialogXml.normal == null || setDialogXml.normal.dialogIds == null
				|| setDialogXml.normal.dialogIds.isEmpty())
		{
			LogWriterNew.writeLog("SetDialog.xml に Normalが存在しません: " + setDialogFile, true);
			throw new IllegalStateException("Normal セクションに1件以上のDialogが必要です");
		}

		// --- Normalを追加 ---
		addDialogGroup(map, Situation.Normal, setDialogXml.normal, dialogList, true);

		// --- 他の状況は存在する場合のみ追加 ---
		addDialogGroup(map, Situation.Moning, setDialogXml.moning, dialogList, false);
		addDialogGroup(map, Situation.Noon, setDialogXml.noon, dialogList, false);
		addDialogGroup(map, Situation.Evening, setDialogXml.evening, dialogList, false);
		addDialogGroup(map, Situation.Night, setDialogXml.night, dialogList, false);
		addDialogGroup(map, Situation.Midnight, setDialogXml.midnight, dialogList, false);

		return map;
	}

	private static void addDialogGroup(Map<Situation, List<CharacterDialog>> map, Situation situation,
			SetDialogXml.DialogGroup group, List<CharacterDialog> dialogList, boolean isRequired)
	{
		// --- タグが存在しない ---
		if(group == null || group.dialogIds == null || group.dialogIds.isEmpty())
		{
			if(isRequired)
			{
				LogWriterNew.writeLog(" <" + situation.name() + "> に Dialog がありません。", true);
				throw new IllegalStateException("" + situation + " にDialogがありません");
			}
			else
			{
				// オプションの場合はスキップ
				return;
			}
		}

		List<CharacterDialog> dialogs = new ArrayList<>();

		for (Integer id : group.dialogIds)
		{
			dialogList.stream()
					.filter(d -> d.id == id)
					.findFirst()
					.ifPresentOrElse(dialogs::add,
							() -> LogWriterNew.writeLog("Dialog ID " + id + " が見つかりません (" + situation + ")", true));
		}

		// 有効なDialogが存在する場合のみMapに登録
		if(!dialogs.isEmpty())
		{
			map.put(situation, dialogs);
		}
		else if(isRequired)
		{
			LogWriterNew.writeLog("<" + situation + "> に有効なDialogが1つもありません。", true);
			throw new IllegalStateException("必須Situation " + situation + " に有効なDialogが1つもありません");
		}
	}

	public static List<CharacterImage> createSprites(SpriteDataXml xml, File spriteDir, String packageId)
			throws Exception
	{
		List<CharacterImage> images = new ArrayList<>();

		// --- 各 SpriteSetting を読み込み ---
		for (SpriteSettingXml setting : xml.spriteSettingList)
		{
			String imageName = setting.spriteName;
			File imageFile = new File(spriteDir, imageName + ".png");

			if(!imageFile.exists())
			{
				LogWriterNew.writeLog("画像が見つかりません: " + imageFile.getPath(), true);
				continue;
			}
			BufferedImage image = ImageIO.read(imageFile);
			Vector2Int settingPivot = new Vector2Int(setting.setting.pivotX, setting.setting.pivotY);
			double scale = setting.setting.scale;
			int x = (int) Math.round(settingPivot.x * scale);
			int y = (int) Math.round(settingPivot.y * scale);

			File frontImageFile = new File(spriteDir, imageName + "_Front.png");
			CharacterImage characterImage;

			if(frontImageFile.exists())
			{
				BufferedImage frontImage = ImageIO.read(frontImageFile);
				characterImage = new CharacterImage(imageName, packageId, image, frontImage, new Vector2Int(x, y),
						scale);
			}
			else
			{
				characterImage = new CharacterImage(imageName, packageId, image, new Vector2Int(x, y), scale);
			}

			images.add(characterImage);
		}

		// --- Normal画像が存在するか確認 ---
		boolean hasNormal = images.stream().anyMatch(img -> "Normal".equalsIgnoreCase(img.getImageName()));
		if(!hasNormal)
		{
			throw new IllegalStateException(
					"必須画像 'Normal.png' が " + spriteDir.getPath() + " に存在しません。処理を中断します。");
		}

		return images;
	}

	private static <T> T loadXml(File path, Class<T> clazz) throws Exception
	{
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return clazz.cast(unmarshaller.unmarshal(path));
	}

}
