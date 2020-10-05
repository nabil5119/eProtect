package scripts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/************************************************************
 * La classe Crypting contient les m�thodes de cryptage des Cha�nes de
 * caract�res et des mots de passe .<BR>
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class Crypting
{

	/**
	 * M�thode.<BR>
	 * Permet de seulement prendre en consid�ration les caract�res qui appartient au
	 * code ASCII.<BR>
	 * 
	 * @param line la ligne qu'on veut coder.
	 *
	 * @return la chaine de caract�re cod�.
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
	 * M�thode.<BR>
	 * Permet de crypter un mot de passe par la m�thode md5.
	 * 
	 * @param password Le mot de passe � crypter.
	 * 
	 * @return le mot de passe crypt�.
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
