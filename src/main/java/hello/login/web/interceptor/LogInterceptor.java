package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

// 인터셉터 - 요청 로그
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logID";

    // [컨트롤러 호출 전에 호출]
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception
    {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        // @RequestMapping 은 handler 가 HandlerMethod 를 사용 (Spring MVC 강의 참고)
        // 정적 리소스를 사용하는 경우에는 ResourceHttpRequestHandler 사용
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;// 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
//            hm.getBean();
//            hm.getMethod();
        }
        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler); // handler 를 통해서 어떤 컨트롤러가 넘어오는지 확인 가능
        return true;
    }

    // [컨트롤러 호출 이후에 호출]
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception
    {
        log.info("postHandler [{}]", modelAndView);
    }

    // [뷰가 렌더링된 이후에 호출]
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception
    {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
