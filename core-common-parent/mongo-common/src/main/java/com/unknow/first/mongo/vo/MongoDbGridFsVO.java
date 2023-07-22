package com.unknow.first.mongo.vo;

import com.unknow.first.mongo.dto.MetadataDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@ApiModel("mongo GridFs文件VO")
public class MongoDbGridFsVO {

    public String get_id() {
        return _id.toString();
    }

    @ApiModelProperty("mongodbId,自动生成")
    private ObjectId _id;

    //    public String get_Id(){
//       return  _id.toString();
//    }
    @ApiModelProperty("文件名称")
    private String filename;
    @ApiModelProperty("文件大小")
    private Long length;
    @ApiModelProperty("文件块数量")
    private Long chunkSize;
    @ApiModelProperty("更新日期")
    private Date uploadDate;
    @ApiModelProperty("md5hash值")
    private String md5;
    @ApiModelProperty("元数据")
    private MetadataDTO metadata;

}
