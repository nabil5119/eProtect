package userInterface.user;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import map.Map;
import userInterface.Window;

public class User
{
	static JFrame frame;
	public static JPanel panel;
	public static CardLayout cl;
	public static JSplitPane splitPane;
	static JPanel map;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static void init()
	{
		float scalex = Window.scalex;
		float scaley = Window.scaley;
		frame = new JFrame();
		panel = new JPanel();
		cl = new CardLayout();
		panel.setLayout(cl);

		panel.add(Home.Init(), "0");

		cl.show(panel, "0");

		map = Map.newMap();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, map, panel);
		splitPane.setDividerLocation((int) screenSize.getWidth() / 2);
		splitPane.setEnabled(false);

		frame.setUndecorated(true);
		frame.add(splitPane);
		frame.setSize((int) (1920 * scalex), (int) (1080 * scaley));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(true);
		frame.setVisible(true);
	}
}