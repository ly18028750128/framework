package org.cloud.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.annotation.AuthLog;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 *
 */
@Slf4j
@Aspect
public class OperateLogAspect {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${spring.application.name:}")
    private String microName;

    public String getMicroName() {
        return microName.toUpperCase();
    }

    public CoreConstant.OperateLogType getType(String uri) {
        if (uri != null && uri.matches("^/admin/.*$")) {
            return CoreConstant.OperateLogType.LOG_TYPE_BACKEND;
        }
        return CoreConstant.OperateLogType.LOG_TYPE_FRONTEND;
    }

    public int getResultCode(Object res) {
        if (!StringUtils.isEmpty(res)){
            JSONObject resJson = JSONObject.parseObject(JSON.toJSON(res).toString());
            Integer code = resJson.getInteger("code");
            if (code != null){
                return code;
            }
            Integer status = resJson.getInteger("status");
            if (status != null){
                return status;
            }
        }
        return -999;
    }

    public String getResultMsg(Object res) {
        if (!StringUtils.isEmpty(res)){
            JSONObject resJson = JSONObject.parseObject(JSON.toJSON(res).toString());
            String message = resJson.getString("message");
            return message;
        }
        return "-999";
    }

    @Pointcut("@annotation(org.cloud.annotation.AuthLog)")
    public void authLog(){}

    //环绕切面持久化日志
    @Around("authLog()")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Exception {
        long startTime = System.currentTimeMillis();
        final BasicDBObject doc = new BasicDBObject();
        Date date = new Date();
        // 方法的注解对象
        MethodSignature ms = (MethodSignature) pjd.getSignature();
        Method method = ms.getMethod();
        AuthLog oLog = method.getAnnotation(AuthLog.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        // 请求资源地址
        String uri = request.getRequestURI();
        //请求ip
        String ip = CommonUtil.single().getIpAddress(request);
        // 设置操作人信息
        Long userId = null;
        String userName = "";
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        if (loginUserDetails != null) {
            userId = loginUserDetails.getId();
            userName = loginUserDetails.getUsername();
        }
        //获取请求参数
        String targetMethodParams=Arrays.toString(pjd.getArgs());
        String microName = getMicroName();
        Object res = null;
        try {
            res = pjd.proceed(pjd.getArgs());
        }
        catch (Throwable e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
        long endTime = System.currentTimeMillis();

        CoreConstant.OperateLogType operateLogType = oLog.operateLogType();
        if (operateLogType == null || operateLogType.getLogType() == CoreConstant.OperateLogType.LOG_TYPE_DEFAULT.getLogType()){
            operateLogType = getType(uri);
        }
        try {
            doc.append("microName", microName);
            doc.append("type", operateLogType.getLogType());
            doc.append("bizType", oLog.bizType());
            doc.append("uri", uri);
            doc.append("desc", oLog.desc());
            doc.append("reqIp", ip);
            doc.append("params", targetMethodParams);
            doc.append("userId", userId);
            doc.append("userName", userName);
            doc.append("resCode", getResultCode(res));
            doc.append("resMsg", getResultMsg(res));
            doc.append("spendTime", endTime - startTime);
            doc.append(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value(), date);
            mongoTemplate.insert(doc, microName + CoreConstant.MongoDbLogConfig.MONGODB_OPERATE_LOG_SUFFIX.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


}
