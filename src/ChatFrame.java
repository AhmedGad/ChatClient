import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = -548244887587790826L;
	private JPanel contentPane;
	private JTextField textField;

	static JTextArea textArea;

	class AreYouSure extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showOptionDialog(ChatFrame.this,
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

	static void init(String s) {
		StringTokenizer tok = new StringTokenizer(s);
		MainFrame.othername = tok.nextToken();
		if (s.replaceAll(" ", "").length() > 0) {
			textArea.setText(textArea.getText() + s);
			textArea.requestFocus();
		}
	}

	/**
	 * Create the frame.
	 */
	public ChatFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new AreYouSure());

		setBounds(100, 100, 450, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(12, 12, 414, 164);
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(12, 12, 414, 229);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		layeredPane.add(scroll);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 10) {
					if (textField.getText().length() > 0) {
						String s = textField.getText() + "\n";
						try {
							ServerMethods.sendMessage(MainFrame.username, s);
						} catch (Exception e) {
							e.printStackTrace();
						}
						textArea.setText(textArea.getText() + "me: " + s);
						textField.setText("");
					}
				}
			}
		});
		textField.setBounds(12, 252, 414, 39);
		layeredPane.add(textField);
		textField.setColumns(10);
	}

	public static void messegeReceived(String s) {
		System.out.println(s);
		if (s.replaceAll(" ", "").length() > 0) {
			textArea.setText(textArea.getText() + MainFrame.othername + ": "
					+ s);
			textArea.requestFocus();
		}
	}
}
