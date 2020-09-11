package com.sr.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataStoreAccess {

	public static Connection getConnection(int dataStore) {
		Connection con = null;

		switch (dataStore) {

		case 1:

			String drivername = ReadConfiguration.getDrivername();
			String datastore = ReadConfiguration.getConnectionString();
			String user = ReadConfiguration.getUsername();
			String password = ReadConfiguration.getPassword();

			try {

				Class.forName(drivername).newInstance();
				con = DriverManager.getConnection(datastore, user, password);

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		return con;
	}
}
/*
 * In the next revision try to implement DAO pattern and try to Put the execute
 * query code in one place; which would just return result set or transfer
 * objects
 * 
 * Additionally try to implement logging
 * 
 */
