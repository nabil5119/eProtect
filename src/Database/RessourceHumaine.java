package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

public class RessourceHumaine
{

	public static String getName(int IdRessourceHumaine)
	{
		Connection connection = Login.connection;

		try
		{
			String Name = "";
			String query = "SELECT Nom FROM ressourcehumaine where idressourcehumaine=" + IdRessourceHumaine + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				Name = resultSet.getString(1);
			}
			return Name;
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static int UseRessourceHumaine(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT idressourcehumaine FROM ressourcehumaine where estDisponible='true' and IdCaserne="
					+ IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int IdRessourcHumainee = 0;
			if (resultSet.next())
			{
				IdRessourcHumainee = Integer.valueOf(resultSet.getString(1));
				setRessourceHumaineStatue(IdRessourcHumainee, false);
			}
			return IdRessourcHumainee;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		;
		return 0;
	}

	public static int getAllWorkingResources(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			int sum = 0;
			String query = "SELECT count(*) FROM ressourcehumaine where estDisponible='false' and IdCaserne=" + IdCaserne
					+ ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				sum = resultSet.getInt(1);
			}
			return sum;
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static int getAllResources(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			int sum = 0;
			String query = "SELECT count(*) FROM ressourcehumaine where IdCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				sum = resultSet.getInt(1);
			}
			return sum;
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static void setRessourceHumaineStatue(int IdRessourcHumainee, boolean status)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "UPDATE ressourcehumaine SET estDisponible = '" + status + "' WHERE (idressourcehumaine ="
					+ IdRessourcHumainee + ");";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public static void addResource(int IdCaserne,String Nom) throws Exception
	{
		Connection connection = Login.connection;

		int IdRessource = GetMaxId() + 1;

		/*Random rand = new Random();
		int f = rand.nextInt(10);
		int l = rand.nextInt(10);
		String[] firstname =
		{ "Ahmed", "Amine", "Mohamed", "Asmaa", "Farah", "Ikram", "Nour", "Salah", "Rabia", "Anas" };
		String[] lastname =
		{ "Soufi", "Wahid", "Hari", "Chana", "Madani", "Rais", "Ftih", "Zahra", "Bouhafa", "Chahid" };
		String Nom = firstname[f] + " " + lastname[l];*/
		String query = "INSERT INTO ressourcehumaine Values (" + IdRessource + ",'" + Nom + "','true'," + IdCaserne
				+ ");";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);

	}

	public static int GetHumaineResourceCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM ressourcehumaine;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int Count = 0;
			while (resultSet.next())
			{
				Count += 1;
			}
			return Count;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static int GetMaxId()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(idressourcehumaine) FROM ressourcehumaine;";
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

	public static ResultSet getHumaineResource(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT * FROM ressourcehumaine and IdCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			return statement.executeQuery(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void Delete(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "Delete FROM ressourcehumaine where IdRessourceHumaine= (select max(IdRessourceHumaine) FROM ressourcehumaine where estDisponible='true' and IdCaserne="
					+ IdCaserne + ");";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void DeleteAll(int IdCaserne)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "Delete FROM ressourcehumaine where estDisponible='true' and IdCaserne=" + IdCaserne + ";";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
