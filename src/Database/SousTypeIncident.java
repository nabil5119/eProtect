package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import userInterface.Login;

/************************************************************
 * La classe SousTypeIncident contient les methodes qui permettent la gestion
 * des sous types d'incidents dans l'application.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class SousTypeIncident
{
	/**
	 * Méthode.<BR>
	 * Permet d'avoir les sous types d'un type d'incident par le biais de son
	 * identifiant.
	 *
	 * @param IdTypeIncident l'identifiant du type d'incident dont on cherche les
	 *                       sous types.
	 *
	 * @return une liste de chaine de carctère qui contients les sous types
	 *         d'incidents associé à ce type.
	 **/
	public static String[] getSubTypeForType(int IdTypeIncident)
	{
		Connection connection = Login.connection;

		String query = "SELECT Nom FROM soustypeincident where IdTypeIncident=" + IdTypeIncident + ";";
		try
		{
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int rowCount = 0;
			while (resultSet.next())
			{
				rowCount += 1;
			}
			String[] ret = new String[rowCount];
			int row = rowCount - 1;
			while (resultSet.previous())
			{
				ret[row] = resultSet.getString(1);
				row -= 1;
			}
			return ret;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		;
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir tous les sous types disponibles ( pour tous les types
	 * d'incidents ).
	 *
	 * @return une liste de listes (matrice) de chaine de carctère qui contients les
	 *         sous types d'incidents associés à tous les types La taille de cette
	 *         matrice est trouvé par les deux méthodes : @see
	 *         {@link TypeIncident#TypeCount()} qui permet d'avoir le nombre total
	 *         de type d'incidents disponible ( nombre de lignes ) , @see
	 *         {@link SousTypeIncident#getMaxSubTypeCount()} qui permet d'avoir le
	 *         plus grand de sous types d'incidents pour un type disponible qui sera
	 *         le nombre des colonnes pour cette matrice.
	 **/
	public static String[][] getSubType()
	{
		Connection connection = Login.connection;

		try
		{
			int TypeCount = TypeIncident.TypeCount();
			int MaxSubType = getMaxSubTypeCount();
			String[][] ret = new String[TypeCount][MaxSubType];

			for (int i = 0; i < TypeCount; i++)
			{
				String query = "SELECT Nom FROM soustypeincident where IdTypeIncident=" + i + ";";
				Statement statement = (Statement) connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				int j = 0;
				while (resultSet.next())
				{
					ret[i][j] = resultSet.getString(1);
					j += 1;
				}
			}
			return ret;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		;
		return null;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir le plus grand nombre de sous incidents disponibles pour un
	 * type d'incident.
	 * 
	 * @return un entier qui représente le plus grand nombre de sous incidents
	 *         disponibles pour un type d'incident.
	 **/
	public static int getMaxSubTypeCount()
	{
		Connection connection = Login.connection;

		try
		{
			int Max = 0;
			for (int i = 0; i < TypeIncident.TypeCount(); i++)
			{
				String query = "SELECT * FROM soustypeincident where IdTypeIncident=" + i + ";";
				Statement statement = (Statement) connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				int rowCount = 0;
				while (resultSet.next())
				{
					rowCount += 1;
				}
				if (Max < rowCount)
					Max = rowCount;
			}
			return Max;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}

	/**
	 * Méthode.<BR>
	 * Permet d'avoir le nom d'un sous type d'incident par le biais de son
	 * identifiant.
	 *
	 * @param id l'identifiant du sous type dont on cherche le nom.
	 *
	 * @return le nom du sous incident recherché.
	 **/
	public static String getSubType(int id)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Nom FROM soustypeincident where IdSousTypeIncident=" + id + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				return resultSet.getString(1);
			}
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
}
