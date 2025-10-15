package ikagaka;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import utils.LogWriterNew;

public class Ikagaka
{

	public static void main(String[] args)
	{
		LogWriterNew.deleteLogFile();
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
		{
			showErrorAndExit(throwable);
		});

		new CharacterFrame();
	}

	private static void showErrorAndExit(Throwable t)
	{
		t.printStackTrace(); // ログ出力
		JLabel label = new JLabel("<html><body>致命的なエラーが発生しました。<br>"
				+ t.getClass().getSimpleName() + ": " + t.getMessage() + "</body></html>");
		label.setForeground(Color.RED);
		JOptionPane.showMessageDialog(null, label, "致命的エラー", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public static void exit(String packageId)
	{
		if(packageId != null && !packageId.isEmpty())
		{
			LoadSaveFile.Instance().save(packageId);
		}
		System.exit(0);
	}

}
