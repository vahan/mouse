package mouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Runner {
	
	
	public static void main(String[] args) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String inputFileName;
		String username;
		String password;
		String host;
		String port;
		String dbName;
		try {
			System.out.println("Enter the input CSV file name");
			inputFileName = br.readLine().trim();
			System.out.println("Enter the DB username");
			username = br.readLine().trim();
			System.out.println("Enter the DB password");
			password = br.readLine().trim();
			System.out.println("Enter the DB host name");
			host = br.readLine().trim();
			System.out.println("Enter the DB port");
			port = br.readLine().trim();
			System.out.println("Enter the DB name");
			dbName = br.readLine().trim();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		DataProcessor processor = new DataProcessor(inputFileName, username, password, host, port, dbName);
		if (!processor.getPsqlManager().connect(host, port, dbName))
			System.out.println("Could not connect to the DB at " + processor.getPsqlManager().getUrl());
		if (processor.process())
			System.out.println("The data was successfully read, processed and stored in DB");
		else
			System.out.println("An error accurred! Please check the above error messages");
	}

}
