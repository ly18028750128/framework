package org.cloud.feign.utils;


import feign.FeignException;
import feign.FeignException.ServiceUnavailable;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.cloud.entity.LoginUserDetails;
import org.cloud.feign.service.IGatewayFeignClient;
import org.cloud.feign.service.IParamConfigFeignClient;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.ParamConfigVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
public final class FeignUtil {

    public LoginUserDetails getLoginUser() {
        try {
            return gatewayFeignClient.getAuthentication();
        } catch (HttpClientErrorException.Unauthorized e) {
            log.info("rest请求无权限");
        } catch (ServiceUnavailable | RetryableException serviceUnavailable) {
            log.warn("网关服务未启动，请稍后!");
        } catch (FeignException.Unauthorized e) {
            log.debug("feign请求,未带用户信息!");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.UNAUTHORIZED.value()) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    public LoginUserDetails getLoginUser(String token) {
        try {
            return gatewayFeignClient.getAuthentication(token);
        } catch (ServiceUnavailable | RetryableException serviceUnavailable) {
            log.warn("网关服务未启动，请稍后!");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.UNAUTHORIZED.value()) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public ParamConfigVO getParamConfig(String code) {
        ParamConfigVO paramConfigVO = paramConfigFeignClient.get(code);
        if(paramConfigVO==null){
            return ParamConfigVO.builder().configCode(code).build();
        }
        return paramConfigVO;
    }

    public Boolean update(ParamConfigVO paramConfigVO) {
        return paramConfigFeignClient.update(paramConfigVO);
    }

    private final IGatewayFeignClient gatewayFeignClient;
    private final IParamConfigFeignClient paramConfigFeignClient;

    @Lazy
    private FeignUtil() {
        gatewayFeignClient = SpringContextUtil.getBean(IGatewayFeignClient.class);
        paramConfigFeignClient = SpringContextUtil.getBean(IParamConfigFeignClient.class);
    }

    private static class Handler {

        private Handler() {
        }

        private static FeignUtil handler = new FeignUtil();
    }

    public static FeignUtil single() {
        return Handler.handler;
    }


}
