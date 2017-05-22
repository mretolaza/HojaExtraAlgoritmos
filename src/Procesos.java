import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.PageRank;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *  @author María Mercedes Retolaza Reyna, 16339 
 *  @author Javier Ramos, 16230 
 *  @author Diego Fernandez, 16344 
 *  Clase para hacer armar grafos, y desplegar relaciones de empleados/estudiantes.
 */
public class Procesos {
	/*Atributos*/
	public double pesos[];
	public Node[] nodos;
	PageRank pageRank = new PageRank();
	public Graph graph;
	private Conexion con = new Conexion();
	public String [] nombres={"Per 1","Per 2","Per 3","Per 4","Per 5","Per 6","Per 7","Per 8","Per 9","Per 10","Per 11"
			,"Per 12","Per 13","Per 14"};
	public String [] nombres2={"","Per 1","Per 2","Per 3","Per 4","Per 5","Per 6","Per 7","Per 8","Per 9","Per 10","Per 11"
			,"Per 12","Per 13","Per 14"};



	/**
	 * @param relaciones Contiene los pesos de las aristas.
	 * Metodo para llenar la db.
	 */
	void crearUsuariosGrafo(ArrayList<String[]> relaciones){
		con.eliminar();
		graph=null;
		graph= new SingleGraph("Proyecto");
		//graph2= new SingleGraph("Proyecto2");
		nodos = new Node [14];	
		for (int i=0;i<=13;i++){
			con.insertar(nombres[i].replace(" ",""), nombres[i]);
			nodos[i]=graph.addNode(nombres[i]);
			nodos[i].addAttribute("ui.label", nombres[i]);
			//graph2.addNode(nombres[i].replace(" ","")).addAttribute("ui.label", nombres[i]);
		}
		int conta=0;	
		for (int i=0; i<14;i++){
			conta=0;
			for (String n: relaciones.get(i)){
				if (conta>=1){
					if(!(n.equals("0"))){
						con.relacionar(nombres[i],  nombres[conta-1], n);			
					}
				}
				conta++;
			}
		}
	}
	/**
	 * Metodo para hacer el pageRank al grafo correspondiente.
	 */
	void pageRank(){
		pageRank.setVerbose(true);
		pageRank.init(graph);
		for (int i=0;i<=13;i++){
			double rank = pageRank.getRank(nodos[i]);
			nodos[i].addAttribute("ui.size", 5 + Math.sqrt(graph.getNodeCount() * rank * 20));
			nodos[i].addAttribute("ui.label", String.format("%.2f%%", rank * 100)+" "+nombres[i]);
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param origen Nodo origen del correo.
	 * Metodo para   tener la cantidad minima de correos que ha enviado una persona directa e 
	 * indirectamente a otra persona o a todas las otras personas. 
	 */
	void minima(String origen){
		pesos = new  double[14];
		int i=0;
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(origen));
		dijkstra.compute();
		// Print the lengths of all the shortest paths
		for (Node node : graph){
			pesos[i]=dijkstra.getPathLength(node);
			i++;	
		}

	}

	/**
	 * @param origen	Nodo origen del correo.
	 * @param destino	Nodo destino del correo.
	 * @return			Los caminos mas cortos.
	 */
	double minima(String origen,String destino ){
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(origen));
		dijkstra.compute();
		// Print the lengths of all the shortest paths
		return dijkstra.getPathLength(graph.getNode(destino));
	}

	/**
	 * @param opc	Entero para saber en base a que criterio hara el grafo.
	 * @param page	boolean para indicar si hara el pageRank.
	 * Metodo para armar el grafo de GraphStream. En base a la db.
	 */
	void armarGrafo(int opc,boolean page){
		//graph2= new SingleGraph("Proyec");
		ResultSet rs = null;
		graph.addAttribute("ui.stylesheet", "node {fill-color: red; size-mode: dyn-size;} edge {fill-color:grey;}");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		String query2 = "MATCH (n)-[rel:CORREOS]->(m)\n" +
				"WHERE n <> m\n" +
				"RETURN rel.length, n.name, m.name";
		String query = "MATCH (n)-[rel:CORREOS]->(m)\n" +
				"RETURN rel.length, n.name, m.name";
		String query3 = "MATCH (n)-[rel:CORREOS]->(m)\n" +
				"WHERE rel.length >= 6\n" +
				"RETURN rel.length, n.name, m.name";
		switch(opc){
		case 1:
			rs = con.getQuery(query);
			break;
		case 2:
			rs = con.getQuery(query2);
			break;
		case 3:
			rs = con.getQuery(query3);
			break;
		case 4:
			rs = con.getQuery(query);
			break;
		}
		if(opc!=4){
			graph.display();
		}
		try {
			while(rs.next()){
				Edge e= graph.addEdge(rs.getString("n.name").replace(" ","")+rs.getString("m.name").replace(" ",""),  
						rs.getString("n.name"), rs.getString("m.name"), true);
				e.addAttribute("length", rs.getString("rel.length"));
				e.addAttribute("label", "" + (int) e.getNumber("length"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (page){
			pageRank();
		}
	}

	/**
	 * @return Cadena con los estudiantes mas y menos comunicados
	 * Metodo que recorre la db para saber cuales son los empleados mas y 
	 * menos comunicados.
	 */
	String comunicacion(){
		String texto="Los 3 empleados menos comunicados son:\n";
		String query= "MATCH (x)-[rel:CORREOS]->()\n" +
				"RETURN x.name, sum(rel.length) as num\n" +
				"ORDER BY num asc";
		String query2= "MATCH (x)-[rel:CORREOS]->()\n" +
				"RETURN x.name, sum(rel.length) as num\n" +
				"ORDER BY num desc";
		ResultSet rs = null;
		int n=0;
		rs = con.getQuery(query);
		try {
			while(rs.next()){

				texto+=rs.getString("x.name")+" con "+rs.getString("num")+" mensajes enviados\n";
				n++;
				if(n>=3){
					break;
				}
			}
			texto+="Los 3 empleados mas comunicados son:\n";
			rs = con.getQuery(query2);
			while(rs.next()){
				texto+=rs.getString("x.name")+" con "+rs.getString("num")+" mensajes enviados\n";
				n++;
				if(n>=6){
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return texto;
	}
}
