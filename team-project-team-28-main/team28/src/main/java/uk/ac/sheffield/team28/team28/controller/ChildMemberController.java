package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.LoanService;

import java.util.List;


@Controller()
@RequestMapping("/child")

public class ChildMemberController {
    private final ChildMemberService childMemberService;


    private final LoanService loanService;

    private final MusicRepository musicRepository;

    public ChildMemberController(ChildMemberService childMemberService, MusicRepository musicRepository, LoanService loanService) {
        this.childMemberService = childMemberService;
        this.musicRepository = musicRepository;
        this.loanService = loanService;

    }

    @PreAuthorize("@childMemberService.canAccessChildDashboard(#childMemberId, authentication.name)")
    @GetMapping("/dashboard/{childMemberId}")
    public String childDashboard(Model model, @PathVariable Long childMemberId) throws Exception{
        model.addAttribute("childMemberId", childMemberId);

        ChildMember member = childMemberService.getChildById(childMemberId);

        List<Loan> memberLoans = loanService.getActiveLoansByChildMemberId(member.getId());
        model.addAttribute("memberLoans", memberLoans);
        System.out.println("loans: " + memberLoans);

        BandInPractice band = member.getBand();
        if (band != BandInPractice.None) {
            List<Music> music =
                    musicRepository.findByBandInPracticeOrBandInPractice(band, BandInPractice.Both);
            model.addAttribute("musics", music);
        }

        return "childDashboard";
    }
    

    @GetMapping("allow-to-go-parent")
    public String allowToGoParent(HttpSession session) {
        Boolean isAuthorised = (Boolean) session.getAttribute("isAuthorised");
        session.setAttribute("referer", "/dashboard");
        return "redirect:/authorise";
    }


}
