package org.avijayan.jooqtest;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.avijayan.jooqgenerated.tables.OrphanedBlockCandidates;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.derby.sys.Sys;

public class JooqHelper {

  DSLContext dslContext;

  public static final String ORPHANED_BLOCKS_TABLE_NAME = "orphaned_block_candidates";
  public static final String RUN_ID_COLUMN = "run_id";
  public static final String RUN_TS_COLUMN = "run_timestamp";
  public static final String CONTAINER_ID_COLUMN = "container_id";
  public static final String LOCAL_ID_COLUMN = "local_id";

  public JooqHelper(Connection connection) {
    dslContext = DSL.using(connection);
  }

  public void createTable() {

    /*
     * CREATE TABLE `orphaned_block_candidates` (
     * `timestamp` TIMESTAMP,
     * `container_id` BIGINT,
     *`local_id` BIGINT
     * );
     */

    int ret = dslContext.createTable(ORPHANED_BLOCKS_TABLE_NAME)
        .column(RUN_ID_COLUMN, SQLDataType.INTEGER)
        .column(RUN_TS_COLUMN, SQLDataType.TIMESTAMP)
        .column(CONTAINER_ID_COLUMN, SQLDataType.BIGINT)
        .column(LOCAL_ID_COLUMN, SQLDataType.BIGINT)
        .constraints(
            DSL.constraint("PK_orpaned_blocks").primaryKey(RUN_ID_COLUMN)
        )
        .execute();
  }

  public void insertIntoTable(int id, Timestamp timestamp, long containerId, long
      localId) {

    dslContext.insertInto(DSL.table(DSL.name(ORPHANED_BLOCKS_TABLE_NAME)))
        .set(DSL.field(DSL.name(RUN_ID_COLUMN)), id)
        .set(DSL.field(DSL.name(RUN_TS_COLUMN)), timestamp)
        .set(DSL.field(DSL.name(CONTAINER_ID_COLUMN)), containerId)
        .set(DSL.field(DSL.name(LOCAL_ID_COLUMN)), localId)
        .execute();
  }

  public int getCountOfTable() {

    int count =
        dslContext
            .selectCount()
            .from(DSL.table(ORPHANED_BLOCKS_TABLE_NAME))
            .fetchOne(0, int.class);
    return count;
  }

  public static Condition condition(Map<Field<?>, Object> map) {
    Condition result = DSL.trueCondition();

    for (Map.Entry<Field<?>, Object> entry : map.entrySet()) {
      result = result.and(((Field) entry.getKey()).eq(entry.getValue()));
    }

    return result;
  }

  public void selectHelper(int runId, long containerId) {

    OrphanedBlockCandidates table = OrphanedBlockCandidates
        .ORPHANED_BLOCK_CANDIDATES;

    Map<Field<?>, Object> map = new HashMap<>();
    if (runId != -1) {
      map.put(table.RUN_ID, runId);
    }
    if (containerId != -1) {
      map.put(table.CONTAINER_ID, containerId);
    }

    Result<Record> selectResult = dslContext
        .select()
        .from(table)
        .where(condition(map))
        .fetch();

    for (Record r : selectResult) {
      System.out.println(r.get(0) + ":" + r.get(1) + ":" + r.get(2) + ":" + r
          .get(3));
    }
  }

  public void insertIntoTableUsingGeneratedClasses(int id,
                                                   Timestamp timestamp,
                                                   long containerId,
                                                   long localId) {

    OrphanedBlockCandidates table = OrphanedBlockCandidates
        .ORPHANED_BLOCK_CANDIDATES;
    dslContext.insertInto(table)
        .set(table.RUN_ID, id)
        .set(table.RUN_TIMESTAMP, timestamp)
        .set(table.CONTAINER_ID, containerId)
        .set(table.LOCAL_ID, localId)
        .execute();
  }
}
