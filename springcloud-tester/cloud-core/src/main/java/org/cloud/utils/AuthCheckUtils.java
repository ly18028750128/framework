package org.cloud.utils;

import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.mongo.DataInterFaceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.util.Set;

public final class AuthCheckUtils {
    private MongoTemplate mongoTemplate;
    private RedisUtil redisUtil;
    private Logger logger = LoggerFactory.getLogger(AuthCheckUtils.class);

    private AuthCheckUtils() {
        mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        redisUtil = SpringContextUtil.getBean(RedisUtil.class);
    }

    private static AuthCheckUtils instance = new AuthCheckUtils();

    public static AuthCheckUtils single() {
        return instance;
    }

    public DataInterFaceVO checkDataInterface(final String ObjectId) throws Exception {
        // 如果是不校验，那么全部用户均可以通过
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(ObjectId)));
        query.addCriteria(Criteria.where("status").is(1));
        query.addCriteria(Criteria.where("interfaceType").is("sql"));
        DataInterFaceVO dataInterFaceVO = mongoTemplate.findOne(query, DataInterFaceVO.class);
        if (dataInterFaceVO == null) {
            throw new BusinessException("不存在的动态接口定义，请检查", ObjectId);
        }
        if (CoreConstant.AuthMethod.ALLSYSTEMUSER.equals(dataInterFaceVO.getAuthMethod())) {
            if (RequestContextManager.single().getRequestContext().getUser() == null) {
                throw new BusinessException("请先登录！", ObjectId, HttpStatus.UNAUTHORIZED.value());
            }
        } else if (CoreConstant.AuthMethod.BYUSERPERMISSION.equals(dataInterFaceVO.getAuthMethod())) {
            LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
            Set<String> userDataiterfaces = redisUtil.hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), CoreConstant.UserCacheKey.DATA_INTERFACE.value());
            if (userDataiterfaces == null || userDataiterfaces.isEmpty() || !userDataiterfaces.contains(ObjectId)) {
                logger.error(loginUserDetails.getUsername() + ",正在非法的请求接口：" + ObjectId);
                throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "没有此接口权限，请联系管理员授权！", null, null, Charset.forName("utf8"));
            }
        }
        return dataInterFaceVO;
    }
}
