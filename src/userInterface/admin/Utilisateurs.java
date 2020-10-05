package userInterface.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import Database.History;
import Database.Utilisateur;
import net.proteanit.sql.DbUtils;
import userInterface.Login;
import userInterface.Window;
import scripts.Crypting;
import scripts.CustomDialog;
import scripts.GUI;

public class Utilisateurs
{
	private static JPanel Utilisateurs;
	static JSplitPane splitPane;
	static JTable table;
	static JButton Delete, ChangeName, ChangeUser, ChangePass, ChangeType;
	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	static boolean RowSelected;
	static int SelectedIduser;
	static Connection connection;

	public static JPanel Init()
	{
		Utilisateurs = new JPanel();
		connection = Login.connection;
		RowSelected = false;

		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font("Century Gothic", Font.PLAIN, (int) (scalex * 14)));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int) (scalex * 50), (int) (scaley * 50), (int) (scalex * 1600), (int) (scaley * 750));
		Utilisateurs.add(scrollPane);
		scrollPane.setViewportView(table);
		ResultSet rs = Utilisateur.getUsers();
		table.setModel(DbUtils.resultSetToTableModel(rs));

		Delete = GUI.NewButton("Supprimer", Color.gray, 20, 200, 40, 1685, 50);
		Utilisateurs.add(Delete);
		Utilisateurs.add(GUI.NewLabel("Modifier :", 20, 1685, 110));
		ChangeName = GUI.NewButton("Nom complet", Color.gray, 20, 200, 30, 1685, 150);
		Utilisateurs.add(ChangeName);
		ChangeUser = GUI.NewButton("Nom d'utilisateur", Color.gray, 20, 200, 30, 1685, 190);
		Utilisateurs.add(ChangeUser);
		ChangePass = GUI.NewButton("Mot de passe", Color.gray, 20, 200, 30, 1685, 230);
		Utilisateurs.add(ChangePass);
		ChangeType = GUI.NewButton("Type de Compte", Color.gray, 20, 200, 30, 1685, 270);
		Utilisateurs.add(ChangeType);

		ChangeName.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIduser = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					CustomDialog customDialog = new CustomDialog(null, "nom");
					customDialog.pack();
					customDialog.setLocationRelativeTo(null);
					customDialog.setVisible(true);
					if (customDialog.getValidatedText() != null)
					{
						Utilisateur.ChangeName(SelectedIduser, customDialog.getValidatedText());
						try
						{
							History.add(Login.username, "Changer le Nom de " + SelectedIduser);
							connection.commit();
						} catch (SQLException e1)
						{
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
					}

					resetTable();
				}
			}
		});
		ChangeUser.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					if (SelectedIduser == Utilisateur.getID(Login.username))
					{
						JOptionPane.showMessageDialog(null,
								"Vous ne pouvez pas modifier le compte auquel vous êtes connecté", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					} else
					{
						SelectedIduser = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
						CustomDialog customDialog = new CustomDialog(null, "nom d'utilateur");
						customDialog.pack();
						customDialog.setLocationRelativeTo(null);
						customDialog.setVisible(true);
						if (customDialog.getValidatedText() != null)
						{
							try
							{
								Utilisateur.ChangeUser(SelectedIduser, customDialog.getValidatedText());
								History.add(Login.username, "Changer le Nom d'utilisateur de " + SelectedIduser);
							} catch (SQLException ex)
							{
								JOptionPane.showMessageDialog(null,
										"Le nom d'utilisateur existe deja, veuillez réessayer.", "Erreur",
										JOptionPane.ERROR_MESSAGE);
							}
							try
							{
								connection.commit();
							} catch (SQLException e1)
							{
								JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.",
										"Erreur", JOptionPane.ERROR_MESSAGE);
							}
						}

						resetTable();
					}
				}
			}
		});
		ChangePass.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIduser = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					CustomDialog customDialog = new CustomDialog(null, "mot de passe", true);
					customDialog.pack();
					customDialog.setLocationRelativeTo(null);
					customDialog.setVisible(true);
					if (customDialog.getValidatedText() != null)
					{
						String password = Crypting.EncryptPassword(customDialog.getValidatedText());
						Utilisateur.ChangePass(SelectedIduser, password);
						try
						{
							History.add(Login.username, "Changer le Mot de passe de " + SelectedIduser);
							connection.commit();
						} catch (SQLException e1)
						{
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					resetTable();
				}
			}
		});
		ChangeType.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIduser = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					if (SelectedIduser == Utilisateur.getID(Login.username))
					{
						JOptionPane.showMessageDialog(null,
								"Vous ne pouvez pas modifier le type du compte auquel vous êtes connecté", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					} else
					{
						int gid = Utilisateur.getGID(Utilisateur.getUser(SelectedIduser));
						if (gid == 0)
						{
							Utilisateur.ChangeType(SelectedIduser, "Utilisateur");
						} else
						{
							Utilisateur.ChangeType(SelectedIduser, "Administrateur");
						}

						try
						{
							History.add(Login.username, "Changer le type du compte " + SelectedIduser);
							connection.commit();
						} catch (SQLException e1)
						{
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
						setButtonColor(Color.GRAY);
						resetTable();
					}
				}
			}
		});

		Delete.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (RowSelected)
				{
					SelectedIduser = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
					if (SelectedIduser == Utilisateur.getID(Login.username))
					{
						JOptionPane.showMessageDialog(null,
								"Vous ne pouvez pas supprimer le compte auquel vous êtes connecté", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					} else
					{
						Utilisateur.Delete(SelectedIduser);
						try
						{
							History.add(Login.username, "Suppimer le compte " + SelectedIduser);
							connection.commit();

						} catch (SQLException e1)
						{
							JOptionPane.showMessageDialog(null, "Erreur de connexion, veuillez réessayer.", "Erreur",
									JOptionPane.ERROR_MESSAGE);
						}
						resetTable();
					}
				}
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				RowSelected = true;
				setButtonColor(new Color(100, 150, 215));
			}
		});

		String[] Types =
		{ "Administrateur", "Utilisateur" };

		Utilisateurs.add(GUI.NewLabel("Ajouter :", 20, 50, 860));

		Utilisateurs.add(GUI.NewLabel("Nom Complet :", 20, 220, 810));
		JTextField Name = GUI.NewTextField(200, 35, 200, 850);
		Utilisateurs.add(Name);

		Utilisateurs.add(GUI.NewLabel("Nom d'utilisateur :", 20, 520, 810));
		JTextField User = GUI.NewTextField(200, 35, 500, 850);
		Utilisateurs.add(User);

		Utilisateurs.add(GUI.NewLabel("Mot de passe :", 20, 820, 810));
		JTextField Pass = GUI.NewTextField(200, 35, 800, 850);
		Utilisateurs.add(Pass);

		Utilisateurs.add(GUI.NewLabel("Type de Compte :", 20, 1120, 810));
		JComboBox<String> Type = GUI.NewComboBox(Types, 220, 35, 1100, 850);
		Utilisateurs.add(Type);

		JButton AddUser = GUI.NewButton("Ajouter", new Color(100, 150, 215), 20, 150, 40, 1425, 850);
		Utilisateurs.add(AddUser);
		AddUser.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int IdUser = Utilisateur.GetuserCount() + 1;
				String nom = Name.getText();
				String username = User.getText();
				String password = Crypting.EncryptPassword(Pass.getText());
				int GroupId = Type.getSelectedIndex();
				if (nom.length() > 0 && username.length() > 0 && Pass.getText().length() > 0)
				{
					try
					{
						Utilisateur.Add(IdUser, nom, username, password, GroupId);
						History.add(Login.username, "Ajouter le compte " + IdUser);
						ResultSet rs = Utilisateur.getUsers();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						connection.commit();
						Name.setText("");
						User.setText("");
						Pass.setText("");
					} catch (Exception ex)
					{
						JOptionPane.showMessageDialog(null, "Veuillez Vérifier vos entrées.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				} else
				{
					JOptionPane.showMessageDialog(null, "Veuillez Vérifier vos entrées.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		Utilisateurs.add(GUI.NewImage("res/User/AdminBackground.png", 1920, 1080, 0, 0));

		Utilisateurs.setLayout(null);
		return Utilisateurs;
	}

	public static void setButtonColor(Color color)
	{
		Delete.setBackground(color);
		ChangeName.setBackground(color);
		ChangeUser.setBackground(color);
		ChangePass.setBackground(color);
		ChangeType.setBackground(color);
	}

	public static void resetTable()
	{
		ResultSet rs = Utilisateur.getUsers();
		table.setModel(DbUtils.resultSetToTableModel(rs));
		table.clearSelection();
		setButtonColor(Color.GRAY);
		RowSelected = false;
	}
}
