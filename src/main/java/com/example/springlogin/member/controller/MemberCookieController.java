package com.example.springlogin.member.controller;

import com.example.springlogin.member.domain.Member;
import com.example.springlogin.member.service.LoginParam;
import com.example.springlogin.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
public class MemberCookieController implements MemberController {

//    @Autowired // 필드주입 방법
//    private MemberService memberService;
    MemberService memberService;
//    public MemberCookieController(MemberService memberService) {
//        this.memberService = memberService;
//    }
    @Override
    @GetMapping("login")
    public String getLoginPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("loginByCookie".equals(cookie.getName())) {
                    // 로그인된 상태라면 로그인 페이지로 이동하는게 아닌 다시 원래의 login으로 이동..
                    return "/index";
                }
            }
        }
        // 로그인이 되어있지 않다면 로그인 페이지로 이동한다...
        return "/login";
    }
    // 인터페이스는 여러가지 컨트롤러를 사용할수 있기에 근본에 충실해서 구현하는 것도 중요하다
//    public String getLoginPage(@CookieValue(value = "loginByCookie", required = false) boolean loginByCookie) {
//        if (loginByCookie) {
//            // 쿠키가 존재하는 경우
//            return "/index";
//        } else {
//            // 쿠키가 존재하지 않는 경우
//            return "/login";
//        }
//    }

    @Override
    @PostMapping("login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response, Model model) {
        // service에 보낼 객체를 만들어서 보내준다
        LoginParam loginParam = LoginParam.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        Member member = memberService.login(loginParam);
        // 로그인에 성공 했다면 !
        if (member != null) {
            Cookie cookie = new Cookie("loginByCookie", member.getEmail());
            // 쿠키 시간 디폴트로 설정
            response.addCookie(cookie);
            // 로그인 완료
            return "/index";
        }
        // 로그인이 실패한 경우
        return "/login";
        //model.addAttribute("loginByCookie", member.getEmail());


    }

    @Override
    @PostMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키 시간을 만료시켜서 쿠키를 제거한다
        // index 파일로 매핑되고 view에 들어가기 전에 컨트롤러에서 쿠키를 확인하고 주입해주기에 여기서는 쿠키 만료만 진행
        Cookie cookie = new Cookie("loginByCookie", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "index";
    }
}
