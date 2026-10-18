package com.fans.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

/** 把任意 Controller 方法参数 Req 解析为合并后的请求参数 */
@Component
public class ReqArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper om;

    public ReqArgumentResolver(ObjectMapper om) {
        this.om = om;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Req.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return Req.of(request, om);
    }
}
