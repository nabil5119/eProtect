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
 * La classe CustomDialog contient les methodes qui permettent de créer et gerer
 * des fenêtres personalisés ( JDialog ) qui vont servir à donner à
 * l'utilisateur la possibilté de changer quelques attributs (selon la
 * possiblité et le privilège) ainsi que confirmer ou annuler on choix.
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
	 * Constructeur paramétré.<BR>
	 * Permet de créer une fenêtre personalisé ( JDialog ) de titre "Modifier" qui
	 * contient un message qui représente l'attribut à changer, un espace text qui
	 * va récuperer la nouvelle valeur entré par l'utilisateur et deux boutons :
	 * "Valider" et "Annuler" pour confirmer ou annuler le changement.<BR>
	 * 
	 * @param aFrame        Le frame père de cette fenêtre ( JDialog ) crée.
	 * @param ValueToChange L'attribut qu'on souhaite changé ( qui sera afficher
	 *                      comme message ).
	 **/
	public CustomDialog(Frame aFrame, String ValueToChange)
	{
		this(aFrame, ValueToChange, false);
	}

	/**
	 * Constructeur paramétré.<BR>
	 * Permet de créer une fenêtre personalisé ( JDialog ) de titre "Modifier" qui
	 * contient un message qui représente l'attribut à changer, un espace text qui
	 * va récuperer la nouvelle valeur entré par l'utilisateur et deux boutons :
	 * "Valider" et "Annuler" pour confirmer ou annuler le changement.<BR>
	 * 
	 * @param aFrame        Le frame père de cette fenêtre ( JDialog ) crée.
	 * @param ValueToChange L'attribut qu'on souhaite changé ( qui sera afficher
	 *                      comme message ).
	 * @param ispassword    Un boolean qui va servir pour savoir si la valeur qui
	 *                      sera entré par l'utilsateur est un mot de passe ou non
	 *                      pour savoir si on va utiliser un espace text normal ou
	 *                      un espace pour les mots de passes.
	 **/
	public CustomDialog(Frame aFrame, String ValueToChange, boolean ispassword)
	{
		super(aFrame, true);
		this.ispassword = ispassword;

		setTitle("Modifier");

		String msgString1 = "Veuillez insérer le nouveau " + ValueToChange;
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
	 * Méthode. <BR>
	 * Cette méthode permet de vider l'espace text de la fenêtre crée ainsi que de
	 * la rendre invisible.
	 **/
	public void clearAndHide()
	{
		textField.setText(null);
		setVisible(false);
	}

	/**
	 * Méthode. <BR>
	 * Cette methode permet de capturer le contenu de l'epace text ( l'espace normal
	 * ou des mots de passes ) pour la fenêtre crée après vérification que cette
	 * fenêtre est visible, que l'utilisateur a confirmé son choix ( le bouton
	 * choisi est "Valider" ) et que l'espace text n'est pas vide ( l'utilisateur a
	 * entré quelque chose de valide ).
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
	 * Méthode. <BR>
	 * Cette méthode permet de retourner la valeur entré dans l'espace text capturé
	 * par la méthode @see {@link CustomDialog#propertyChange(PropertyChangeEvent)}
	 * 
	 * @return la valeur entré dans l'espace text par l'utilisateur.
	 */
	public String getValidatedText()
	{
		return typedText;
	}
}
