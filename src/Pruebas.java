import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

/**
 * @author María Mercees Retolaza Reyna, 16339 
 * @author Javier Ramos, 16230 
 * @author Gadhi Rodriguez, 16206 
 * @author Diego Fernandez, 16344
 * Clase para las JUnits.
 * Estas son las pruebas que se realizaron para identidicar la correcta utilizacion de dicho programa 
 *
 */
public class Pruebas {
	/*Atributos 
	En esta Sección se declaran los atributos que se utilizarán para la realización de dicho proceso*/
	private Procesos p = new Procesos();
	private Conexion c = new Conexion();
	private ResultSet rs=null;
	private ArrayList<String[]> array2 = new ArrayList<String[]>();
	private  ArrayList<String[]> array3 = new ArrayList<String[]>();
	public String [] nombres={"Per 1","Per 2","Per 3","Per 4","Per 5","Per 6","Per 7","Per 8","Per 9","Per 10","Per 11"
			,"Per 12","Per 13","Per 14"};
	private BufferedReader ar;
	private String dir="C:\\Users\\Carlos\\Desktop\\datos.csv";
	/**
	 * @throws SQLException
	 * @throws IOException 
	 * Este metodo teste los metodos de getQuery,eliminar, y de armar el grafo 
	 * con informacio de la db.
	 */
	@Test
	public void testgetQueryarmado() throws SQLException, IOException {
		String bfRead;
		ar = new BufferedReader(new FileReader(dir));
		while ((bfRead = ar.readLine()) != null) {
			String array []=  bfRead.split(";");
			array2.add(array);
		}
		int conta=0;
		for (String [] i: array2){
			conta++;
			if (conta>1){
				array3.add(i);
			}
		}
		p.crearUsuariosGrafo(array3);
		String query= "MATCH (x)-[rel:CORREOS]->()\n" +
				"RETURN x.name, sum(rel.length) as num\n" +
				"ORDER BY num asc";
		rs= c.getQuery(query);
		rs.next();
		assertEquals(rs.getString("num"),"7");
	}
	/**
	 * @throws IOException
	 * Este metodo testea el metodo para comprobar si esta devolviendo los usuarios
	 * mas y menos comunicados correctos
	 */
	@Test
	public void testacomunicacion() throws IOException{
		String bfRead;
		ar = new BufferedReader(new FileReader("C:\\Users\\Carlos\\Desktop\\datos.csv"));
		while ((bfRead = ar.readLine()) != null) {
			String array []=  bfRead.split(";");
			array2.add(array);
		}
		int conta=0;
		for (String [] i: array2){
			conta++;
			if (conta>1){
				array3.add(i);
			}
		}
		p.crearUsuariosGrafo(array3);
		p.armarGrafo(1, false);
		String s="Los 3 empleados menos comunicados son:\n"
				+ "Per 3 con 7 mensajes enviados\n"
				+ "Per 13 con 11 mensajes enviados\n"
				+ "Per 1 con 12 mensajes enviados\n"
				+ "Los 3 empleados mas comunicados son:\n"
				+ "Per 10 con 42 mensajes enviados\n"
				+ "Per 8 con 40 mensajes enviados\n"
				+ "Per 12 con 40 mensajes enviados\n";
		String com=p.comunicacion();
		assertEquals(com,s);

	}
	/**
	 * @throws SQLException
	 * Este metodo testea el metodo para insertar un dato a la db.
	 */
	@Test
	public void testInsertar() throws SQLException{
		c.insertar("Per15", "Per 15");
		String query= "MATCH (x) where x.name = 'Per 15' return x.name";
		rs= c.getQuery(query);
		rs.next();
		assertEquals(rs.getString("x.name"),"Per 15"); 
	}
	/**
	 * @throws SQLException
	 * Este metodo testea los metodos eliminar y crear una relacion
	 * en la db.
	 */
	@Test
	public void testrelacionareliminar() throws SQLException{
		c.eliminar();
		c.insertar("Per15", "Per 15");
		c.insertar("Per16", "Per 16"); 
		c.relacionar("Per 15", "Per 16", "4");
		String query = "MATCH (x:User{name:'Per 15'})-[rel:CORREOS]->(y:User{name:'Per 16'})\n" +
				"RETURN rel.length, x.name, y.name"; 
		rs= c.getQuery(query);
		rs.next();
		assertEquals(rs.getString("x.name")+"->"+rs.getString("rel.length")+"->"+rs.getString("y.name"),
				"Per 15->4->Per 16");
	}

}
