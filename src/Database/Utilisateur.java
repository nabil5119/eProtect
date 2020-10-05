package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import userInterface.Login;

public class Utilisateur
{

	public static boolean connect(String username, String password)
	{
		Connection connection = Login.connection;
		String query = "SELECT username,password from utilisateur";
		try
		{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				if (rs.getString(1).equals(username) && rs.getString(2).equals(password))
				{
					return true;
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static int getGID(String username)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT type from utilisateur where username='" + username + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
			{
				if (rs.getString(1).equals("Administrateur"))
					return 0;
				return 1;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	public static int getID(String username)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT iduser from utilisateur where username='" + username + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	public static String getUser(int Iduser)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT username from utilisateur where Iduser='" + Iduser + "';";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
			{
				return rs.getString(1);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void Add(int IdUser, String nom, String username, String password, int GroupId) throws SQLException
	{
		Connection connection = Login.connection;

		String Type = "Utilisateur";
		if (GroupId == 0)
		{
			Type = "Administrateur";
		}
		String query = "INSERT INTO utilisateur Values (" + IdUser + ",'" + nom + "','" + username + "','" + password
				+ "','" + Type + "');";
		Statement statement;
		statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	public static void Delete(int IdUser)
	{
		Connection connection = Login.connection;

		String query = "Delete from utilisateur where IdUser=" + IdUser + ";";
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static ResultSet getUsers()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "Select Iduser as ID,Nom as 'Nom Complet',username AS 'Nom de utilisateur',type as 'Type de Compte' from utilisateur;";
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static int GetuserCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(iduser) FROM utilisateur;";
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

	public static void ChangeName(int IdUser, String Name)
	{
		Connection connection = Login.connection;

		String query = "UPDATE utilisateur SET `nom` = '" + Name + "' WHERE (`Iduser` =" + IdUser + " );";
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void ChangeUser(int IdUser, String User) throws SQLException
	{
		Connection connection = Login.connection;

		String query = "UPDATE utilisateur SET `username` = '" + User + "' WHERE (`Iduser` =" + IdUser + " );";
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
	}

	public static void ChangePass(int IdUser, String Pass)
	{
		Connection connection = Login.connection;

		String query = "UPDATE utilisateur SET `password` = '" + Pass + "' WHERE (`Iduser` =" + IdUser + " );";
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void ChangeType(int IdUser, String Type)
	{
		Connection connection = Login.connection;

		String query = "UPDATE utilisateur SET `Type` = '" + Type + "' WHERE (`Iduser` =" + IdUser + " );";
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
