package userInterface.user;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import org.jxmapviewer.viewer.GeoPosition;

import Database.History;
import Database.Incident;
import Database.PersonnelUtiliser;
import Database.Utiliser;
import map.Map;
import net.proteanit.sql.DbUtils;
import scripts.GUI;
import userInterface.Login;
import userInterface.Window;

public class Manage
{

	public static JPanel Manage;
	static JSplitPane splitPane;
	static JTable table;
	static String etat;
	static int SelectedIdIncident = 0;
	static JButton BOption;
	static JButton BWaiting,BInProgress,BDone;
	static boolean RowSelected = false;
	static float scalex = Window.scalex;
	static float scaley = Window.scaley;

	public static JPanel Init()
	{
		Manage = new JPanel();
		BOption = GUI.NewButton("", 17, 200, 40, 400, 850);
		Connection connection = Login.connection;
		table = new JTable();
		etat = "En Attente";
		RowSelected = false;

		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 300), (int) (scalex * 860), (int) (scaley * 400));
		Manage.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = Incident.GetIncidentByState(etat);
		table.setModel(DbUtils.resultSetToTableModel(rs));

		BWaiting = GUI.NewButton("En attente","res/User/Manage/WaitingIcon.png", 17, 200, 40, 190, 250);
		Manage.add(BWaiting);
		JButton BStart = GUI.NewButton("Traiter", Color.GRAY, 17, 200, 40, 700, 750);
		BInProgress = GUI.NewButton("En cours","res/User/Manage/ProgressIcon.png", Color.GRAY, 17, 200, 40, 390, 250);
		Manage.add(BInProgress);
		JButton BFinish = GUI.NewButton("Est Résolue", Color.GRAY, 17, 200, 40, 700, 750);
		BDone = GUI.NewButton("Traité","res/User/Manage/ResolvedIcon.png", Color.GRAY, 17, 200, 40, 590, 250);
		Manage.add(BDone);

		JPanel BOptions = new JPanel();
		CardLayout cl = new CardLayout();
		BOptions.setLayout(cl);
		BOptions.setBounds((int) (scalex * 400), (int) (scaley * 750), (int) (scalex * 200), (int) (scaley * 40));

		BOptions.add(BStart, "1");
		BOptions.add(BFinish, "2");

		cl.show(BOptions, "1");
		Manage.add(BOptions);

		BWaiting.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				BOptions.setVisible(true);
				etat = "En Attente";
				ResultSet rs = Incident.GetIncidentByState(etat);
				table.setModel(DbUtils.resultSetToTableModel(rs));
				cl.show(BOptions, "1");
				BStart.setBackground(Color.gray);
				BFinish.setBackground(Color.gray);
				resetColor();
				BWaiting.setBackground(new Color(224, 116, 60));
			}
		});

		BStart.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					String date = table.getValueAt(table.getSelectedRow(), 1).toString();
					String Emplacement = table.getValueAt(table.getSelectedRow(), 3).toString();
					String Rue = table.getValueAt(table.getSelectedRow(), 4).toString();
					String commune = table.getValueAt(table.getSelectedRow(), 5).toString();
					String sousType = Incident.getIncidentSubType(SelectedIdIncident);
					User.panel.add(Validation.Init(0, date, sousType, Emplacement, commune, Rue), "2");
					User.cl.show(User.panel, "2");
				}
			}
		});

		BInProgress.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				BOptions.setVisible(true);
				etat = "En Cours";
				ResultSet rs = Incident.GetIncidentByState(etat);
				table.setModel(DbUtils.resultSetToTableModel(rs));
				cl.show(BOptions, "2");
				BStart.setBackground(Color.gray);
				BFinish.setBackground(Color.gray);
				resetColor();
				BInProgress.setBackground(new Color(224, 116, 60));
			}
		});

		BFinish.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIdIncident = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					Incident.setIncidentResolved(SelectedIdIncident);
					Utiliser.SetAllResourceFree(SelectedIdIncident);
					PersonnelUtiliser.SetAllResourceFree(SelectedIdIncident);
					Incident.SetIncidentState(SelectedIdIncident, "Traité");
					Map.clearIncident();
					try
					{
						History.add(Login.username, "Déclarer l'incident " + SelectedIdIncident + " comme 'Traité'");
						connection.commit();
					} catch (SQLException a)
					{
						a.printStackTrace();
					}

					BStart.setBackground(Color.gray);
					BFinish.setBackground(Color.gray);

					ResultSet rs = Incident.GetIncidentByState(etat);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					Map.mapViewer.repaint();
					
				}
			}
		});

		BDone.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				BOptions.setVisible(false);
				ResultSet rs = Incident.GetFinishedIncident();
				table.setModel(DbUtils.resultSetToTableModel(rs));
				BStart.setBackground(Color.gray);
				BFinish.setBackground(Color.gray);
				resetColor();
				BDone.setBackground(new Color(224, 116, 60));
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				SelectedIdIncident = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
				double[] Destination = new double[3];
				GeoPosition IncidentPos = Incident.getIncidentPosition(SelectedIdIncident);
				Destination[0] = IncidentPos.getLatitude();
				Destination[1] = IncidentPos.getLongitude();
				Destination[2] = 15;

				Map.goTo(Destination);
				BStart.setBackground(new Color(224, 116, 60));
				BFinish.setBackground(new Color(224, 116, 60));
				RowSelected = true;
			}
		});

		JButton Back = GUI.NewButton("Retour","res/User/Manage/ReturnIcon.png", Color.black, 17, 200, 40, 400, 800);
		Manage.add(Back);

		Back.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				Map.clearIncident();

				Insertion.Insertion.removeAll();
				Home.Home.removeAll();
				Validation.validation.removeAll();
				Manage.removeAll();
				User.panel.add(Home.Init(), "0");
				User.cl.show(User.panel, "0");
			}
		});

		Manage.add(GUI.NewImage("res/User/Background.png", 960, 1080, 0, 0));
		Manage.setLayout(null);

		return Manage;
	}
	
	static void resetColor()
	{
		BWaiting.setBackground(Color.GRAY);
		BInProgress.setBackground(Color.GRAY);
		BDone.setBackground(Color.GRAY);
	}
}
