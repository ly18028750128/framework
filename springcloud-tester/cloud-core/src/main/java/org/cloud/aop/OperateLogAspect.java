package org.cloud.aop;

import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.annotation.AuthLog;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.SpringContextUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 *
 */
@Slf4j
public abstract class OperateLogAspect {

    private static MongoTemplate mongoTemplate;

    /**
     * 获取服务名称
     * @return
     */
    public abstract String getMicroName();

    /**
     * 获取请求类型（前台用户或者后台管理）
     * @param uri
     * @return
     */
    public abstract CoreConstant.OperateLogType getType(String uri);


    /**
     * 获取返回结果状态码
     * @param res
     * @return
     */
    public abstract int getResultCode(Object res);
    /**
     * 获取返回结果描述
     * @param res
     * @return
     */
    public abstract String getResultMsg(Object res);

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
        int type = getType(uri).getLogType();
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
        try {
            if (mongoTemplate == null){
                mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
            }
            doc.append("microName", microName);
            doc.append("type", type);
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
