package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.security.Principal;
import java.util.Optional;

@Controller()
@RequestMapping("/")
public class HomeController {


    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public HomeController(MemberRepository memberRepository, MemberService memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        Member member = memberService.findMember();
        model.addAttribute("member", member);
        return "home";
    }

}
