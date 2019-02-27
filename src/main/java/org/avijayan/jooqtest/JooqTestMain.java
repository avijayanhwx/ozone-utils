package org.avijayan.jooqtest;

import java.sql.Connection;
import java.sql.Timestamp;

import org.avijayan.jooqgenerated.tables.OrphanedBlockCandidates;

public class JooqTestMain {

  public static void main(String[] args) {

    String fileName = null;
    try {
      String tempDir = System.getProperty("java.io.tmpdir");
      fileName = tempDir + "sqlite_1551302464380.db";
      //tempDir + "sqlite_" + System
      // .currentTimeMillis() + "" +
      //    ".db";
      System.out.println("DB Location : " + fileName);
      Connection connection = SqlliteDB.createNewSqliteDb(fileName);

      JooqHelper jooqHelper = new JooqHelper(connection);
      //jooqHelper.createTable();

      for (int i = 16; i <= 20; i++) {
        jooqHelper.insertIntoTableUsingGeneratedClasses(i,
            new Timestamp(System.currentTimeMillis()),
            i * 100,
            i);
      }
      System.out.println("NumRecords written : " + jooqHelper.getCountOfTable());

      jooqHelper.selectHelper(-1, -1);

      jooqHelper.selectHelper(1, 100);



    } catch (Exception e) {
      e.printStackTrace();
    } finally {
//      FileUtils.deleteQuietly(new File(fileName));
    }
  }
}
