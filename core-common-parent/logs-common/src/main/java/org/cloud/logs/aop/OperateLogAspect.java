package org.cloud.logs.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

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

    public void setStatusAndMsg(Object res, BasicDBObject doc) {
        if (CollectionUtil.single().isEmpty(res)) {
            return;
        }
        try {
            JSONObject resJson = JSONObject.parseObject(JSON.toJSON(res).toString());
            if (resJson.containsKey("code")) {
                doc.append("resCode", resJson.getInteger("code"));
            } else {
                doc.append("resCode", resJson.getInteger("status"));
            }
            doc.append("resMsg", resJson.getString("message"));

        } catch (Exception ignored) {

        }
    }


    @Pointcut("@annotation(org.cloud.logs.annotation.AuthLog)")
    public void authLog() {
    }

    // 环绕切面持久化日志
    @Around(value = "authLog()")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        final HttpServletRequest request = HttpServletUtil.single().getHttpServlet();
        final LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        try {
            Object res = pjd.proceed(pjd.getArgs());
            new Thread(() -> saveOperateLog(pjd, res, true, "", request, loginUserDetails)).start();
            return res;
        } catch (Throwable e) {
            new Thread(() -> saveOperateLog(pjd, null, false, e.getMessage(), request, loginUserDetails)).start();
            throw e;
        }
    }

    private void saveOperateLog(ProceedingJoinPoint pjd, final Object res, Boolean successFlag, String errorMsg, HttpServletRequest request,
        LoginUserDetails loginUserDetails) {
        long startTime = System.currentTimeMillis();
        final BasicDBObject doc = new BasicDBObject();
        Date date = new Date();
        // 方法的注解对象
        MethodSignature ms = (MethodSignature) pjd.getSignature();
        Method method = ms.getMethod();
        doc.append("targetClass", pjd.getTarget().getClass().getName());
        doc.append("method", method.getName());
        AuthLog oLog = method.getAnnotation(AuthLog.class);
        if (request != null) {
            // 请求资源地址
            String uri = request.getRequestURI();
            //请求ip
            String ip = IPUtil.single().getIpAddress(request);
            CoreConstant.OperateLogType operateLogType = oLog.operateLogType();
            if (operateLogType == null || operateLogType.getLogType() == CoreConstant.OperateLogType.LOG_TYPE_DEFAULT.getLogType()) {
                operateLogType = getType(uri);
            }
            doc.append("type", operateLogType.getLogType());
            doc.append("uri", uri);
            doc.append("reqIp", ip);
        }

        final HttpServletResponse response = HttpServletUtil.single().getHttpServletResponse();
        if (response != null) {
            doc.append("httpStatus", response.getStatus());
        }
        // 设置操作人信息
        Long userId = -9999999L;
        String userName = "";

        if (loginUserDetails != null) {
            userId = loginUserDetails.getId();
            userName = loginUserDetails.getUsername();
        }
        //获取请求参数
        String targetMethodParams = Arrays.toString(pjd.getArgs());
        String microName = getMicroName();

        long endTime = System.currentTimeMillis();

        try {
            doc.append("success", successFlag);
            doc.append("errorMsg", errorMsg);
            doc.append("microName", microName);
            doc.append("bizType", oLog.bizType());
            doc.append("desc", oLog.desc());
            doc.append("params", targetMethodParams);
            doc.append("userId", userId);
            doc.append("userName", userName);

            setStatusAndMsg(res, doc);
            doc.append("spendTime", endTime - startTime);
            doc.append(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value(), date);
            mongoTemplate.insert(doc, microName + CoreConstant.MongoDbLogConfig.MONGODB_OPERATE_LOG_SUFFIX.value());
        } catch (Exception e) {
            log.error("保存操作日志错误", e);
        }
    }


}
