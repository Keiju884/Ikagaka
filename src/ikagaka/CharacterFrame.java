package ikagaka;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import bean.CharacterModel;
import utils.WindowDetailUtil;

public class CharacterFrame extends JFrame
{
	private List<CharacterModel> characterList;

	CharacterPanel panle;

	public CharacterFrame(List<CharacterModel> characterList)
	{
		super("如何か");
		if(characterList.size() <= 0)
		{
		    JLabel label = new JLabel("Characterが存在しません");
		    label.setForeground(Color.RED);
		    JOptionPane.showMessageDialog(this, label,"エラー",JOptionPane.ERROR_MESSAGE);
		    Ikagaka.exit(null);
		}
		this.characterList = characterList;
		this.panle = new CharacterPanel(this.characterList);
		init();
		update();
	}

	public void init()
	{
		SwingUtilities.invokeLater(() ->
		{
			// ★ すべてのディスプレイを覆うサイズと位置を設定
			setBounds(WindowDetailUtil.getAllScreenBounds());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			setUndecorated(true);
			setBackground(new Color(0, 0, 0, 0));

			add(panle);

			setLocationRelativeTo(null);
			setResizable(false);
			setAlwaysOnTop(true);
			setVisible(true);
		});
	}

	public void update()
	{
		final double FPS = 60.0;
		final double FRAME_TIME = 1_000_000_000 / FPS;
		long last = System.nanoTime();

		while (true)
		{
			long now = System.nanoTime();
			if(now - last >= FRAME_TIME)
			{
				last = now;
				panle.update();
				repaint();
			}

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}
