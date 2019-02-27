create database recondb;

CREATE TABLE `orphaned_block_candidates` (
  `timestamp` TIMESTAMP,
  `container_id` BIGINT,
  `local_id` BIGINT
);