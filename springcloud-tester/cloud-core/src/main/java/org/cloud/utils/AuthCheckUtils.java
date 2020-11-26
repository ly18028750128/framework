package org.cloud.utils;

import org.cloud.constant.CoreConstant;
import org.cloud.constant.UnauthorizedConstant;
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

    public DataInterFaceVO checkDataInterface(final String md5) throws Exception {
        // 如果是不校验，那么全部用户均可以通过
        Query query = new Query();
        query.addCriteria(Criteria.where("md5").is(md5));
        query.addCriteria(Criteria.where("status").is(1));
        query.addCriteria(Criteria.where("interfaceType").is("sql"));
        DataInterFaceVO dataInterFaceVO = mongoTemplate.findOne(query, DataInterFaceVO.class);
        if (dataInterFaceVO == null) {
            throw new BusinessException("不存在的动态接口定义，请检查", md5);
        }
        if (!CoreConstant.AuthMethod.NOAUTH.equals(dataInterFaceVO.getAuthMethod())) {
            if (RequestContextManager.single().getRequestContext().getUser() == null) {
                throw new BusinessException("请先登录！", md5, HttpStatus.UNAUTHORIZED.value());
            } else if (CoreConstant.AuthMethod.BYUSERPERMISSION.equals(dataInterFaceVO.getAuthMethod())) {
                LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
                Set<String> userDataiterfaces = redisUtil.hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), CoreConstant.UserCacheKey.DATA_INTERFACE.value());
                if (userDataiterfaces == null || userDataiterfaces.isEmpty() || !userDataiterfaces.contains(md5)) {
                    logger.error(loginUserDetails.getUsername() + ",正在非法的请求接口：" + md5);
                    throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, UnauthorizedConstant.DATA_INTERFACE_UNAUTHORIZED.value(), null, null, Charset.forName("utf8"));
                }
            }
        }
        return dataInterFaceVO;
    }
}
