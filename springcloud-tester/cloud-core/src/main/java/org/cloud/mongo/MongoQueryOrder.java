package org.cloud.mongo;

import lombok.Data;

/**
 * mongo db排序VO
 */
@Data
public class MongoQueryOrder {

    private String direction;
    private String property;

}
