package org.cloud.utils.mongo;

public enum MongoDBEnum {
    metadataKey("metadata", "文件属性"),
    metadataContentTypeKey("contentType", "文件类型"),
    metadataOwnerKey("owner", "文件拥有者ID"),
    metadataOwnerNameKey("ownerName", "文件拥有者账号"),
    metadataOwnerFullNameKey("ownerFullName", "文件拥有者全称"),
    defaultFileOwnerId("-999999999999999","默认的上传文件人，当未登录的时候用到");

    private String key;
    private String name;

    MongoDBEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String value() {
        return this.key;
    }

    public Long getLong(){
        return Long.valueOf(this.key);
    }
}
