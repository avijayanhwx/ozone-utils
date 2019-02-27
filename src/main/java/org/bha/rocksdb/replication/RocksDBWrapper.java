package org.bha.rocksdb.replication;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.hdds.protocol.proto.HddsProtos;
import org.apache.hadoop.ozone.om.helpers.OmKeyInfo;
import org.apache.hadoop.ozone.om.helpers.OmKeyLocationInfoGroup;
import org.bha.rocksdb.RocksDBWrite;
import org.rocksdb.RocksDBException;

import com.google.common.collect.Lists;

public class RocksDBWrapper {
  private RocksDBWrite rocksDBWrite;
  private String path;
  private String volumeName;
  private String bucketName;
  private List<RocksDBWrapper> listeners;

  public void openDB(String dbPath, String volume, String bucket)
      throws RocksDBException {
    rocksDBWrite = new RocksDBWrite();
    path = dbPath;
    rocksDBWrite.openDB(path, false);
    volumeName = volume;
    bucketName = volume;
  }

  public void deleteDB() {
    if (path != null) {
      FileUtil.fullyDelete(new File(path));
    }
  }

  public void writeKeys(long numKeys) throws Exception {
    for (int i = 1; i <= numKeys; i++) {
      String keyName = "Key-" + i;

      OmKeyInfo omKeyInfo =
          new OmKeyInfo.Builder().setVolumeName(volumeName)
              .setBucketName(bucketName)
              .setKeyName(keyName)
              .setReplicationFactor(HddsProtos.ReplicationFactor.THREE)
              .setReplicationType(HddsProtos.ReplicationType.RATIS)
              .setOmKeyLocationInfos(Lists.newArrayList(
                  new OmKeyLocationInfoGroup(0,
                      rocksDBWrite.getKeyInfoList()))).build();

      byte[] key =
          rocksDBWrite.getKey(volumeName, bucketName, keyName).getBytes();
      byte[] val = omKeyInfo.getProtobuf().toByteArray();

      // Doing similar what we do in OM
      rocksDBWrite.put(key, val);
      rocksDBWrite.commit(key, val);
    }
  }

  public void addListener(RocksDBWrapper rocksDBWrapper) {
    this.listeners.add(rocksDBWrapper);
  }

}
