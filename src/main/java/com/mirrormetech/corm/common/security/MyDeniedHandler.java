package com.mirrormetech.corm.common.security;

import cn.hutool.json.JSONUtil;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.common.exception.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 授权错误处理器
 *
 * @author RudeCrab
 */
public class MyDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ApiResult<String> apiResultVO = ApiResult.fail(ResultCode.FORBIDDEN);
        JSONUtil.toJsonStr(apiResultVO, out);
        //out.write(apiResultVO.toString());
        out.flush();
        out.close();
    }
}
