package uk.ac.sheffield.team28.team28.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.model.Request;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.service.RequestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/request")
public class RequestController {

    private final RequestService requestService;
    private final MemberService memberService;

    public RequestController(RequestService requestService, MemberService memberService) {
        this.requestService = requestService;
        this.memberService = memberService;
    }

    @GetMapping("/show")
    public String showApprovedRequests(Model model) {
        if (memberService.findMember().getMemberType()!=MemberType.DIRECTOR) {
            List<Request> requests = requestService.getAllApprovedRequestWhereRequesterIs(memberService.findMember());
            model.addAttribute("requests", requests);
            return "approved-changes";
        } else {
            List<Request> requests = requestService.getAllToBeApprovedRequests();
            model.addAttribute("requests", requests);
            return "changes-to-be-approved";
        }

    }

    @PostMapping("/approve/{id}")
    public String fulfilRequest(@PathVariable Long id, Model model) {
        Request requestToBeApproved = requestService.getRequestWithId(id);
        if (requestToBeApproved == null) {
            model.addAttribute("error", "Request with id " + id + " does not exist");
            return "redirect:/request/show";
        } else {
            boolean approvedFlag = requestService.approveRequest(requestToBeApproved);
            if (!approvedFlag) {
                model.addAttribute("error", "Request with id " + id + " contains input errors");
                return "redirect:/request/show";
            }
            model.addAttribute("approvedFlag", true);
        }
        model.addAttribute("requests", requestService.getAllToBeApprovedRequests());
        return "changes-to-be-approved";
    }

    @PostMapping("/reject/{id}")
    public String rejectRequest(@PathVariable Long id, Model model) {
        boolean deletedFlag = requestService.deleteRequest(requestService.getRequestWithId(id));
        if (!deletedFlag) {
            model.addAttribute("error", "Request with id " + id + " does not exist");
            return "redirect:/request/show";
        }
        model.addAttribute("deletedFlag", true);
        model.addAttribute("requests", requestService.getAllToBeApprovedRequests());
        return "changes-to-be-approved";
    }

    @PostMapping("/discard/{id}")
    public String deleteRequest(@PathVariable Long id, Model model) {
        boolean deletedFlag = requestService.deleteRequest(requestService.getRequestWithId(id));
        if (!deletedFlag) {
            model.addAttribute("error", "Request with id " + id + " does not exist");
            return "redirect:/request/show";
        }
        model.addAttribute("deletedFlag", true);
        model.addAttribute("requests", requestService.getAllApprovedRequestWhereRequesterIs(memberService.findMember()));
        return "approved-changes";
    }


}
