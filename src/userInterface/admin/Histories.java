package userInterface.admin;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.*;

import Database.History;
import net.proteanit.sql.DbUtils;
import scripts.GUI;
import userInterface.Login;
import userInterface.Window;

public class Histories
{
	private static JPanel Histories;
	static JSplitPane splitPane;
	static JTable table;
	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	static Connection connection;

	public static JPanel Init()
	{
		Histories = new JPanel();
		connection = Login.connection;
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 20), (int) (scalex * 1820), (int) (scaley * 900));
		Histories.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = History.getHistory();
		table.setModel(DbUtils.resultSetToTableModel(rs));

		Histories.add(GUI.NewImage("res/User/AdminBackground.png", 1920, 1080, 0, 0));

		Histories.setLayout(null);
		return Histories;
	}

	public static void update()
	{
		ResultSet rs = History.getHistory();
		table.setModel(DbUtils.resultSetToTableModel(rs));
	}

}
