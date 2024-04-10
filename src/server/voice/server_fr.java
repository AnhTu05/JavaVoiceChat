package server.voice;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.awt.event.ActionEvent;

public class server_fr extends JFrame {

	public int port = 8888;

	private JPanel contentPane;
	JButton btn_start = new JButton("Start");
	
	public static AudioFormat getaudioformat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, signed);
	}
	public SourceDataLine audio_out;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					server_fr frame = new server_fr();
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
	public server_fr() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Server");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNewLabel.setBounds(10, 11, 414, 51);
		contentPane.add(lblNewLabel);
		
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				init_audio();
			}
		});
		btn_start.setBounds(171, 160, 89, 30);
		contentPane.add(btn_start);
	}

	public void init_audio() {
		try {
			AudioFormat format = getaudioformat();
			DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
			if (!AudioSystem.isLineSupported(info_out)) {
				System.out.println("Not support");
				System.exit(0);
			}
			audio_out = (SourceDataLine) AudioSystem.getLine(info_out);
			audio_out.open(format);
			audio_out.start();
			player_thread p = new player_thread();
			p.din = new DatagramSocket(port);
			p.audio_out = audio_out;
			Server_voice.calling = true;
			p.start();
			btn_start.setEnabled(false);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
