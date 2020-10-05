package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

import userInterface.Login;

/************************************************************
 * La classe Incident contient les methodes qui permettent la gestion des
 * incidents dans l'application.
 * 
 * @author A.S.E.D.S
 * @version 7.02
 *****************************/
public class Incident
{
	/**
	 * M�thode.<BR>
	 * Permet d'inserer un incident compos� de son identifiant par le biais de le
	 * methode @see {@link Incident#IncidentCount()} , sa date, sa priorit�, son
	 * emplacement, la rue, la commune, l'identifiant de la ville, la latitude et la
	 * longitude, l'identifiant du type d'indident et du sous type d'incident et
	 * l'etat ( initialement en attente )
	 *
	 * @param date               la date de l'incident.
	 *
	 * @param priorit�           priorit� de l'incident.
	 *
	 * @param emplacement        l'emplacement de l'incident.
	 *
	 * @param rue                rue de l'incident.
	 * 
	 * @param commune            la commune de l'incident.
	 *
	 * @param IdVille            l'identifiant de la ville de l'incident.
	 *
	 * @param latitude           la latitude de la localisation de l'incident.
	 *
	 * @param longitude          la longitude de la localistation de l'incident.
	 *
	 * @param IdTypeIncident     l'identfiant du type d'incident.
	 *
	 * @param IdSousTypeIncident l'identifiant du sous type d'incident.
	 *
	 * @throws SQLException Si on n�arrive pas � ins�rer les donn�es.
	 **/
	public static void insert(String date, int priorit�, String emplacement, String rue, String commune, int IdVille,
			double latitude, double longitude, int IdTypeIncident, int IdSousTypeIncident) throws SQLException
	{
		Connection connection = Login.connection;

		int IdIncident = IncidentCount() + 1;
		String etat = "En Attente";
		String query = "INSERT INTO incident Values (" + IdIncident + ",'" + date + "','null'," + priorit� + ",\""
				+ emplacement + "\",\"" + rue + "\",\"" + commune + "\"," + IdVille + "," + latitude + "," + longitude
				+ "," + IdTypeIncident + "," + IdSousTypeIncident + ",'" + etat + "');";
		Statement statement;

		statement = connection.createStatement();
		statement.executeUpdate(query);

	}

