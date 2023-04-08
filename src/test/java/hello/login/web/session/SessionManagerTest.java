package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SessionManagerTest {
    SessionManager sessionManager = new SessionManager();

    @DisplayName("Session 테스트")
    @Test
    void sessionTest() {
        // 세션 생성 (서버 -> 클라이언트)
        // 서버에서 쿠키를 만들고 response 에 담아서 클라이언트로 응답이 나간 상태로 가정
        // public void createSession(Object value, HttpServletResponse response) 메소드 테스트
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        sessionManager.createSession(member, response);

        // 웹 브라우저의 요청이라고 가정
        // 웹 브라우저가 쿠키를 만들어서 서버에 전달 (클라이언트 -> 서버)
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); // mySessionId=1213-~

        // 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        // 세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }
}
