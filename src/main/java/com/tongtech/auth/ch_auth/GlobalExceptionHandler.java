package com.tongtech.auth.ch_auth;

import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yxy
 * @version 1.0.0
 * @date 2020/04/14 22:07
 * @ControllerAdvice 捕获 Controller 层抛出的异常，如果添加 @ResponseBody 返回信息则为JSON 格式。
 * @RestControllerAdvice 相当于 @ControllerAdvice 与 @ResponseBody 的结合体。
 * @ExceptionHandler 统一处理一种类的异常，减少代码重复率，降低复杂度。
 * <p>
 * <p>
 * 因为我们这里全部异常信息都约定返回json，所以直接使用 @RestControllerAdvice 代替 @ControllerAdvice ，这样在方法上就可以不需要添加 @ResponseBody了
 * <p>
 * 步骤：
 * 1.创建一个 GlobalExceptionHandler 类，并添加上 @RestControllerAdvice 注解就可以实现异常通知类的定义了
 * 2.定义的方法中添加上 @ExceptionHandler 即可实现Controller层的异常捕捉
 * <p>
 * <p>
 * 其他说明：
 * 通过 @ControllerAdvice（或者@RestControllerAdvice） 和 @ExceptionHandler 实现了对全局异常的捕获。 *
 * 这里仅定义了2个异常，一个是自定义的BaseCustomException，另外一个是RuntimeException，按需增加自定义的异常类即可
 */
@RestControllerAdvice(basePackages = {"com.tongtech.controller","com.tongtech.auth.controller"})
public class GlobalExceptionHandler {
    /**
     * 如果需要捕获多个异常   定义如下：@ExceptionHandler({a.class,b.class})
     *
     * @param request
     * @param e
     * @param response
     * @return
     */
//    @ExceptionHandler({BaseCustomException.class})
//    public JsonResult baseCustomExceptionHandler(HttpServletRequest request, final Exception e,
//                                                 HttpServletResponse response) {
//        BaseCustomException exception = (BaseCustomException) e;
//        return new JsonResult(exception.getStatus(), exception.getMsg(), exception.getData());
//    }
//
//    @ExceptionHandler(AuthorizationException.class)
//    public JsonResult authorizationExceptionHandler(HttpServletRequest request, final Exception e,
//                                                    HttpServletResponse response) {
//        return JsonResult.noAuthority("您没有访问权限");
//    }
//
//
//    @ExceptionHandler(UnexpectedRollbackException.class)
//    public JsonResult unexpectedRollbackExceptionHandler(HttpServletRequest request, final Exception e,
//                                                         HttpServletResponse response) {
//        return JsonResult.error("数据处理异常，请及时反馈");
//    }
//
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public JsonResult noHandlerFoundExceptionHandler(HttpServletRequest request, final Exception e,
//                                                     HttpServletResponse response) {
//        return JsonResult.error("请求资源不存在");
//    }
//
//
//    @ExceptionHandler(NoTransactionException.class)
//    public JsonResult noTransactionExceptionHandler(HttpServletRequest request, final Exception e,
//                                                    HttpServletResponse response) {
//        NoTransactionException exception = (NoTransactionException) e;
//        return JsonResult.error("事务处理出现问题", exception.getMessage());
//    }
//
//
//    @ExceptionHandler(NullPointerException.class)
//    public JsonResult nullPointerExceptionHandler(HttpServletRequest request, final Exception e,
//                                                  HttpServletResponse response) {
//        return JsonResult.error("传输参数形式错误或对象为空");
//    }
//
//
//    @ExceptionHandler({RuntimeException.class})
//    public JsonResult runtimeExceptionHandler(HttpServletRequest request, final Exception e,
//                                              HttpServletResponse response) {
//        String errMsg = e.getMessage();
//        if (errMsg != null && errMsg.contains("a foreign key constraint fails")) {
//            errMsg = "数据原因导致违反外键约束，请核实。";
//        }
//        else {
//            errMsg = "内部错误";
//        }
//        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), errMsg, null);
//    }

    @ExceptionHandler({Exception.class})
    public RestResult exceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        int status = response.getStatus();
        System.out.println(status);
        e.printStackTrace();
        return RestResultFactory.createFailedResult("");
//        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "内部错误", null);
    }
}
