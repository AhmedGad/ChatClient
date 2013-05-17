import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerMethods {
	static URL url;

	static {
		try {
			ServerMethods.url = new URL("http://myserver.herokuapp.com");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	static String password = "androidelteety ";

	static boolean register(String userName) throws Exception {
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "register " + userName).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		return tempstr.contains("succsessfully");
	}

	static boolean unregister(String userName) throws Exception {
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "unregister " + userName).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		return tempstr.contains("succsessfully");
	}

	static void host(String userName) throws Exception {
		connected = true;
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "host " + userName).getBytes());
		os.close();

		InputStream in = httpConn.getInputStream();

		Scanner myScanner = new Scanner(in);
		String tempstr = "";
		boolean first = true;
		while (connected) {
			while (myScanner.hasNext()) {
				tempstr = myScanner.nextLine() + "\n";
				System.out.println(tempstr);
				if (first) {
					if (tempstr.trim().length() > 0) {
						if (tempstr.contains("successfully"))
							ChatFrame.textArea.setText(tempstr);
						else {
							ChatFrame.init(tempstr);
							first = false;
						}
					}
				} else {
					ChatFrame.messegeReceived(tempstr);
				}
			}
			in.close();
			myScanner.close();
			in = reconnect(userName);
			myScanner = new Scanner(in);
		}
		in.close();
	}

	static boolean connected = false;

	static boolean sendMessage(String from, String message) throws Exception {
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "message " + from + " " + message).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		System.out.println(tempstr);
		in.close();
		return tempstr.contains("succsessfully");
	}

	static boolean connect(String userName, String hostName) throws Exception {
		connected = true;
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "connect " + userName + " " + hostName).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		Scanner myScanner = new Scanner(in);
		String tempstr = "";
		while (connected) {
			while (myScanner.hasNext()) {
				tempstr = myScanner.nextLine() + "\n";
				ChatFrame.messegeReceived(tempstr);
			}
			in.close();
			myScanner.close();
			in = reconnect(userName);
			myScanner = new Scanner(in);
		}
		in.close();
		return tempstr.contains("succsessfully");
	}

	static InputStream reconnect(String userName) throws Exception {
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "reconnect " + userName).getBytes());
		os.close();
		System.out.println("jhabsndjasdd");
		return httpConn.getInputStream();
	}

	static boolean diconnect(String userName) throws Exception {
		connected = false;
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		httpConn.connect();
		OutputStream os = httpConn.getOutputStream();
		os.write((password + "disconnect " + userName).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		return tempstr.contains("succsessfully");
	}

	static ArrayList<String> getHostsList() throws Exception {
		URL url = new URL("http://myserver.herokuapp.com/?show=hosts");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("GET");
		httpConn.connect();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		Scanner myScanner = new Scanner(tempstr);
		myScanner.nextLine();
		ArrayList<String> res = new ArrayList<String>();
		while (myScanner.hasNext())
			res.add(myScanner.next());
		myScanner.close();
		return res;
	}

}
