package me.rpairo.nasa;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import me.rpairo.nasa.syscontrol.Data;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Color;
import java.awt.Font;


public class Server {

	private JFrame frame;
	ImageIcon ventilador;
	
	JLabel switch_solar_off;
	JLabel switch_solar_on;
	JLabel switch_fan_off;
	JLabel switch_fan_on;
	private JLabel lblLifeSupport;
	private JLabel switch_life_off;
	private JLabel switch_life_on;
	
	JButton btnStopServer;
	JButton btnEncenderServer;
	
	JLabel lblStatusFan;
	JLabel lblStatusSolarPanel;
	
	final String font = "Helvetica";
	private JLabel label;
	private JLabel lblConnected;
	private JLabel lblOpenHatch;
	private JLabel switch_hatch_off;
	private JLabel switch_hatch_on;
	private JLabel cloud_off;
	private JLabel cloud_on;
	JLabel switch_pilot_on;
	JLabel switch_pilot_off;
	
	SocketNASA sock;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Server() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 284);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnEncenderServer = new JButton("Run Server");
		btnEncenderServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				run_server();
			}
		});
		btnEncenderServer.setBackground(Color.WHITE);
		btnEncenderServer.setBounds(6, 6, 117, 29);
		btnEncenderServer.setFont(new Font(font, Font.PLAIN, 13));
		btnEncenderServer.setBorder(BorderFactory.createEtchedBorder());
		frame.getContentPane().add(btnEncenderServer);
		
		btnStopServer = new JButton("Stop server");
		btnStopServer.setFont(new Font(font, Font.PLAIN, 13));
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					stop_server();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnStopServer.setBackground(Color.WHITE);
		btnStopServer.setBounds(6, 47, 117, 29);
		btnStopServer.setBorder(BorderFactory.createEtchedBorder());
		frame.getContentPane().add(btnStopServer);
		
		lblStatusFan = new JLabel("Fan system");
		lblStatusFan.setFont(new Font(font, Font.PLAIN, 13));
		lblStatusFan.setBounds(256, 6, 96, 29);
		frame.getContentPane().add(lblStatusFan);
		
		lblStatusSolarPanel = new JLabel("Solar panels");
		lblStatusSolarPanel.setBounds(256, 47, 96, 29);
		lblStatusSolarPanel.setFont(new Font(font, Font.PLAIN, 13));
		frame.getContentPane().add(lblStatusSolarPanel);
		
		switch_fan_on = new JLabel("");
		switch_fan_on.setIcon(new ImageIcon(Server.class.getResource("/img/switch_on.png")));
		switch_fan_on.setBounds(364, 6, 69, 29);
		switch_fan_on.setVisible(false);
		frame.getContentPane().add(switch_fan_on);
		
		switch_solar_on = new JLabel("");
		switch_solar_on.setIcon(new ImageIcon(Server.class.getResource("/img/switch_on.png")));
		switch_solar_on.setBounds(364, 47, 69, 29);
		switch_solar_on.setVisible(false);
		frame.getContentPane().add(switch_solar_on);
		
		switch_solar_off = new JLabel("");
		switch_solar_off.setIcon(new ImageIcon(Server.class.getResource("/img/switch_off.png")));
		switch_solar_off.setBounds(364, 47, 69, 29);
		frame.getContentPane().add(switch_solar_off);
		
		switch_fan_off = new JLabel("");
		switch_fan_off.setIcon(new ImageIcon(Server.class.getResource("/img/switch_off.png")));
		switch_fan_off.setBounds(364, 6, 69, 29);
		frame.getContentPane().add(switch_fan_off);
		
		lblLifeSupport = new JLabel("Life support");
		lblLifeSupport.setBounds(256, 88, 96, 29);
		lblLifeSupport.setFont(new Font(font, Font.PLAIN, 13));
		frame.getContentPane().add(lblLifeSupport);
		
		switch_life_on = new JLabel("");
		switch_life_on.setIcon(new ImageIcon(Server.class.getResource("/img/switch_on.png")));
		switch_life_on.setBounds(364, 88, 69, 29);
		switch_life_on.setVisible(false);
		frame.getContentPane().add(switch_life_on);
		
		switch_life_off = new JLabel("");
		switch_life_off.setIcon(new ImageIcon(Server.class.getResource("/img/switch_off.png")));
		switch_life_off.setBounds(364, 88, 69, 29);
		frame.getContentPane().add(switch_life_off);
		
		label = new JLabel("");
		label.setIcon(new ImageIcon(Server.class.getResource("/img/nasa_logo.png")));
		label.setBounds(6, 187, 69, 69);
		frame.getContentPane().add(label);
		
		lblConnected = new JLabel("Server status");
		lblConnected.setBounds(6, 128, 89, 29);
		frame.getContentPane().add(lblConnected);
		
		lblOpenHatch = new JLabel("Open hatch");
		lblOpenHatch.setFont(new Font(font, Font.PLAIN, 13));
		lblOpenHatch.setBounds(256, 129, 96, 29);
		frame.getContentPane().add(lblOpenHatch);
		
		switch_hatch_on = new JLabel("");
		switch_hatch_on.setIcon(new ImageIcon(Server.class.getResource("/img/switch_on.png")));
		switch_hatch_on.setFont(new Font("Helvetica", Font.PLAIN, 13));
		switch_hatch_on.setBounds(364, 129, 69, 29);
		switch_hatch_on.setVisible(false);
		frame.getContentPane().add(switch_hatch_on);
		
		switch_hatch_off = new JLabel("");
		switch_hatch_off.setIcon(new ImageIcon(Server.class.getResource("/img/switch_off.png")));
		switch_hatch_off.setFont(new Font("Helvetica", Font.PLAIN, 13));
		switch_hatch_off.setBounds(364, 129, 69, 29);
		frame.getContentPane().add(switch_hatch_off);
		
		cloud_off = new JLabel("");
		cloud_off.setIcon(new ImageIcon(Server.class.getResource("/img/cloud_off.png")));
		cloud_off.setBounds(96, 130, 39, 20);
		frame.getContentPane().add(cloud_off);
		
		cloud_on = new JLabel("");
		cloud_on.setIcon(new ImageIcon(Server.class.getResource("/img/cloud_on.png")));
		cloud_on.setBounds(96, 130, 39, 20);
		cloud_on.setVisible(false);
		frame.getContentPane().add(cloud_on);
		
		JLabel lblAutopilot = new JLabel("Automatic pilot");
		lblAutopilot.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblAutopilot.setBounds(256, 170, 96, 29);
		frame.getContentPane().add(lblAutopilot);
		
		switch_pilot_on = new JLabel("");
		switch_pilot_on.setIcon(new ImageIcon(Server.class.getResource("/img/switch_on.png")));
		switch_pilot_on.setFont(new Font("Helvetica", Font.PLAIN, 13));
		switch_pilot_on.setBounds(364, 170, 69, 29);
		switch_pilot_on.setVisible(false);
		frame.getContentPane().add(switch_pilot_on);
		
		switch_pilot_off = new JLabel("");
		switch_pilot_off.setIcon(new ImageIcon(Server.class.getResource("/img/switch_off.png")));
		switch_pilot_off.setFont(new Font("Helvetica", Font.PLAIN, 13));
		switch_pilot_off.setBounds(364, 170, 69, 29);
		frame.getContentPane().add(switch_pilot_off);
		
		lblSecondarySys = new JLabel("Secondary sys.");
		lblSecondarySys.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblSecondarySys.setBounds(256, 212, 96, 29);
		frame.getContentPane().add(lblSecondarySys);
		
		switch_secondary_on = new JLabel("");
		switch_secondary_on.setIcon(new ImageIcon(Server.class.getResource("/img/switch_on.png")));
		switch_secondary_on.setFont(new Font("Helvetica", Font.PLAIN, 13));
		switch_secondary_on.setBounds(364, 211, 69, 29);
		switch_secondary_on.setVisible(false);
		frame.getContentPane().add(switch_secondary_on);
		
		switch_secondary_off = new JLabel("");
		switch_secondary_off.setIcon(new ImageIcon(Server.class.getResource("/img/switch_off.png")));
		switch_secondary_off.setFont(new Font("Helvetica", Font.PLAIN, 13));
		switch_secondary_off.setBounds(364, 211, 69, 29);
		frame.getContentPane().add(switch_secondary_off);
	}
	
	public void run_server() {
		cloud_off.setVisible(false);
		cloud_on.setVisible(true);
		this.sock = new SocketNASA();
		this.sock.start();
	}
	
	public void stop_server() throws IOException {
		cloud_off.setVisible(true);
		cloud_on.setVisible(false);
		
		switch_hatch_on.setVisible(false);
		switch_hatch_off.setVisible(true);
		
		switch_fan_on.setVisible(false);
		switch_fan_off.setVisible(true);
		
		switch_solar_on.setVisible(false);
		switch_solar_off.setVisible(true);
		
		switch_life_on.setVisible(false);
		switch_life_off.setVisible(true);
		
		switch_pilot_on.setVisible(false);
		switch_pilot_off.setVisible(true);
		
		switch_secondary_on.setVisible(false);
		switch_secondary_off.setVisible(true);
		
		if(!(skCliente == null))
			skCliente.close();
		
		if(!(ois == null))
			ois.close();
		
		if(!(oos == null))
			oos.close();
		
		if(!(skServidor == null))
			skServidor.close();
		
		System.out.println("El servidor ha sido cerrado\n");
	}
	
	Socket skCliente;
	ObjectOutputStream oos = null;
	ServerSocket skServidor;
	ObjectInputStream ois;
	private JLabel lblSecondarySys;
	private JLabel switch_secondary_off;
	private JLabel switch_secondary_on;
	
	private class SocketNASA extends Thread{
		
		
		final int PUERTO = 5555;
		String IP_client;
		
		Data mdata = null;
		String TimeStamp;
		
		public void run() {
			try {
				run_socket();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
		private void run_socket() throws IOException, ClassNotFoundException {
			skServidor = new ServerSocket(PUERTO);
			
			System.out.println("************ SERVER ****************");
			
			System.out.println("Escuchando el puerto " + PUERTO);
			System.out.println("En Espera....");
			try {
				handshake();
			}catch(IOException i) {
				//i.printStackTrace();
			} catch (ClassNotFoundException c) {
				//c.printStackTrace();
			}
		}
		
		private void handshake() throws IOException, ClassNotFoundException{

			while(true) {
			skCliente = skServidor.accept(); // esperamos al cliente
			// una vez q se conecto obtenemos la ip
			IP_client = skCliente.getInetAddress().toString();
			System.out.println("[" + TimeStamp + "] Conectado al cliente "
					+ "IP:" + IP_client);
			
			while (true) {
				// Manejamos flujo de Entrada
				ois = new ObjectInputStream(
						skCliente.getInputStream());
				// Cremos un Objeto con lo recibido del cliente
				Object aux = ois.readObject();// leemos objeto

				// si el objeto es una instancia de Mensaje_data
				if (aux instanceof Data) {
					// casteamos el objeto
					mdata = (Data) aux;

					// Analizamos el mensaje recibido
					// si no es el mensaje FINAL
					if (!mdata.last_msg) {

						// Es un mensaje de Accion
						if (mdata.Action != -1) {
							// exec accion
							//Exec(mdata.Action);
							this.funcion(mdata.Action);
							System.out.println("[" + TimeStamp + "] "+ "Ejecutar Accion " + mdata.Action + " [" + IP_client + "]");
						} else {
							// No es un mensaje de accion entonces es de
							// texto
							
							/*PointerInfo a = MouseInfo.getPointerInfo();
							Point b = a.getLocation();
							int x = (int) b.getX();
							int y = (int) b.getY();
							System.out.print(y + "jjjjjjjjj");
							System.out.print(x);
							Robot r = new Robot();
							r.mouseMove(x, y - 50);*/
							
							
							
							System.out.println("[" + TimeStamp + "] "+ "Mensaje de [" + IP_client + "]--> "+ mdata.texto);
						}
					} else {// cerramos socket
						skCliente.close();
						ois.close();
						System.out.println("["+ TimeStamp+ "] Cerrando conexion");
						
						
						break;
					}
				} else {
					// Si no es del tipo esperado, se marca error
					System.err.println("Mensaje no esperado ");
				}
			}
			}
		}
		
		private void funcion(int funcion) throws IOException {
			
			System.out.println(mdata.encender);
			
			switch (funcion) {
			case 6:
				if(mdata.encender) {
					switch_fan_on.setVisible(true);
					switch_fan_off.setVisible(false);
					retournCall();
				} else {
					switch_fan_on.setVisible(false);
					switch_fan_off.setVisible(true);
					retournCall();
				}
				break;
			case 7:
				if(mdata.encender) {
					switch_solar_on.setVisible(true);
					switch_solar_off.setVisible(false);
					retournCall();
				} else {
					switch_solar_on.setVisible(false);
					switch_solar_off.setVisible(true);
					retournCall();
				}
				break;
			case 8:
				if(mdata.encender) {
					switch_life_on.setVisible(true);
					switch_life_off.setVisible(false);
					retournCall();
				} else {
					switch_life_on.setVisible(false);
					switch_life_off.setVisible(true);
					retournCall();
				}
				break;
			case 9:
				if(mdata.encender) {
					switch_hatch_on.setVisible(true);
					switch_hatch_off.setVisible(false);
					retournCall();
				} else {
					switch_hatch_on.setVisible(false);
					switch_hatch_off.setVisible(true);
					retournCall();
				}
				break;
				
			case 10:
				if(mdata.encender) {
					switch_pilot_on.setVisible(true);
					switch_pilot_off.setVisible(false);
					retournCall();
				} else {
					switch_pilot_on.setVisible(false);
					switch_pilot_off.setVisible(true);
					retournCall();
				}
				break;
			case 11:
				if(mdata.encender) {
					switch_secondary_on.setVisible(true);
					switch_secondary_off.setVisible(false);
					retournCall();
				} else {
					switch_secondary_on.setVisible(false);
					switch_secondary_off.setVisible(true);
					retournCall();
				}
				break;

			default:
				break;
			}
		}
		
		private void retournCall() throws IOException {
			oos = new ObjectOutputStream(skCliente.getOutputStream());
			Data dat = new Data();
			dat.callback = true;
			dat.Action = mdata.Action;
			dat.encender = mdata.encender;
			
			oos.writeObject(dat);
		}
	}
}
