package userInterface.admin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import scripts.GUI;
import userInterface.Login;
import userInterface.user.User;

public class Admin
{
	static JFrame frame;
	static JPanel tabs, buttomPanel;
	static JPanel use, cas, his, cal;
	static CardLayout cl;
	static JSplitPane splitPane;

	public static void init()
	{
		frame = new JFrame();
		tabs = new JPanel();
		buttomPanel = new JPanel();
		cl = new CardLayout();

		JButton UserTab = GUI.NewButton("Utilisateurs", 15, 0, 0);
		tabs.add(UserTab);
		JButton CaserneTab = GUI.NewButton("Casernes", 15, 120, 0);
		tabs.add(CaserneTab);
		JButton ResourceTab = GUI.NewButton("Ressources", 15, 240, 0);
		tabs.add(ResourceTab);
		JButton IncidentTab = GUI.NewButton("Incidents", 15, 360, 0);
		tabs.add(IncidentTab);
		JButton CallTab = GUI.NewButton("Appels", 15, 480, 0);
		tabs.add(CallTab);
		JButton HistoryTab = GUI.NewButton("Historique", 15, 600, 0);
		tabs.add(HistoryTab);

		JButton userMode = GUI.NewButton("Mode Utilisateur", new Color(75, 75, 75), 15, 160, 40, 1920 - 160 * 3, 0);
		tabs.add(userMode);
		JButton Disconnect = GUI.NewButton("Déconnecter", new Color(75, 75, 75), 15, 160, 40, 1920 - 160 * 2, 0);
		tabs.add(Disconnect);
		JButton Exit = GUI.NewButton("Quitter", new Color(75, 75, 75), 15, 160, 40, 1920 - 160, 0);
		tabs.add(Exit);
		tabs.setLayout(null);

		buttomPanel.setLayout(cl);
		use = Utilisateurs.Init();
		cas = Casernes.Init();
		his = Histories.Init();
		cal = Calls.Init();
		buttomPanel.add(use, "1");
		buttomPanel.add(cas, "2");
		buttomPanel.add(his, "3");
		buttomPanel.add(cal, "4");
		buttomPanel.add(Incidents.Init(), "5");
		buttomPanel.add(TypeResources.Init(), "6");
		cl.show(buttomPanel, "1");

		userMode.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				User.init();
			}
		});

		UserTab.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				cl.show(buttomPanel, "1");
			}
		});
		CaserneTab.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				cl.show(buttomPanel, "2");
			}
		});
		HistoryTab.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Histories.update();
				cl.show(buttomPanel, "3");
			}
		});

		CallTab.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				cl.show(buttomPanel, "4");
			}
		});
		IncidentTab.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Incidents.update();
				cl.show(buttomPanel, "5");
			}
		});
		ResourceTab.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				cl.show(buttomPanel, "6");
			}
		});

		Disconnect.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					Login.connection.close();
				} catch (SQLException e1)
				{
					e1.printStackTrace();
				}
				Admin.frame.dispose();
				Login.main(null);
			}
		});
		Exit.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				System.exit(0);
			}
		});

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabs, buttomPanel);
		splitPane.setDividerLocation(40);
		splitPane.setEnabled(false);

		frame.add(splitPane);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setResizable(true);
		frame.setVisible(true);
	}
}