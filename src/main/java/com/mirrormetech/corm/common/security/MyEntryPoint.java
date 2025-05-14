package com.mirrormetech.corm.common.security;

import cn.hutool.json.JSONUtil;
import com.mirrormetech.corm.common.result.ApiResult;
import com.mirrormetech.corm.common.exception.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证错误处理器
 *
 * @author RudeCrab
 */
@Slf4j
public class MyEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error(e.getMessage());
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ApiResult<String> apiResultVO = ApiResult.fail(ResultCode.UNAUTHORIZED);
        JSONUtil.toJsonStr(apiResultVO, out);
        //out.write(apiResultVO.toString());
        out.flush();
        out.close();
    }
}
