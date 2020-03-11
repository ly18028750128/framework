package org.cloud.mongo;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * mongo db查询VO
 */
@Data
public class MongoQueryParamsDTO {

    private List<MongoQueryParam> params = new ArrayList<>();
    private List<MongoQueryOrder> orders = new ArrayList<>();

}
