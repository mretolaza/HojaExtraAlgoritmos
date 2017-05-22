import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 *@author María Mercedes Retolaza, 16339 
 * Clase para hacer manejos con neo4j.
 */
public class Conexion {
	/*Atributos*/
	private Connection con;
	private Statement stmt;
	private final String USER="neo4j";
	private final String PASSWORD= "4jneo";
	static final String  _URL = "jdbc:neo4j:bolt://localhost";
	/**
	 * Metodo constructor
	 */
	public Conexion(){
		try{
			con = DriverManager.getConnection(_URL, USER,PASSWORD);	
			stmt = con.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
		}


	}
	/**
	 * @param _query el query para consultar.
	 * @return la coleccion
	 * Metodo para consultar en la db.
	 */
	public ResultSet getQuery(String _query){
		Statement state = null;
		ResultSet resultado = null;
		try{
			state = (Statement) con.createStatement();
			resultado = state.executeQuery(_query);
		}
		catch(SQLException e)
		{
			e.printStackTrace();

		}
		return resultado;
	}

	/**
	 * @param nodo Nombre del nodo.
	 * @param id   Nombre del usuario.
	 * Metodo para insertar un nodo en la db.
	 */
	public void insertar(String nodo,String id){
		try {
			stmt.executeUpdate("CREATE ("+nodo+": User{name:'"+ id+"'})");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param name1	Nombre del nodo origen
	 * @param name2 Nombre del nodo destino
	 * @param peso	Cantidad de correos representada con length.
	 * Metodo para hacer las relaciones entre nodos.
	 */
	public void relacionar(String name1, String name2,String peso){
		try {
			stmt.executeUpdate("MATCH (n:User {name:'" + name1 + "'})" +
					"MATCH (m:User {name:'" + name2 + "'})" +
					"MERGE (n)-[:CORREOS {length: " + peso + "}]->(m)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Metodo para limpiar la base de datos.
	 */
	public void eliminar(){
		try {
			stmt.executeUpdate("MATCH (n) " +
					"OPTIONAL MATCH (n)-[r]-()"+" delete n,r" );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
