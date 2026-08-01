package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.sheffield.team28.team28.dto.MiscDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.MiscRepository;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;
import uk.ac.sheffield.team28.team28.service.*;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/committee")
public class CommitteeController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private MusicService musicService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ChildMemberService childMemberService;
    @Autowired
    private MiscService miscService;
    @Autowired
    private MiscRepository miscRepository;

    @GetMapping("/dashboard")
    public String committeeDashboard(Model model, HttpSession session) {
        // removed session used for authorizing users to go in to different parts of the web
        session.removeAttribute("isAuthorised");

        //Get logged in member
        Member member = memberService.findMember();
        model.addAttribute("member", member);
        model.addAttribute("memberType", member.getMemberType().toString());

        if (member.getMemberType() == MemberType.COMMITTEE || member.getMemberType() == MemberType.DIRECTOR) {
            List<ChildMember> children = childMemberService.getChildByParent(member);
            int childNum = children.size();
            model.addAttribute("childNum", childNum);
            model.addAttribute("children",children);

            List<Instrument> instruments = instrumentRepository.findAll();
            model.addAttribute("instruments", instruments);

            List<Misc> miscs =miscRepository.findAll();
            model.addAttribute("miscs", miscs);


            //Get all music
            List<Music> music = musicRepository.findAll();
            model.addAttribute("musics", music);

            //Get band types
            List<BandInPractice> bands = Arrays.asList(BandInPractice.values());
            model.addAttribute("bands", bands);

            //Get all orders
            List<Order> orders = orderRepository.findByItemTypeAndNotFulfilled(ItemType.Music);
            model.addAttribute("musicOrders", orders);
            model.addAttribute("orderService", orderService);
            return "committee-dashboard";
        } else {
            return "dashboard";
        }
    }

    @GetMapping("/allow-to-go-to-director")
    public String allowToGoToDirector(HttpSession session) {
        Boolean isAuthorised = (Boolean) session.getAttribute("isAuthorised");
        if (isAuthorised == null || !isAuthorised) {
            session.setAttribute("referer", "/director");
            return "redirect:/authorise";
        }
        return "redirect:/committee/dashboard";
    }


}
