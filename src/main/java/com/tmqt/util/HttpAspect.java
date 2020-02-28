package com.tmqt.util;

import com.tmqt.dto.RestResultBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by chenw on 2018/12/19.
 */
@Aspect
@Component
public class HttpAspect {
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);//参数为当前使用的类名

    @Pointcut("execution(public * com.tmqt.web.*.*(..)) && !execution(public * com.tmqt.web.LoginController.exit(..)) && !execution(public * com.tmqt.web.ExportExcelController.*(..))")//要处理的方法，包名+类名+方法名
    public void cut(){
    }

    @Before("cut()")//在调用上面 @Pointcut标注的方法前执行以下方法
    public void doBefore(JoinPoint joinPoint){//用于获取类方法
        logger.info("----doStart-----------");
    }

    @After("cut()")//无论Controller中调用方法以何种方式结束，都会执行
    public void doAfter(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  attributes.getRequest();
        String userId = request.getSession().getAttribute("userId").toString();

        //url
        logger.info("url ={}",request.getRequestURI());
        //method
        logger.info("method={}",request.getMethod());
        //ip
        logger.info("ip={}",request.getRemoteAddr());
        //用户id
        logger.info("userId={}",userId);
        //类方法
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+'.'+ joinPoint.getSignature().getName());//获取类名及类方法
        //参数
        logger.info("args={}",joinPoint.getArgs());
        logger.info("----doAfter-----------");
    }

    @AfterReturning(returning = "obj",pointcut = "cut()")//在调用上面 @Pointcut标注的方法后执行。用于获取返回值
    public void doAfterReturning(Object obj){
        logger.info("response={}",obj.toString());
    }
}
