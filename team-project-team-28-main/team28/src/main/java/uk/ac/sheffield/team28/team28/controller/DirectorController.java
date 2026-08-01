package uk.ac.sheffield.team28.team28.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;

import javax.servlet.http.HttpServletRequest;


import java.util.*;


@Controller
@RequestMapping("/director")
public class DirectorController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ChildMemberService childMemberService;
    private final ChildMemberRepository childMemberRepository;



    @Autowired
    public DirectorController(MemberService memberService, MemberRepository memberRepository, ChildMemberRepository childMemberRepository, ChildMemberService childMemberService) {

        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.childMemberRepository = childMemberRepository;
        this.childMemberService = childMemberService;
    }

    @GetMapping("")
    public String directorHome(Model model) {
        model.addAttribute("message", "This is our director page");
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "directorhome";
    }

    @GetMapping("/allMembers")
    public String allMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        List<ChildMember> children = childMemberService.getAllChildren();
        model.addAttribute("members", members);
        model.addAttribute("children", children);
        return "viewAllMembers";
    }




    @PostMapping("/removeMember/{memberId}")
    public String removeMember(@PathVariable Long memberId, @RequestParam(value = "redirectTo", required = false) String redirectTo,  RedirectAttributes redirectAttributes) {
        try {
            Member member = memberService.findMemberById(memberId);
            boolean hasChildren = !childMemberService.getChildByParent(member).isEmpty();
            if (hasChildren) {
                if (childMemberService.doAnyChildrenHaveLoans(member) == true) {
                    redirectAttributes.addAttribute("error", true);
                    return "redirect:" + (redirectTo != null ? redirectTo : "director/allMembers");
                }
            }
            //Check parent
            boolean cannotDelete = memberService.doesMemberHaveLoans(member);
            //Delete parent
            if (!cannotDelete) {
                if (hasChildren){
                    childMemberService.deleteChildMembers(member);
                }
                memberService.deleteMember(member.getId());
                redirectAttributes.addAttribute("success", true);
            } else {
                redirectAttributes.addAttribute("error", true);
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director/allMembers");
    }


    @PostMapping("/removeChildMember/{childMemberId}")
    public String removeChildMember(@PathVariable Long childMemberId, @RequestParam(value = "redirectTo", required = false) String redirectTo, RedirectAttributes redirectAttributes) {
        try {
            ChildMember childMember = childMemberService.getChildById(childMemberId);
            boolean cannotDelete = childMemberService.doesChildHaveLoans(childMember);
            //Delete parent
            if (!cannotDelete) {
                childMemberService.deleteChildMember(childMember);
                redirectAttributes.addAttribute("success", true);
            } else {
                throw new Exception("Child member has loans");
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director/allMembers");
    }

    @GetMapping("/committee")
    public String showCommitteeMembers(Model model) {
        List<Member> committeeMembers = memberService.getCommitteeMembers();
        List<Member> nonCommitteeMembers = memberService.getAdultMembers();

        model.addAttribute("committeeMembers", committeeMembers);
        model.addAttribute("nonCommitteeMembers", nonCommitteeMembers);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());

        return "dcommittee";
    }


    @GetMapping("/trainingBand")
    public String showTrainingBand(Model model) {
        List<Member> nonBandMembers = memberService.getAllMembersBands();
        List<Member> trainingBandMembers = memberService.getTrainingBandMembers();
        List<ChildMember> nonTrainingBandChildren = childMemberService.getAllChildren();
        List<ChildMember> childTrainingBandMembers = childMemberService.getTrainingBandMembers();
        nonBandMembers.removeAll(trainingBandMembers);
        nonTrainingBandChildren.removeAll(childTrainingBandMembers);
        model.addAttribute("childTrainingBandMembers", childTrainingBandMembers);
        model.addAttribute("nonBandMembers", nonBandMembers);
        model.addAttribute("trainingBandMembers", trainingBandMembers);
        model.addAttribute("nonTrainingBandChildren", nonTrainingBandChildren);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());

        return "trainingBand";
    }

    @GetMapping("/seniorBand")
    public String showSeniorBand(Model model) {
        List<Member> nonBandMembers = memberService.getAllMembersBands();
        List<Member> seniorBandMembers = memberService.getSeniorBandMembers();
        nonBandMembers.removeAll(seniorBandMembers);
        model.addAttribute("nonBandMembers", nonBandMembers);
        model.addAttribute("seniorBandMembers", seniorBandMembers);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "seniorBand";
    }





    @PostMapping("/removeFromBand/{memberId}/{band}")
    public String removeMemberFromBand(@PathVariable Long memberId, @PathVariable BandInPractice band, @RequestParam(value = "redirectTo", required = false) String redirectTo, RedirectAttributes redirectAttributes) {
        try {
            memberService.removeMemberFromBand(memberId, band);
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director" + (band==BandInPractice.Training? "/trainingBand" : "/seniorBand"));
    }

    @PostMapping("/addToBandByEmail")
    public String addMemberToBandByEmail(@RequestParam String email,
                                         @RequestParam BandInPractice band,
                                         @RequestParam(value = "redirectTo", required = false) String redirectTo,
                                         RedirectAttributes redirectAttributes) {
        try {
            if (email.equals("director@test.com")) {
                throw new RuntimeException("Can't add director to band");
            }
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
            memberService.addMemberToBand(member.getId(), band);
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director" + (band==BandInPractice.Training? "/trainingBand" : "/seniorBand"));
    }





    @PostMapping("/addChildToBandByName")
    public String addChildToBandByName(@RequestParam String fullName, @RequestParam(value = "redirectTo", required = false) String redirectTo,RedirectAttributes redirectAttributes) {
        try {
            String [] names = fullName.trim().split(" ");
            String firstName = names[0];
            String lastName = names[names.length - 1];
            String fullFirstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1).toLowerCase();
            String fullLastName = lastName.isEmpty() ? "" : Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1).toLowerCase();
            String newFullName = fullFirstName + " " + fullLastName;

            ChildMember childMember = childMemberService.getChildByFullName(newFullName)
                    .orElseThrow(() -> new RuntimeException("Member not found with name:" + fullName));

            childMemberService.addChildMemberToBand(childMember.getId());
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director/trainingBand");

    }

    @PostMapping("/removeChildFromBand/{childMemberId}")
    public String removeMemberFromBand(@PathVariable Long childMemberId, @RequestParam(value = "redirectTo", required = false) String redirectTo,RedirectAttributes redirectAttributes) {
        try {
            childMemberService.removeChildMemberFromBand(childMemberId);
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director/trainingBand");
    }

    @PostMapping("/addToCommitteeByEmail")
    public String addMemberToCommitteeByEmail(@RequestParam String email, @RequestParam(value = "redirectTo", required = false) String redirectTo, RedirectAttributes redirectAttributes) {
        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));

            memberService.promoteMemberWithId(member.getId());
            redirectAttributes.addAttribute("success", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director/committee");

    }


    @GetMapping("/parents")
    public String showParents(Model model) {
        Map<Member, List<ChildMember>> parentsWithChildren = childMemberService.getParentsWithChildren();
        model.addAttribute("parentsWithChildren", parentsWithChildren);
        model.addAttribute("memberType", memberService.findMember().getMemberType().toString());
        return "parents";
    }


    @PostMapping("/committee/{id}")
    public String removeMemberFromCommittee(@PathVariable Long id, @RequestParam(value = "redirectTo", required = false) String redirectTo, RedirectAttributes redirectAttributes) {
        try {
            Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
            memberService.demoteMemberWithId(member.getId());
            redirectAttributes.addAttribute("successR", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorR", true);
        }
        return "redirect:" + (redirectTo != null ? redirectTo : "/director/committee");

    }


}
