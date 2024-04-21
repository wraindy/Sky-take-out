package com.sky.interceptor;

import com.sky.context.BaseContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Wraindy
 * @DateTime 2024/04/21 17:46
 * Description 移除线程变量，避免内存泄漏
 * Notice
 **/
@Component
public class RemoveLocalThreadInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 清理操作
        BaseContext.removeCurrentId();
    }

}
