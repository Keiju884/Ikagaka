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

	public CharacterFrame()
	{
		super("如何か");
		List<CharacterModel> list = LoadCharacter.Instance().loadAllCharacter();
		if(list == null || list.size() <= 0)
		{
			JLabel label = new JLabel("Characterが存在しません");
			label.setForeground(Color.RED);
			JOptionPane.showMessageDialog(this, label, "エラー", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		this.characterList = list;
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
				try
				{
					panle.update(); // ←ここで例外が出る可能性あり
					repaint();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					// 例外発生時にアプリ終了
					JOptionPane.showMessageDialog(this,
							"致命的なエラーが発生しました: " + e.getMessage(),
							"エラー", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
			}

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}
	}
}
