package userInterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Database.Utilisateur;
import scripts.Crypting;
import scripts.GUI;

/************************************************************
 * La classe Login permet de créer l'interface graphique de connection de
 * l'utilisateur qui va demander à l'utilisateur de saisir son nom de compte et
 * mot de passe pour se connecter.<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/

public class Login
{
	public static Connection connection;
	public static String username;
	static private JCheckBox RememberUser, RememberPass;
	static private int connectCounter;
	static private long timeCounter;

	public static void init()
	{
		JFrame frame = new JFrame();
		connectCounter = 0;
		timeCounter = System.currentTimeMillis() / 1000000000;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float scaley = (float) screenSize.getHeight() / 1080;
		float scalex = (float) screenSize.getWidth() / 1920;
		
		frame.setSize((int) (650 * scalex), (int) (328 * scaley));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setUndecorated(true);

		frame.add(GUI.NewLabel("Nom d'utilisateur", 20, 290, 80));
		frame.add(GUI.NewLabel("Mot de Passe", 20, 290, 130));
		frame.add(GUI.NewImage("res/Login/ProtectionCivile.png", (int) (650 * 3 / 7), 328, 0, 0));

		JTextField User = GUI.NewTextField(175, 32, 460, 80);
		frame.add(User);
		JPasswordField Pass = GUI.NewPasswordField(175, 32, 460, 130);
		frame.add(Pass);
		User.setText(importUserName());
		Pass.setText(importPassword());

		User.addActionListener(ae ->
		{
			Pass.grabFocus();
		});

		Action Submit = new AbstractAction()
		{
			private static final long serialVersionUID = -7571401136851912153L;
			int secondes;

			public void actionPerformed(ActionEvent arg0)
			{
				if ((System.currentTimeMillis() - importConnectTime()) / 1000 < 20
						&& (System.currentTimeMillis() - importConnectTime()) / 1000 > 0)
				{
					JOptionPane jop = new JOptionPane();
					jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
					jop.setMessage("Vous avez entré un nom d'utilisateur ou un mot de passe incorrect plusieurs fois"
							+ "\n Veuillez attendre " + (20 - (System.currentTimeMillis() - importConnectTime()) / 1000)
							+ " secondes avant de réessayer la connection");
					JDialog dialog = jop.createDialog(null, "Message");
					new Thread(new Runnable()
					{
						public void run()
						{
							while (true)
							{
								try
								{
									dialog.setEnabled(false);
									Thread.sleep(1000);
									secondes = (int) (20 - (System.currentTimeMillis() - importConnectTime()) / 1000);
									jop.setMessage(
											"Vous avez entré un nom d'utilisateur ou un mot de passe incorrect plusieurs fois"
													+ "\n Veuillez attendre "
													+ (20 - (System.currentTimeMillis() - importConnectTime()) / 1000)
													+ " secondes avant de réessayer la connection");
								} catch (Exception e)
								{
								}
								if (secondes == 0)
								{
									dialog.dispose();
									dialog.setEnabled(true);
									jop.setEnabled(false);
									JOptionPane.showMessageDialog(null, "Vous pouvez maintenant se reconnecter ");
									break;
								}
							}
						}
					}).start();
					dialog.setVisible(true);
				} else
				{
					try
					{
						connect(User, Pass);
						connectCounter = 0;
						frame.dispose();
					} catch (Exception e1)
					{
						if (connectCounter < 2)
							connectCounter += 1;
						else
						{
							saveConnectTime();
							connectCounter = 0;
						}
						JOptionPane.showMessageDialog(null, "Connexion echoué , Utilisateur/Mot de passe incorrect",
								"Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};

		Pass.addActionListener(Submit);

		JButton exit = GUI.NewButton("x", 20, 75, 20, 575, 0);
		frame.add(exit);
		RememberUser = GUI.NewCheckBox("Rappeler mon nom d'utilisateur", 290, 180);
		frame.add(RememberUser);
		RememberPass = GUI.NewCheckBox("Rappeler mon mot de passe", 290, 210);
		frame.add(RememberPass);
		RememberPass.setVisible(false);
		RememberUser.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				if (RememberUser.isSelected())
				{
					RememberPass.setVisible(true);
				} else
				{
					RememberPass.setSelected(false);
					RememberPass.setVisible(false);
				}
			}
		});

		if (User.getText().length() > 0)
		{
			RememberUser.setSelected(true);
			if (Pass.getPassword().length > 0)
			{
				RememberPass.setSelected(true);
			}
		}

		JButton login = GUI.NewButton("Connexion","res/User/Login/LoginIcon.png", 20, 180, 40, 360, 250);
		frame.add(login);

		login.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				long currentTime = System.currentTimeMillis() / 1000;
				if (connectCounter < 3 && currentTime > timeCounter + 20)
				{
					try
					{
						connect(User, Pass);
						frame.dispose();
					} catch (Exception e1)
					{
						connectCounter += 1;
						JOptionPane.showMessageDialog(null, "Une erreur est survenue, veuillez réessayer", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				} else
				{
					timeCounter = System.currentTimeMillis() / 1000;
					JOptionPane.showMessageDialog(null,
							"Vous avez entré un nom d'utilisateur ou un mot de passe incorrect plusieurs fois,"
									+ "\nVeuillez attendre " + (timeCounter + 20 - currentTime)
									+ " secondes avant de réessayer la connection",
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		exit.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				System.exit(0);
			}
		});

		frame.setVisible(true);
	}

	static void connect(JTextField User, JPasswordField Pass) throws Exception
	{
		username = User.getText();
		String password = String.valueOf(Pass.getPassword());
		if (RememberUser.isSelected())
		{
			saveUserName(username);
		}
		if (RememberPass.isSelected())
		{
			savePassword(password);
		}
		password = Crypting.EncryptPassword(password);

		if (Utilisateur.connect(username, password))
		{
			if (Utilisateur.getGID(username) == 0)
				new Window("0", username);
			else if (Utilisateur.getGID(username) == 1)
				new Window("1", username);
		} else
			throw new Exception("");
	}

	public static void main(String[] args)
	{
		/*
		 * String url="jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7273503"; String
		 * user="sql7273503"; String password="Gt2v5n2rlc";
		 */

		connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			// Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:sqlite:SQLite.db");
			// connection=DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		init();
	}

	/**
	 * Méthode.<BR>
	 * Permet de sauvegardé le nom d'utilisateur dans un ficher binaire <BR>
	 **/
	static void saveUserName(String S)
	{
		try
		{
			ObjectOutputStream Out = new ObjectOutputStream(new FileOutputStream("UserName.bin"));
			Out.writeObject(S);
			Out.close();
		} catch (Exception e)
		{
		}
	}

	/**
	 * Méthode.<BR>
	 * Permet d'importer le nom d'utilisateur enregistré dans un ficher binaire <BR>
	 **/
	static String importUserName()
	{
		String UserName;
		try
		{
			ObjectInputStream In = new ObjectInputStream(new FileInputStream("UserName.bin"));
			UserName = (String) In.readObject();
			In.close();
		} catch (Exception e)
		{
			UserName = "";
		}
		saveUserName("");
		return UserName;
	}

	/**
	 * Méthode.<BR>
	 * Permet de sauvegarder le mot de passe dans un ficher binaire <BR>
	 **/
	static void savePassword(String S)
	{
		try
		{
			ObjectOutputStream Out = new ObjectOutputStream(new FileOutputStream("Password.bin"));
			Out.writeObject(S);
			Out.close();
		} catch (Exception e)
		{
		}
	}

	/**
	 * Méthode.<BR>
	 * Permet d'importer le nom d'utilisateur enregistré dans un ficher binaire <BR>
	 **/
	static String importPassword()
	{
		String Password;
		try
		{
			ObjectInputStream In = new ObjectInputStream(new FileInputStream("Password.bin"));
			Password = (String) In.readObject();
			In.close();
		} catch (Exception e)
		{
			Password = "";
		}
		savePassword("");
		return Password;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'importer le temps de connection enregistré dans un ficher binaire
	 * <BR>
	 **/

	static long importConnectTime()
	{
		long connectTime = 0;
		try
		{
			ObjectInputStream In = new ObjectInputStream(new FileInputStream("connectTime.bin"));
			connectTime = (long) In.readObject();
			In.close();
		} catch (Exception e)
		{
			connectTime = 0;
		}
		return connectTime;

	}

	/**
	 * Méthode.<BR>
	 * Permet de sauvegarder le temps de la connexion dans un ficher binaire <BR>
	 **/
	static void saveConnectTime()
	{
		ObjectOutputStream Out;
		try
		{
			Out = new ObjectOutputStream(new FileOutputStream("connectTime.bin"));
			Out.writeObject(System.currentTimeMillis());
			Out.close();
		} catch (Exception e)
		{
		}
	}

}