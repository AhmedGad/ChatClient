import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static String username = null;
	static String othername = null;

	class AreYouSure extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showOptionDialog(MainFrame.this,
					"Are you sure you want to quit?", "Exit Dialog",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				if (username != null) {
					try {
						while (!ServerMethods.unregister(MainFrame.username))
							;

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		}
	}

	static HostsFrame hostframe;
	static MainFrame mainframe;
	static ChatFrame cf;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		mainframe = this;

		try {
			HostsFrame.hosts = ServerMethods.getHostsList();
			hostframe = new HostsFrame();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setVisible(true);
		cf = new ChatFrame();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new AreYouSure());

		setBounds(100, 100, 450, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);

		final JLabel lblEnterYourName = new JLabel("Enter Your Name");
		lblEnterYourName.setBounds(12, 12, 118, 23);
		layeredPane.add(lblEnterYourName);

		textField = new JTextField();
		textField.setBounds(159, 12, 152, 23);
		layeredPane.add(textField);
		textField.setColumns(10);

		cf = new ChatFrame();

		final JButton btnHost = new JButton("Host");
		btnHost.setVisible(false);
		btnHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("start");
						try {
							ServerMethods.host(username);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
				cf.setVisible(true);
				mainframe.setVisible(false);
			}
		});

		btnHost.setBounds(22, 47, 117, 25);
		layeredPane.add(btnHost);

		final JButton btnGuest = new JButton("Guest");
		btnGuest.setVisible(false);
		btnGuest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainframe.setVisible(false);
				hostframe.setVisible(true);
			}
		});
		btnGuest.setBounds(251, 47, 117, 25);
		layeredPane.add(btnGuest);

		final JButton login = new JButton("log in");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField.getText().length() > 0) {
					try {
						if (ServerMethods.register(textField.getText())) {
							login.setVisible(false);
							textField.setVisible(false);
							lblEnterYourName.setVisible(false);
							username = textField.getText();
							btnHost.setVisible(true);
							btnGuest.setVisible(true);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		login.setBounds(136, 77, 117, 25);
		layeredPane.add(login);

	}
}
