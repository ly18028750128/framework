package org.cloud.utils.http.handler;

import org.cloud.utils.http.HttpRequestParams;

/**
 * 外部api调用里的前置和后置处理
 */
public interface IOuterApiHandler {

    public <T> T doBefore(final String url, final HttpRequestParams params, Object... otherParms) throws Exception;

    public <T> T doAfter(final String url, final HttpRequestParams params, Object... result) throws Exception;
}
