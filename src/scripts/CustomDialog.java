package scripts;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
/************************************************************
 * La classe CustomDialog contient les methodes qui permettent de cr�er et gerer
 * des fen�tres personalis�s ( JDialog ) qui vont servir � donner �
 * l'utilisateur la possibilt� de changer quelques attributs (selon la
 * possiblit� et le privil�ge) ainsi que confirmer ou annuler on choix.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class CustomDialog extends JDialog implements ActionListener, PropertyChangeListener
{
	private String typedText = null;
	private String typed;

	private JTextField textField;
	private JPasswordField passwordField;
	private JOptionPane optionPane;

	private String btnString1 = "Valider";
	private String btnString2 = "Annuler";

	private boolean ispassword;

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une fen�tre personalis� ( JDialog ) de titre "Modifier" qui
	 * contient un message qui repr�sente l'attribut � changer, un espace text qui
	 * va r�cuperer la nouvelle valeur entr� par l'utilisateur et deux boutons :
	 * "Valider" et "Annuler" pour confirmer ou annuler le changement.<BR>
	 * 
	 * @param aFrame        Le frame p�re de cette fen�tre ( JDialog ) cr�e.
	 * @param ValueToChange L'attribut qu'on souhaite chang� ( qui sera afficher
	 *                      comme message ).
	 **/
	public CustomDialog(Frame aFrame, String ValueToChange)
	{
		this(aFrame, ValueToChange, false);
	}

	/**
	 * Constructeur param�tr�.<BR>
	 * Permet de cr�er une fen�tre personalis� ( JDialog ) de titre "Modifier" qui
	 * contient un message qui repr�sente l'attribut � changer, un espace text qui
	 * va r�cuperer la nouvelle valeur entr� par l'utilisateur et deux boutons :
	 * "Valider" et "Annuler" pour confirmer ou annuler le changement.<BR>
	 * 
	 * @param aFrame        Le frame p�re de cette fen�tre ( JDialog ) cr�e.
	 * @param ValueToChange L'attribut qu'on souhaite chang� ( qui sera afficher
	 *                      comme message ).
	 * @param ispassword    Un boolean qui va servir pour savoir si la valeur qui
	 *                      sera entr� par l'utilsateur est un mot de passe ou non
	 *                      pour savoir si on va utiliser un espace text normal ou
	 *                      un espace pour les mots de passes.
	 **/
	public CustomDialog(Frame aFrame, String ValueToChange, boolean ispassword)
	{
		super(aFrame, true);
		this.ispassword = ispassword;

		setTitle("Modifier");

		String msgString1 = "Veuillez ins�rer le nouveau " + ValueToChange;
		textField = new JTextField(20);
		passwordField = new JPasswordField(20);
		Object[] array =
		{ msgString1, textField };
		if (ispassword)
		{
			array[1] = passwordField;
		}
		Object[] options =
		{ btnString1, btnString2 };

		optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options,
				options[0]);

		setContentPane(optionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		textField.addActionListener(this);
		optionPane.addPropertyChangeListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		optionPane.setValue(btnString1);
	}

	/**
	 * M�thode. <BR>
	 * Cette m�thode permet de vider l'espace text de la fen�tre cr�e ainsi que de
	 * la rendre invisible.
	 **/
	public void clearAndHide()
	{
		textField.setText(null);
		setVisible(false);
	}

	/**
	 * M�thode. <BR>
	 * Cette methode permet de capturer le contenu de l'epace text ( l'espace normal
	 * ou des mots de passes ) pour la fen�tre cr�e apr�s v�rification que cette
	 * fen�tre est visible, que l'utilisateur a confirm� son choix ( le bouton
	 * choisi est "Valider" ) et que l'espace text n'est pas vide ( l'utilisateur a
	 * entr� quelque chose de valide ).
	 */
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();

		if (isVisible() && (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop)))
		{
			Object value = optionPane.getValue();

			if (btnString1.equals(value) && (textField.getText() != null || passwordField.getPassword() != null))
			{
				if (ispassword)
				{
					typed = String.valueOf(passwordField.getPassword());
				} else
				{
					typed = textField.getText();
				}

				typedText = typed;
				clearAndHide();
			} else
			{
				typedText = null;
				clearAndHide();
			}
		}
	}

	/**
	 * M�thode. <BR>
	 * Cette m�thode permet de retourner la valeur entr� dans l'espace text captur�
	 * par la m�thode @see {@link CustomDialog#propertyChange(PropertyChangeEvent)}
	 * 
	 * @return la valeur entr� dans l'espace text par l'utilisateur.
	 */
	public String getValidatedText()
	{
		return typedText;
	}
}
