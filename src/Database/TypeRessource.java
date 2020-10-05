package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

public class TypeRessource
{

	public static String[][] getRessourceTypeName()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Abbreviation,Nom FROM typeressource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			String[][] RessourceTypeName = new String[getAllRessourceTypeCount()][2];
			int row = 0;
			while (resultSet.next())
			{
				RessourceTypeName[row][0] = resultSet.getString(1);
				RessourceTypeName[row][1] = resultSet.getString(2);
				row += 1;
			}
			return RessourceTypeName;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static int getAllRessourceTypeCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM typeressource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int RessourceTypeCount = 0;
			while (resultSet.next())
			{
				RessourceTypeCount += 1;
			}
			return RessourceTypeCount;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}

	public static int getMaxId()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(idTypeRessource) FROM typeressource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int Count = 0;
			if (resultSet.next())
			{
				Count = resultSet.getInt(1);
			}
			return Count;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static void add(String abv, String nom) throws SQLException
	{
		Connection connection = Login.connection;

		int id = getMaxId() + 1;
		String query = "INSERT INTO typeressource Values (" + id + ",'" + abv + "','" + nom + "');";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	public static void delete(int id)
	{
		Connection connection = Login.connection;

		String query = "DELETE FROM typeressource WHERE idTypeRessource=" + id + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		int maxId = getMaxId();
		if (id < maxId)
			ChangeId(maxId, id);
	}

	public static ResultSet getTypes()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM typeressource order by IdTypeRessource;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static void ChangeABV(int id, String abv) throws SQLException
	{
		Connection connection = Login.connection;

		String query = "UPDATE `typeressource` SET `abbreviation` = '" + abv + "' WHERE (`IdTypeRessource` = " + id
				+ ");";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	public static void ChangeName(int id, String Name) throws SQLException
	{
		Connection connection = Login.connection;

		String query = "UPDATE `typeressource` SET `nom` = '" + Name + "' WHERE (`IdTypeRessource` = " + id + ");";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	public static String getName(int id)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Nom FROM typeressource where idTypeRessource=" + id + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
				return resultSet.getString(1);
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return "";
	}

	public static void ChangeId(int oldId, int newId)
	{
		Connection connection = Login.connection;

		String query = "UPDATE `typeressource` SET `IdTypeRessource` = '" + newId + "' WHERE (`IdTypeRessource` = "
				+ oldId + ");";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public static boolean isWorking(int IdTypeRessource)
	{
		Connection connection = Login.connection;

		String query = "select count(IdTypeRessource) from Ressource where estDisponible='false' and IdTypeRessource="
				+ IdTypeRessource + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next() && rs.getInt(1) == 0)
			{
				return false;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}