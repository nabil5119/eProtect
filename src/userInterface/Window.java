package userInterface;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import userInterface.admin.Admin;
import userInterface.user.User;

/***************************************************************************************************
 * La classe Windows permet d'afficher pour chacun des deux types d'utilisateurs
 * son interface associée.
 * 
 * @author ASEDS.
 * @version 7.02
 ***************************************************************************************************/
public class Window
{
	JFrame frame = new JFrame();

	static JPanel panelCont = new JPanel();
	static CardLayout cl = new CardLayout();

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static float scalex = (float) screenSize.getWidth() / 1920;
	public static float scaley = (float) screenSize.getHeight() / 1080;

	/**
	 * Constructeur paramétré.<BR>
	 * Permet d'afficher à chacun des deux types d'utilisateurs son interface
	 * associée.
	 * 
	 * @param GID      Le type de compte.
	 * @param username Le nom d'utilisateur.
	 */
	public Window(String GID, String username)
	{
		if (GID.equals("0"))
			Admin.init();
		if (GID.equals("1"))
			User.init();
	}
}