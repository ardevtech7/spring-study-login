package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

// 서블릿 필터 - 요청 로그 (모든 요청을 로그로 남기는 필터)
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    // 요청이 올 때마다 doFilter 가 호출
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request; // ServletRequest 이 부모이기 때문에 다운캐스팅
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString(); // 요청을 구분하기 위해 추가

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response); // 다음 필터가 있으면, 필터 호출. 없으면 서블릿 호출 -> 컨트롤러 호출
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI); // 컨트롤러까지 다 끝나면, 실행됨
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
