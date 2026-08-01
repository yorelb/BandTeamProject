package uk.ac.sheffield.team28.team28.controller;

//import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.model.Member;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Controller()
@RequestMapping("/")

public class MemberController {
    private final MemberService memberService;

    private final ChildMemberRepository childMemberRepository;
    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final ChildMemberService childMemberService;

    public MemberController(MemberService memberService, ChildMemberRepository childMemberRepository, ChildMemberService childMemberService) {
        this.memberService = memberService;
        this.childMemberRepository = childMemberRepository;
        this.childMemberService = childMemberService;
    }



    @GetMapping("/authorise")
    public String showAuthorisePage(Model model) {
        Member member = memberService.findMember();
        System.out.println("Received memberId: " + member.getFirstName());
        model.addAttribute("member", member);
        model.addAttribute("memberType", member.getMemberType().toString());
        model.addAttribute("We have got the page");
        return "authorise";
    }

    @PostMapping("/authorise")
    public String authorise(@RequestParam String password,
            Model model, HttpSession session) {
        Member member = memberService.findMember();
        model.addAttribute("memberType", member.getMemberType().toString());
        System.out.println(model.getAttribute("memberType"));
        System.out.println("Received memberId: " + member.getId());
        System.out.println("Received password: " + password);
        System.out.println("Received password: " + member.getPassword());

        String referer = session.getAttribute("referer").toString();
        try {
            boolean authorised = memberService.authorise(member.getId(), password);
            if (authorised) {
                session.setAttribute("isAuthorised", true);
                String safeReferer = (referer != null) ? referer : "/dashboard";
                model.addAttribute("message", true);
                return "redirect:" + safeReferer;
            } else {
                model.addAttribute("error", "Invalid password. Please try again.");
                return "authorise";
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "authorise";
        }
    }

    @PostMapping("/addChild")
    public String addChild(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String dateOfBirth,
                           Model model) {
        try {
            Member parent = memberService.findMember();
            ChildMember child = new ChildMember();
            String formattedFirstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            String formattedLastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
            LocalDate dob = LocalDate.parse(dateOfBirth);

            child.setFirstName(formattedFirstName.trim());
            child.setLastName(formattedLastName.trim());
            child.setDateOfBirth(dob);
            child.setParent(parent);

            childMemberRepository.save(child);
            return "redirect:/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "error";
        }
    }


    @GetMapping("allow-to-go-committee")
    public String allowToGoParent(HttpSession session) {
        Boolean isAuthorised = (Boolean) session.getAttribute("isAuthorised");
        if (isAuthorised == null || !isAuthorised) {
            session.setAttribute("referer", "/committee/dashboard");
            return "redirect:/authorise";
        }
        return "redirect:/dashboard";
    }



}
