package org.cloud.utils.mongo;

public enum MongoDBEnum {
    metadataKey("metadata", "文件属性"),
    metadataContentTypeKey("contentType", "文件类型，上传时的通用类型"),
    metadataOwnerKey("owner", "文件拥有者ID"),
    metadataOwnerNameKey("ownerName", "文件拥有者账号"),
    metadataOwnerFullNameKey("ownerFullName", "文件拥有者全称"),
    metadataFileAuthRangeFieldName("fileAuthRange","metadata保存文件类型的字段名称"),
    metadataFileAuthRangeResource("resource","资源文件，保存在metadata的fileType里"),
    metadataFileAuthRangePersonal("personal","个人文件，保存在metadata的fileType里"),
    metadataFileAuthRangePublic("public","公共文件，保存在metadata的fileType里"),
    metadataFilesSuffixFieldName("suffix","metadata保存文件文件后缀字段"),
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
