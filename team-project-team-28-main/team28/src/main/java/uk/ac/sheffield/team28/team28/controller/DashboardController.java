package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.dto.MiscDto;
import uk.ac.sheffield.team28.team28.dto.MusicDto;
import uk.ac.sheffield.team28.team28.dto.OrderDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.service.InstrumentService;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;
import uk.ac.sheffield.team28.team28.service.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

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
    private PerformanceService performanceService;


    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // removed session used for authorizing users to go in to different parts of the web
        session.removeAttribute("isAuthorised");

        //Get logged in member
        Member member = memberService.findMember();
        model.addAttribute("member", member);
        model.addAttribute("memberType", member.getMemberType().toString());
        List <ChildMember> children = childMemberService.getChildByParent(member);
        int childNum = children.size();
        model.addAttribute("childNum", childNum);
        model.addAttribute("children",children);

        //Get music and performances based on band
        BandInPractice band = member.getBand();
        if (band != BandInPractice.None) {
            List<Music> music =
                    musicRepository.findByBandInPracticeOrBandInPractice(band, BandInPractice.Both);
            model.addAttribute("musics", music);
            List<Performance> performances =
                    performanceService.getPerformanceByBand(band, BandInPractice.Both);
            model.addAttribute("performances", performances);
        }



        List<Loan> memberLoans = loanService.getActiveLoansByMemberId(member.getId());
        model.addAttribute("memberLoans", memberLoans);



        return "dashboard";
    }

    @PostMapping("/addInstrument")
    public String addInstrument(@ModelAttribute("instrument") InstrumentDto dto){
        instrumentService.saveInstrument(dto);
        return "redirect:/committee/dashboard";
    }

    @PostMapping("/editInstrument")
    public String editInstrument(@ModelAttribute("instrument") InstrumentDto dto){
        instrumentService.updateInstrument(dto);
        return "redirect:/committee/dashboard";
    }

    @PostMapping("/deleteInstrument")
    public String deleteInstrument(@RequestParam("instrumentId") Long id) {
        instrumentService.deleteInstrument(id);
        return "redirect:/committee/dashboard";
    }

    @GetMapping("/loanDetails")
    public String getLoanDetails(@RequestParam Long loanId, Model model) {
        Loan selectedLoan = loanService.findLoanById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        model.addAttribute("selectedLoan", selectedLoan);
        model.addAttribute("memberType", memberService.findMember().getMemberType());
        System.out.println("Selected Loan: " + selectedLoan);
        return "dashboard";
    }

    @PostMapping("/addMusic")
    public String addMusic(@ModelAttribute("music") MusicDto dto){
        musicService.saveMusic(dto);
        return "redirect:/committee/dashboard";
    }

    @PostMapping("/orderMusic")
    public String orderMusic(@ModelAttribute("order") OrderDto dto){
        Member member = memberService.findMember();
        dto.setMember(member);
        Item item = itemService.findById(dto.getItemId());
        dto.setItem(item);
        System.out.println("Local date is: " + LocalDate.now());
        dto.setOrderDate(LocalDate.now());
        orderService.save(dto);
        return "redirect:/dashboard";
    }
    @PostMapping("/addMisc")
    public String addMisc(@ModelAttribute("misc") MiscDto dto){
        miscService.saveMisc(dto);
        return "redirect:/committee/dashboard";
    }
    @PostMapping("/editMisc")
    public String editMisc(@ModelAttribute("misc") MiscDto dto){
        miscService.updateMisc(dto);
        return "redirect:/committee/dashboard";
    }


}
