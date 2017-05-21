import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JTextField;

/**
 * @author María Mercedes Retolaza Reyna, 16339 
 * Clase que se encarga de dar la interfaz al usuario.
 */
public class ClaseFrame extends JFrame implements ActionListener
	{
		/*Atributos*/
		private Container contenedor;
		JLabel labelTitulo;/*declaramos el objeto Label*/
		public JTextArea areaDeTexto;
		JButton botonAbrir,btnNewButton,btnNewButton_1,btnNewButton_2,btnNewButton_3
		,btnNewButton_4,btnCargar;/*declaramos el objeto Boton*/
		JScrollPane scrollPaneArea;
		JFileChooser fileChooser; /*Declaramos el objeto fileChooser*/
		String texto="";
		private ArrayList<String[]> array2 = new ArrayList<String[]>();
		public  ArrayList<String[]> array3 = new ArrayList<String[]>();
		private JTextField txtNoHayArchivo;
		private Procesos p = new Procesos();
		
		/**
		 * Constructor que arma el GUI.
		 */
		public ClaseFrame(){
			contenedor=getContentPane();
			contenedor.setLayout(null);
			
			/*Creamos el objeto*/
			fileChooser=new JFileChooser();
			 
			/*Propiedades del Label, lo instanciamos, posicionamos y
			 * activamos los eventos*/
			labelTitulo= new JLabel();
			labelTitulo.setText("Proyecto");
			labelTitulo.setBounds(110, 20, 180, 23);
			
			areaDeTexto = new JTextArea();
			areaDeTexto.setEditable(false);
			//para que el texto se ajuste al area
			areaDeTexto.setLineWrap(true);
			//permite que no queden palabras incompletas al hacer el salto de linea
			areaDeTexto.setWrapStyleWord(true);
		   	scrollPaneArea = new JScrollPane();
			scrollPaneArea.setBounds(20, 50, 350, 270);
	        scrollPaneArea.setViewportView(areaDeTexto);
	       	
			/*Propiedades del boton, lo instanciamos, posicionamos y
			 * activamos los eventos*/
			botonAbrir= new JButton();
			botonAbrir.setText("Boton 1");
			botonAbrir.setBounds(30, 331, 91, 23);
			botonAbrir.addActionListener(this);
			
			/*Agregamos los componentes al Contenedor*/
			contenedor.add(labelTitulo);
			contenedor.add(scrollPaneArea);
			contenedor.add(botonAbrir);
			
			btnNewButton = new JButton("Boton 2");
			btnNewButton.setBounds(131, 331, 89, 23);
			getContentPane().add(btnNewButton);
			btnNewButton.addActionListener(this);
			
			btnNewButton_1 = new JButton("Boton 3");
			btnNewButton_1.setBounds(230, 331, 94, 23);
			getContentPane().add(btnNewButton_1);
			btnNewButton_1.addActionListener(this);
			
			btnNewButton_2 = new JButton("Boton 4");
			btnNewButton_2.setBounds(32, 378, 89, 23);
			getContentPane().add(btnNewButton_2);
			btnNewButton_2.addActionListener(this);
			
			btnNewButton_3 = new JButton("Boton 5");
			btnNewButton_3.setBounds(131, 378, 89, 23);
			getContentPane().add(btnNewButton_3);
			btnNewButton_3.addActionListener(this);
			
			btnNewButton_4 = new JButton("Boton 6");
			btnNewButton_4.setBounds(230, 378, 94, 23);
			getContentPane().add(btnNewButton_4);
			btnNewButton_4.addActionListener(this);
			
			JTextPane txtpnInstruccionesnBoton = new JTextPane();
			txtpnInstruccionesnBoton.setEditable(false);
			txtpnInstruccionesnBoton.setFont(new Font("Dialog", Font.PLAIN, 14));
			txtpnInstruccionesnBoton.setBackground(SystemColor.control);
			txtpnInstruccionesnBoton.setText("Instrucciones: \r\nCargar: Cargar datos.csv.\r\nBoton 1: Visualizar grafo.\r\nBoton 2: Visualizar relaciones que tienen mas de 6 correos.\r\nBoton 3: Simplificar grafo.\r\nBoton 4: Page-Rank.\r\nBoton 5: Mostrar personas mas y menos comunicadas.\r\nBoton 6:  Mostrar la cantidad m\u00EDnima de correos que ha enviado una persona directa e indirectamente a otra persona o a todas las otras personas.");
			txtpnInstruccionesnBoton.setBounds(440, 50, 269, 304);
			getContentPane().add(txtpnInstruccionesnBoton);
			
			txtNoHayArchivo = new JTextField();
			txtNoHayArchivo.setEditable(false);
			txtNoHayArchivo.setForeground(Color.RED);
			txtNoHayArchivo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
			txtNoHayArchivo.setText("No hay archivo cargado.");
			txtNoHayArchivo.setBackground(Color.DARK_GRAY);
			txtNoHayArchivo.setBounds(505, 365, 204, 34);
			getContentPane().add(txtNoHayArchivo);
			txtNoHayArchivo.setColumns(10);
			
			btnCargar = new JButton("Cargar");
			btnCargar.setBounds(406, 372, 89, 23);
			getContentPane().add(btnCargar);
			btnCargar.addActionListener(this);
       		//Asigna un titulo a la barra de titulo
			setTitle("Proyecto");
			//tamaño de la ventana
			setSize(750,471);
			//pone la ventana en el Centro de la pantalla
			setLocationRelativeTo(null);
			
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent evento) {
			//Si presiono el boton de cargar
			if (evento.getSource()==btnCargar){
				abrirArchivo();
				int conta=0;
				for (String [] i: array2){
					conta++;
					if (conta>1){
						array3.add(i);
					}
				}
				
				
			}
			//Si presiono el boton 1
			if (evento.getSource()==botonAbrir){
				if (txtNoHayArchivo.getText().equals("Archivo Cargado")){
					p.crearUsuariosGrafo(array3);
					p.armarGrafo(1,false);
				}
				else{
					JOptionPane.showMessageDialog(null, "NO HA CARGADO EL ARCHIVO",
							  "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			//Si presiono el boton 2
			if (evento.getSource()==btnNewButton){
				if (txtNoHayArchivo.getText().equals("Archivo Cargado")){
					p.crearUsuariosGrafo(array3);
					p.armarGrafo(2,false);
				}
				else{
					JOptionPane.showMessageDialog(null, "NO HA CARGADO EL ARCHIVO",
							  "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}	
			//Si presiono el boton 3
			if (evento.getSource()==btnNewButton_1){
				if (txtNoHayArchivo.getText().equals("Archivo Cargado")){
					p.crearUsuariosGrafo(array3);
					p.armarGrafo(3,false);
				}
				else{
					JOptionPane.showMessageDialog(null, "NO HA CARGADO EL ARCHIVO",
							  "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}	
			//Si presiono el boton 4
			if (evento.getSource()==btnNewButton_2){
				if (txtNoHayArchivo.getText().equals("Archivo Cargado")){
					p.crearUsuariosGrafo(array3);
					p.armarGrafo(1,true);
				}
				else{
					JOptionPane.showMessageDialog(null, "NO HA CARGADO EL ARCHIVO",
							  "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
			//Si presiono el boton 5
			if (evento.getSource()==btnNewButton_3){
				if (txtNoHayArchivo.getText().equals("Archivo Cargado")){
					p.crearUsuariosGrafo(array3);
					areaDeTexto.setText(p.comunicacion());
				}
				else{
					JOptionPane.showMessageDialog(null, "NO HA CARGADO EL ARCHIVO",
							  "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
			//Si presiono el boton 6
			if (evento.getSource()==btnNewButton_4){
				if (txtNoHayArchivo.getText().equals("Archivo Cargado")){
					p.crearUsuariosGrafo(array3);
					p.armarGrafo(4,false);
					String origen = (String) JOptionPane.showInputDialog(null,"Seleccione un empleado de origen ",
							   "ORIGEN", JOptionPane.QUESTION_MESSAGE, null,
							  p.nombres,"Seleccione");
					if(origen!=null){
						String destino = (String) JOptionPane.showInputDialog(null,"Seleccione un empleado de origen ",
								   "ORIGEN", JOptionPane.QUESTION_MESSAGE, null,
								  p.nombres2,"Seleccione");
						if(!destino.equals("")){
							areaDeTexto.setText(origen+" ha enviado "+p.minima(origen,destino)+" mensajes a:" +destino );
							
						}
						else{
							int conta=0;
							String t="";
							p.minima(origen);
							for ( double i: p.pesos){
								t+=origen+" ha enviado "+i+" mensajes a:" +p.nombres[conta]+"\n";
								conta++;
							}
							areaDeTexto.setText(t);
						}
					}
					
				}
				else{
					JOptionPane.showMessageDialog(null, "NO HA CARGADO EL ARCHIVO",
							  "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
		

		/**
		 * Permite mostrar la ventana que carga 
		 * archivos en el area de texto
		 * @return arreglo con los campos del texto.
		 */
		private void  abrirArchivo() {
						
	 		//texto="";
	 		String bfRead;
	 		/*llamamos el metodo que permite cargar la ventana*/
			fileChooser.showOpenDialog(this);
			/*abrimos el archivo seleccionado*/
			File abre=fileChooser.getSelectedFile();

			/*recorremos el archivo, lo leemos para plasmarlo
			 *en el area de texto*/
			if(abre!=null)
			{ 				
				txtNoHayArchivo.setText("Archivo Cargado");
				try {
					BufferedReader ar = new BufferedReader(new FileReader(abre));
					while ((bfRead = ar.readLine()) != null) {
						 String array []=  bfRead.split(";");
						 array2.add(array);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "No se encontro archivo");
				}
			} 
				
		}
	}