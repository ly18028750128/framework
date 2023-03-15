/*
 Navicat Premium Data Transfer

 Source Server         : 309-192.168.30.239
 Source Server Type    : MongoDB
 Source Server Version : 50010 (5.0.10)
 Source Host           : 192.168.30.239:10003
 Source Schema         : YS-LONGYOU-framework

 Target Server Type    : MongoDB
 Target Server Version : 50010 (5.0.10)
 File Encoding         : 65001

 Date: 13/03/2023 14:37:59
*/


// ----------------------------
// Collection structure for jobLogs_collection
// ----------------------------
db.getCollection("jobLogs_collection").drop();
db.createCollection("jobLogs_collection");
db.getCollection("jobLogs_collection").createIndex({
    serviceName: NumberInt("1"),
    jobName: NumberInt("1")
}, {
    name: "idx_joblogs_collection_1"
});

// ----------------------------
// Collection structure for message_logs
// ----------------------------
db.getCollection("message_logs").drop();
db.createCollection("message_logs");
db.getCollection("message_logs").createIndex({
    serviceName: NumberInt("1"),
    sender: NumberInt("1"),
    to: NumberInt("1")
}, {
    name: "idx_message_logs_1"
});
