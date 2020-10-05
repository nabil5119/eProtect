package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import userInterface.Window;

/************************************************************
 * La classe GUI(Graphic user interface) permet de cr�er les �lements graphiques
 * qu'on va utiliser dans le reste de l'application.<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class GUI
{

	static float scalex = Window.scalex;
	static float scaley = Window.scaley;
	
	// --------------------ScreenShot-----------------------------------------------------------------------------------
	static public BufferedImage screenShot(int x, int y, int w, int h)
	{
		try
		{
			Robot rbt = new Robot();
			BufferedImage background = rbt.createScreenCapture(new Rectangle((int) (GUI.scalex * x),
					(int) (GUI.scalex * y), (int) (GUI.scalex * w), (int) (GUI.scalex * h)));
			return background;
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	// -------------------NewLabel--------------------------------------------------------------------------------------
	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une JLabel en donnant son contenu(text), couleur, taille,
	 * largeur, hauteur, et ses coordonn�es x et y.<BR>
	 * Le JLabel est position� dans la partie gauche du Layout.<BR>
	 * 
	 * @param text   Le contenu du JLabel.
	 * @param color  Le couleur du JLabel.
	 * @param size   La taille du JLabel.
	 * @param width  La largeur du JLabel.
	 * @param height La hauteur du JLabel.
	 * @param x      La coordonn� du axe des abscisses du JLabel.
	 * @param y      La coordonn� du axe des ordonn� du JLabel.
	 *
	 * @return le JLabel cr�e
	 */
	public static JLabel NewLabel(String text, Color color, int size, int width, int height, int x, int y)
	{
		JLabel Label = new JLabel(text);
		Label.setFont(new Font("Century Gothic", Font.ITALIC, (int) (GUI.scalex * size)));
		Label.setForeground(color);
		Label.setBounds((int) (GUI.scalex * x), (int) (GUI.scaley * y), (int) (GUI.scalex * width),
				(int) (GUI.scaley * height));
		Label.setHorizontalAlignment(SwingConstants.LEFT);
		return Label;
	}

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une JLabel en donnant son contenu(text), taille, et ses
	 * coordonn�es x et y.<BR>
	 * Le JLabel est position� dans la partie gauche du Layout.<BR>
	 * 
	 * @param text Le contenu du JLabel.
	 * @param size La taille du JLabel.
	 * @param x    La coordonn� du axe des abscisses du JLabel.
	 * @param y    La coordonn� du axe des ordonn� du JLabel.
	 *
	 * @return le JLabel cr�e
	 */
	public static JLabel NewLabel(String text, int size, int x, int y)
	{
		return GUI.NewLabel(text, Color.BLACK, size, x, y);
	}

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une JLabel en donnant son contenu(text), couleur, taille, et
	 * ses coordonn�es x et y.<BR>
	 * Le JLabel est position� dans la partie gauche du Layout.<BR>
	 * 
	 * @param text  Le contenu du JLabel.
	 * @param color Le couleur du JLabel.
	 * @param size  La taille du JLabel.
	 * @param x     La coordonn� du axe des abscisses du JLabel.
	 * @param y     La coordonn� du axe des ordonn� du JLabel.
	 *
	 * @return le JLabel cr�e
	 */
	public static JLabel NewLabel(String text, Color color, int size, int x, int y)
	{
		return NewLabel(text, color, size, (int) (text.length() * size / 1.3), (int) (size * 1.2), x, y);
	}

	// --------------------NewButton------------------------------------------------------------------------------------
	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une JButton en donnant son contenu(text), couleur, taille,
	 * largeur, hauteur, et ses coordonn�es x et y.<BR>
	 * Le JLabel est position� dans la partie gauche du Layout.<BR>
	 * 
	 * @param text   Le contenu du JButton.
	 * @param color  Le couleur du JButton.
	 * @param size   La taille du JButton.
	 * @param width  La largeur du JButton.
	 * @param height La hauteur du JButton.
	 * @param x      La coordonn� du axe des abscisses du JButton.
	 * @param y      La coordonn� du axe des ordonn� du JButton.
	 *
	 * @return le JButton cr�e
	 */
	public static JButton NewButton(String text,String path, Color color, int size, int width, int height, int x, int y)
	{
		JButton Button;
		if (path==null)
		{
			Button = new JButton(text);
		}
		else 
		{
			ImageIcon image=new ImageIcon(path);
			Image img = image.getImage();
			Image newimg = img.getScaledInstance((int) (GUI.scalex * (height-5)), (int) (GUI.scaley * (height-5)),java.awt.Image.SCALE_SMOOTH);
			image = new ImageIcon(newimg);
			Button = new JButton(text,image);
		}
		Button.setBackground(color);
		Button.setFont(new Font("Century Gothic", Font.BOLD, (int) (GUI.scalex * size)));
		Button.setForeground(Color.WHITE);
		Button.setLocation((int) (GUI.scalex * x), (int) (GUI.scaley * y));
		Button.setSize((int) (GUI.scalex * width), (int) (GUI.scaley * height));
		Button.setFocusPainted(false);

		return Button;
	}

	public static JButton NewButton(String text, Color color, int size, int width, int height, int x, int y)
	{
		return GUI.NewButton(text,null,color, size, width, height,x, y);
	}
	public static JButton NewButton(String test, int x, int y)
	{
		return GUI.NewButton(test, 20, 120, 40, x, y);
	}

	public static JButton NewButton(String test, int size, int x, int y)
	{
		return GUI.NewButton(test, size, 120, 40, x, y);
	}

	public static JButton NewButton(String test, Color color, int x, int y)
	{
		return NewButton(test, color, 20, 120, 40, x, y);
	}

	public static JButton NewButton(String test, Color color, int size, int x, int y)
	{
		return NewButton(test, color, size, 120, 40, x, y);
	}

	public static JButton NewButton(String text, int size, int width, int height, int x, int y)
	{
		return NewButton(text, new Color(224, 116, 60), size, width, height, x, y);
	}
	public static JButton NewButton(String text,String path, int size, int width, int height, int x, int y)
	{
		return NewButton(text,path, new Color(224, 116, 60), size, width, height, x, y);
	}

	// ----------------------NewTextPane--------------------------------------------------------------------------------
	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une JTextField en donnant sa largeur, hauteur, et ses
	 * coordonn�es x et y.<BR>
	 * 
	 * @param width  La largeur du JTextField.
	 * @param height La hauteur du JTextField.
	 * @param x      La coordonn� du axe des abscisses du JTextField.
	 * @param y      La coordonn� du axe des ordonn� du JTextField.
	 *
	 * @return le JTextField cr�e
	 */
	public static JTextField NewTextField(int width, int height, int x, int y)
	{
		JTextField TextField = new JTextField();
		TextField.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scalex * (height - 13))));
		TextField.setLocation((int) (GUI.scalex * x), (int) (GUI.scaley * y));
		TextField.setSize((int) (GUI.scalex * width), (int) (GUI.scaley * height));
		return TextField;
	}

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une JPasswordField en donnant sa largeur, hauteur, et ses
	 * coordonn�es x et y.<BR>
	 * 
	 * @param width  La largeur du JPasswordField.
	 * @param height La hauteur du JPasswordField.
	 * @param x      La coordonn� du axe des abscisses du JPasswordField.
	 * @param y      La coordonn� du axe des ordonn� du JPasswordField.
	 *
	 * @return le JPasswordField cr�e
	 */
	public static JPasswordField NewPasswordField(int width, int height, int x, int y)
	{
		JPasswordField PasswordFiel = new JPasswordField();
		PasswordFiel.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scalex * (height - 13))));
		PasswordFiel.setLocation((int) (GUI.scalex * x), (int) (GUI.scaley * y));
		PasswordFiel.setSize((int) (GUI.scalex * width), (int) (GUI.scaley * height));
		return PasswordFiel;
	}

	// -------------------NewImage--------------------------------------------------------------------------------------
	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une en donnant sa largeur, hauteur, et ses coordonn�es x et
	 * y.<BR>
	 * 
	 * @param path Le chemin absolu du fichier dont on va �xtraire l'image.
	 * @param w    La largeur du l'image.
	 * @param h    La hauteur du l'image.
	 * @param x    La coordonn� du axe des abscisses du l'image.
	 * @param y    La coordonn� du axe des ordonn� de l'image.
	 *
	 * @return le JLabel cr�e qui contient l'image
	 */

	public static JLabel NewImage(String path, int w, int h, int x, int y)
	{
		ImageIcon img = new ImageIcon(path);
		JLabel label = new JLabel();

		Image image = img.getImage();
		Image newimg = image.getScaledInstance((int) (GUI.scalex * w), (int) (GUI.scaley * h),
				java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newimg);
		label.setBounds(x, y, img.getIconWidth(), img.getIconHeight());
		label.setIcon(img);
		return label;
	}

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une image en donnant son chemin, et ses coordonn�es x et
	 * y.<BR>
	 * 
	 * @param path Le chemin absolu du fichier dont on va �xtraire l'image.
	 * @param x    La coordonn� du axe des abscisses du l'image.
	 * @param y    La coordonn� du axe des ordonn� de l'image.
	 *
	 * @return le JLabel cr�e qui contient l'image
	 */
	public static JLabel NewImage(String path, int x, int y)
	{
		ImageIcon img = new ImageIcon(path);
		return NewImage(path, img.getIconWidth(), img.getIconHeight(), x, y);
	}

	// --------------------NewComboBox----------------------------------------------------------------------------------
	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er un JComboBox en donnant la liste de ses composantes, sa
	 * largeur, hauteur, et ses coordonn�es x et y.<BR>
	 * 
	 * @param list La liste des objets du JComboBox.
	 * 
	 * @param w    La largeur du JComboBox.
	 * @param h    La hauteur du JComboBox.
	 * @param x    La coordonn� du axe des abscisses du JComboBox.
	 * @param y    La coordonn� du axe des ordonn� de JComboBox.
	 *
	 * @return le ComboBox cr�e
	 */
	public static JComboBox<String> NewComboBox(String[] list, int w, int h, int x, int y)
	{
		JComboBox<String> ComboBox = new JComboBox<>(list);
		ComboBox.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scalex * 18)));
		ComboBox.setBounds((int) (GUI.scalex * x), (int) (GUI.scaley * y), (int) (GUI.scalex * w),
				(int) (GUI.scaley * h));
		ComboBox.setBackground(Color.white);
		return ComboBox;
	}

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er un JComboBox en donnant la liste de ses composantes, sa
	 * largeur, hauteur, et ses coordonn�es x et y.<BR>
	 * 
	 * @param list La liste des objets du JComboBox.
	 * @param x    La coordonn� du axe des abscisses du JComboBox.
	 * @param y    La coordonn� du axe des ordonn� de JComboBox.
	 *
	 * @return le ComboBox cr�e
	 */
	public static JComboBox<String> NewComboBox(String[] list, int x, int y)
	{
		return NewComboBox(list, 350, 35, x, y);
	}

	// --------------------NewCheckBox----------------------------------------------------------------------------------
	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er un JCheckBox en donnant son contenu et ses coordonn�es x et
	 * y.<BR>
	 * 
	 * @param text La liste des objets du JComboBox.
	 * 
	 * @param x    La coordonn� du axe des abscisses du JComboBox.
	 * @param y    La coordonn� du axe des ordonn� de JComboBox.
	 *
	 * @return le JCheckBox cr�e
	 */
	public static JCheckBox NewCheckBox(String text, int x, int y)
	{

		JCheckBox CheckBox = new JCheckBox(text);
		CheckBox.setFont(new Font("Century Gothic", Font.PLAIN, (int) (GUI.scalex * 18)));
		CheckBox.setBounds((int) (GUI.scalex * x), (int) (GUI.scalex * y), (int) (GUI.scalex * text.length() * 12),
				(int) (GUI.scalex * 20));
		return CheckBox;
	}
	

}
