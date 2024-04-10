package client.voice;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class client_fr extends JFrame {

	private JPanel contentPane;
	JButton btn_start = new JButton("Start");
	JButton btn_stop = new JButton("Stop");

	public int port_server = 8888;
	public String add_server = "127.0.0.1";
	public static AudioFormat getaudioformat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, signed);
	}
	
	TargetDataLine audio_in;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client_fr frame = new client_fr();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public client_fr() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Client Voice");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 414, 40);
		contentPane.add(lblNewLabel);
		
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				init_audio();
			}
		});
		btn_start.setBounds(80, 146, 89, 32);
		contentPane.add(btn_start);
		
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client_voice.calling = false;
				btn_start.setEnabled(true);
				//btn_stop.setEnabled(false);
			}
		});
		btn_stop.setBounds(248, 146, 89, 32);
		contentPane.add(btn_stop);
	}
	
	public void init_audio() {
		JButton btn_stop = new JButton("Stop");
		btn_stop.setBounds(248, 146, 89, 32);
		contentPane.add(btn_stop);
		try {
			AudioFormat format = getaudioformat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Not support");
			}
			audio_in = (TargetDataLine) AudioSystem.getLine(info);
			audio_in.open(format);
			audio_in.start();
			recorder_thread r = new recorder_thread();
			InetAddress inet = InetAddress.getByName(add_server);
			r.audio_in = audio_in;
			r.dout = new DatagramSocket();
			r.server_ip = inet;
			r.server_port = port_server;
			Client_voice.calling = true;
			r.start();
			btn_start.setEnabled(false);
			btn_stop.setEnabled(true);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
}
