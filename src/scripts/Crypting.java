package scripts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/************************************************************
 * La classe Crypting contient les méthodes de cryptage des Chaînes de
 * caractères et des mots de passe .<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class Crypting
{

	/**
	 * Méthode.<BR>
	 * Permet de seulement prendre en considération les caractères qui appartient au
	 * code ASCII.<BR>
	 * 
	 * @param line la ligne qu'on veut coder.
	 *
	 * @return la chaine de caractère codé.
	 */
	public static String CharCoding(String line)
	{
		String ret = "";
		for (char c : line.toCharArray())
		{
			if ((int) c > 0 && (int) c < 255)
			{
				ret += c;
			}
		}
		return ret;
	}

	/**
	 * Méthode.<BR>
	 * Permet de crypter un mot de passe par la méthode md5.
	 * 
	 * @param password Le mot de passe à crypter.
	 * 
	 * @return le mot de passe crypté.
	 */
	public static String EncryptPassword(String password)
	{
		String generatedPassword = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

}
