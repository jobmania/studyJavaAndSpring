package com.demo.interceptor;

import com.demo.beans.ContentBean;
import com.demo.beans.LoginUserBean;
import com.demo.service.BoardService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckWriterInterceptor implements HandlerInterceptor {

    private LoginUserBean loginUserBean;
    private BoardService boardService;

    public CheckWriterInterceptor(LoginUserBean loginUserBean, BoardService boardService){
        this.loginUserBean = loginUserBean;
        this.boardService = boardService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String str1 = request.getParameter("content_idx");
        int content_idx = Integer.parseInt(str1);
        ContentBean currentContentBean = boardService.getContentInfo(content_idx);

        if(currentContentBean.getContent_writer_idx() != loginUserBean.getUser_idx()){
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/board/not_writer");
            return false;
        }


        return true;
    }
}
