package org.cloud.mongo;

import com.mongodb.BasicDBObject;
import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * mongo db查询VO
 */
@Data
public class MongoQueryParamsDTO {

    private List<MongoQueryParam> params = new ArrayList<>();
    private List<MongoQueryOrder> orders = new ArrayList<>();
    private Map<String,Boolean> fields = new LinkedHashMap<>();   //显示的字段

}
