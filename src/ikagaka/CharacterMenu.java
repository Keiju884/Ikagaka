package ikagaka;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import bean.CharacterModel;

public class CharacterMenu extends JPopupMenu
{
	private CharacterPanel panel;
	
	private List<CharacterModel> characterList;

	public CharacterMenu(CharacterPanel panel, List<CharacterModel> characterList)
	{
		this.panel = panel;
		this.characterList = characterList;
		init(false);
	}
	private void init(boolean modelIsStopTalk)
	{
		JMenu createCharacterMenu = new JMenu("キャラクターを生成");
		JMenu changeCharacterMenu = new JMenu("キャラクターを変更");

		addSubMenus_ModelList(createCharacterMenu,changeCharacterMenu, characterList);
		
		JMenu talkTimeMenu = new JMenu("喋る頻度を変更");
		
		addSubMenus_TalkTimer(talkTimeMenu,modelIsStopTalk);

		JMenuItem removeCharacterMenuItem = new JMenuItem("キャラクターを消去");
		JMenuItem exitCharacterMenuItem = new JMenuItem("終了");
		
		removeCharacterMenuItem.addActionListener(e -> this.panel.removeCharacterModel());
		exitCharacterMenuItem.addActionListener(e -> this.panel.exit());
		
		this.add(talkTimeMenu);
		this.add(createCharacterMenu);
		this.add(changeCharacterMenu);
		this.add(removeCharacterMenuItem);
		this.add(exitCharacterMenuItem);
	}

	private void addSubMenus_ModelList(JMenu createCharacterMenu,JMenu changeCharacterMenu, List<CharacterModel> characterList)
	{
		for (CharacterModel model : characterList)
		{
			JMenuItem subMenu = createSubMenu_ModelList(model, e -> this.panel.createCharacterModel(model));
			JMenuItem subMenu2 = createSubMenu_ModelList(model, e -> this.panel.changeCharacterModel(model));
			createCharacterMenu.add(subMenu);
			changeCharacterMenu.add(subMenu2);
		}
	}
	
	private void addSubMenus_TalkTimer(JMenu menu, boolean modelIsStopTalk)
	{
		String text = modelIsStopTalk ? "喋る" : "喋らない";
		int[] timer = {10,30,60,180,300,600};
		JMenuItem subMenu = new JMenuItem(text);
		subMenu.addActionListener(e -> this.panel.stopTalkTime(modelIsStopTalk));
		menu.add(subMenu);
		for(int valu : timer)
		{
			JMenuItem timeMenu = new JMenuItem(valu + "秒");
			timeMenu.addActionListener(e -> this.panel.changeCharacterTalkTime(valu));
			menu.add(timeMenu);
			
		}
	}

	private JMenuItem createSubMenu_ModelList(CharacterModel model, ActionListener l)
	{
		JMenuItem subMenu = new JMenuItem(model.getName());
		subMenu.addActionListener(l);
		return subMenu;
	}

	public void showMenu(MouseEvent e,boolean modelIsStopTalk)
	{
		if(e.isPopupTrigger())
		{
			removeAll();
			init(modelIsStopTalk);
			show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
