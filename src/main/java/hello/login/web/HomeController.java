package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// Login 전에 홈화면을 보여준다.
// Login 성공하면,
// -> 로그인 사용자 홈화면을 보여주기 위해서 요청에서 세션을 가져와서 세션 조회
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    // [Login V1] - 쿠키 사용
//    @GetMapping("/")
    public String homeLogin(
            // 쿠키값이 없는 로그인 안 한 사용자도 들어오기 때문에 required = false
            // 쿠키값은 String 이지만 스프링이 자동으로 타입 컨버터로 변환
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model
    ) {
        // 쿠키없는 사용자 로그인
        if (memberId == null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        // 쿠키가 없는 경우 : 쿠키 세션 기간이 끝난 경우
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인 사용자 전용 화면
    }

    // [Login V2] - 직접 만든 세션 사용
//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        // 세션 관리자에 저장된 회원 조회
        Member member = (Member) sessionManager.getSession(request);

        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";
    }

    // [Login V3] - 서블릿이 제공하는 HttpSession 사용
//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        // 세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인 홈화면으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // [Login V3Spring] - 스프링이 제공하는 @SessionAttribute 사용
//    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model
    ) {
        // 세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인 홈화면으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // [LoginV3ArgumentResolver] ArgumentResolver 활용, custom annotation
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {
        // 세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}