	/**
	 * M�thode.<BR>
	 * Permet de supprimer un incident par le biais de son identifiant.
	 *
	 * @param IdIncident l'identifiant de l'incident qu'on veut supprimer.
	 *
	 **/
	public static void delete(int IdIncident)
	{
		Connection connection = Login.connection;

		String query = "DELETE FROM incident WHERE IdIncident=" + IdIncident + ";";
		Statement statement;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(query);

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir l'identifiant du dernier incident enregistr�.
	 * 
	 * @return l'identifiant du derni�r incident enregistr�.
	 **/
	public static int IncidentCount()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT max(idIncident) FROM incident;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int IncidentCount = 0;
			if (resultSet.next())
			{
				IncidentCount = resultSet.getInt(1);
			}
			return IncidentCount;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir un type d'incidents (etat sp�cifi� comme param�tre) avec leur
	 * identifiant, leur dates, priorit�s, emplacement, rue, commune, et ville
	 *
	 * @param etat l'etat des incidents recherch�s ( en attente, en cours de
	 *             traitement, r�solu )
	 *
	 * @return ResultSet qui contient ces incidents.
	 **/
	public static ResultSet GetIncidentByState(String etat)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT IdIncident,dateoccurrence,Priorit�,Emplacement,Rue,Commune,ville.nom as Ville FROM incident, ville where ville.idville=incident.idville and etat='"
					+ etat + "';";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir la location des incidents par leur etat
	 *
	 * @param etat l'etat des incidents recherch�s ( en attente, en cours de
	 *             traitement, r�solu )
	 *
	 * @return List des points geographiques des incidents.
	 **/
	public static List<GeoPosition> GetIncidentLocByState(String etat)
	{
		Connection connection = Login.connection;
		List<GeoPosition> IncidentsLoc = new ArrayList<GeoPosition>();
		try
		{
			String query = "SELECT latitude,longitude FROM incident where etat='" + etat + "';";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next())
			{
				GeoPosition pos = new GeoPosition(resultSet.getFloat(1), resultSet.getFloat(2));
				IncidentsLoc.add(pos);
			}
			return IncidentsLoc;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return IncidentsLoc;
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir les incidents trait� ( dont l'�tat est : "trait�" ) avec leur
	 * identifiant, leur dates (d'occurence et de resolution) , priorit�s,
	 * emplacement, rue, commune, et ville
	 *
	 * @return ResultSet qui contient ces incidents trait�s.
	 **/

	public static ResultSet GetFinishedIncident()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT IdIncident,dateoccurrence,dater�solution,Priorit�,Emplacement,Rue,Commune,ville.nom as Ville FROM incident, ville where ville.idville=incident.idville and etat='Trait�';";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir tous les incidents enregistr�s ( dont l'�tat est : "trait�" )
	 * avec leur identifiant, leur dates (d'occurence et de r�solution d'il s'agit
	 * d'un incident trait�) , priorit�s, emplacement, rue, commune, et ville
	 *
	 * @return ResultSet qui contient ces incidents.
	 **/
	public static ResultSet GetAllIncident()
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT IdIncident,DateOccurrence,DateR�solution,Priorit�,Emplacement,Rue,Commune,ville.nom as Ville,Etat FROM incident, ville where ville.idville=incident.idville;";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * M�thode.<BR>
	 * Permet de modifier l'etat d'un incident par le biais de l'identifiant.
	 * 
	 * @param IdIncident l'identifiant de l'incident qu'on veut modifier.
	 * @param etat       le nouveau etat de l'incident.
	 **/
	public static void SetIncidentState(int IdIncident, String etat)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "UPDATE incident SET etat = '" + etat + "' WHERE (IdIncident =" + IdIncident + ")";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir les coordonn�es d'un incident par le biais de son identifiant.
	 * 
	 * @param IdIncident l'identifiant de l'incident.
	 * @return GeoPosition qui contient la longitude et la latitude de l'incident
	 *         recherch�.
	 **/

	public static GeoPosition getIncidentPosition(int IdIncident)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT Latitude,Longitude FROM incident where IdIncident=" + IdIncident + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next())
			{
				double Lat = resultSet.getDouble(1);
				double Lon = resultSet.getDouble(2);
				return new GeoPosition(Lat, Lon);
			}
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * M�thode.<BR>
	 * Permet de changer modifier le type d'un incident pour montrer qu'il est
	 * trait�, par le biais de son identifiant, et inserant sa date de r�solution.
	 * 
	 * @param IdIncident l'identifiant de l'incident.
	 **/
	public static void setIncidentResolved(int IdIncident)
	{
		Connection connection = Login.connection;

		try
		{
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			String strDate = dateFormat.format(date);

			String query = "UPDATE incident SET dater�solution = '" + strDate + "' WHERE (IdIncident =" + IdIncident
					+ ")";
			Statement statement = (Statement) connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * M�thode.<BR>
	 * Permet d'avoir le sous type de l'incident par le biais de son identifiant.
	 * 
	 * @param id l'identifiant de l'incident qu'on veut modifier.
	 * @return Le sousType de l'incident.
	 **/
	public static String getIncidentSubType(int id)
	{
		Connection connection = Login.connection;

		try
		{
			String query = "SELECT IdSousTypeIncident FROM incident where IdIncident=" + id + ";";
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			int IdSousTypeIncident = 0;
			if (resultSet.next())
			{
				IdSousTypeIncident = resultSet.getInt(1);
			}
			return SousTypeIncident.getSubType(IdSousTypeIncident);
		} catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
}
