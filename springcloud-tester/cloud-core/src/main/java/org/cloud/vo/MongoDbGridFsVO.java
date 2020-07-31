package org.cloud.vo;

import com.mongodb.BasicDBObject;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class MongoDbGridFsVO {
    public String get_id() {
        return _id.toString();
    }

    private ObjectId _id;

//    public String get_Id(){
//       return  _id.toString();
//    }

    private String filename;
    private Long length;
    private Long chunkSize;
    private Date uploadDate;
    private String md5;
    private BasicDBObject metadata;

}
