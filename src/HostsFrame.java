import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLayeredPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

public class HostsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	class AreYouSure extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showOptionDialog(HostsFrame.this,
					"Are you sure you want to quit?", "Exit Dialog",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				if (MainFrame.username != null) {
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

	static JLayeredPane layeredPane;
	static ArrayList<String> hosts;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HostsFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new AreYouSure());
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane); // /**

		layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);
		Vector items = new Vector();
		for (int i = 0; i < hosts.size(); i++)
			items.add(hosts.get(i));

		final JComboBox comboBox = new JComboBox(items);
		comboBox.setBounds(54, 36, 285, 42);
		layeredPane.add(comboBox);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (hosts.size() > 0) {
					try {
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									ServerMethods.connect(MainFrame.username,
											hosts.get(comboBox
													.getSelectedIndex()));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						t.start();
						MainFrame.othername = hosts.get(comboBox
								.getSelectedIndex());
						MainFrame.cf.setVisible(true);
						HostsFrame.this.setVisible(false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnConnect.setBounds(54, 143, 117, 25);
		layeredPane.add(btnConnect);

		JButton btnHost = new JButton("Host");
		btnHost.setBounds(143, 193, 117, 25);
		layeredPane.add(btnHost);

		JButton btnRefreshList = new JButton("Refresh list");
		btnRefreshList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector v = new Vector();
				try {
					hosts = ServerMethods.getHostsList();
					for (int i = 0; i < hosts.size(); i++)
						v.add(hosts.get(i));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				ComboBoxModel model = new DefaultComboBoxModel(v);
				comboBox.setModel(model);
			}
		});
		btnRefreshList.setBounds(222, 143, 117, 25);
		layeredPane.add(btnRefreshList);
	}
}
