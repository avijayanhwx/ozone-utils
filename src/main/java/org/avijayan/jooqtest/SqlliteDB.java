package org.avijayan.jooqtest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlliteDB {

  public static Connection createNewSqliteDb(String fileName) {

    String url = "jdbc:sqlite:" + fileName;

    try {
      Connection conn = DriverManager.getConnection(url);
      if (conn != null) {
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());
        System.out.println("A new database has been created.");
      }
      return conn;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
