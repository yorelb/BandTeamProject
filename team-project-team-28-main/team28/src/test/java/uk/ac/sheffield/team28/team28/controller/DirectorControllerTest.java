package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.CustomUserDetailsService;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DirectorController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Model model;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ChildMemberService childMemberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private ChildMemberRepository childMemberRepository;

    @Autowired
    private HttpSession httpSession;
    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;


    @BeforeEach
    public void setup() {
        String encodedPassword = new BCryptPasswordEncoder().encode("Director1");
        User testUser = new User("director@test.com", encodedPassword, Collections.emptyList());
        when(customUserDetailsService.loadUserByUsername("director@test.com")).thenReturn(testUser);

        Member testMember = new Member();
        testMember.setEmail("director@test.com");
        testMember.setMemberType(MemberType.DIRECTOR);
        when(memberService.findMember()).thenReturn(testMember);
    }

    @Test
    public void testDirectorHome() throws Exception {

        mockMvc.perform(get("/director")
                .requestAttr("model", model)
                        .sessionAttr("session", httpSession))
                .andExpect(view().name("directorhome"))
                .andDo(print());
    }


    @Test
    public void testRemoveMemberWithNonExistentMember() throws Exception {

        when(memberService.findMemberById(30L)).thenThrow(new Exception("We dont want to delete yet"));

        mockMvc.perform(post("/director/removeMember/{tesId}", 30L)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testRemoveMemberWithNonExistentMemberWithNoRedirectToAttribute() throws Exception {

        when(memberService.findMemberById(30L)).thenThrow(new Exception("We dont want to delete yet"));

        mockMvc.perform(post("/director/removeMember/{tesId}", 30L))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberWithMemberWithNoLoans() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.findMemberById(idNotUsed))
                .thenReturn(new Member(
                        idNotUsed,
                        "toBeRemoved@email.com",
                        new BCryptPasswordEncoder().encode("toBeRemoved"),
                        null,
                        "toBeRemoved",
                        "toBeRemoved", "toBeRemoved"

                ));

        mockMvc.perform(post("/director/removeMember/{tesId}", idNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?success=true"));
    }
    @Test
    public void testRemoveMemberWithMemberWithNoLoansWithNoRedirectToAttribute() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.findMemberById(idNotUsed))
                .thenReturn(new Member(
                        idNotUsed,
                        "toBeRemoved@email.com",
                        new BCryptPasswordEncoder().encode("toBeRemoved"),
                        null,
                        "toBeRemoved",
                        "toBeRemoved", "toBeRemoved"

                ));

        mockMvc.perform(post("/director/removeMember/{tesId}", idNotUsed))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberWithMemberWithLoans() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        Member testToBeRemovedMember = new Member(
                idNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );
        when(memberService.findMemberById(idNotUsed))
                .thenReturn(testToBeRemovedMember);
        when(memberService.doesMemberHaveLoans(testToBeRemovedMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeMember/{tesId}", idNotUsed)
                        .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testRemoveMemberWithMemberWithLoansWithNoRedirectToAttribute() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        Member testToBeRemovedMember = new Member(
                idNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );
        when(memberService.findMemberById(idNotUsed))
                .thenReturn(testToBeRemovedMember);
        when(memberService.doesMemberHaveLoans(testToBeRemovedMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeMember/{tesId}", idNotUsed))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberWithParentAndOneChildThatBothHaveNoLoans() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(false);

        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?success=true"));
    }
    @Test
    public void testRemoveMemberWithParentAndOneChildThatBothHaveNoLoansWithNoRedirectToAttribute() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(false);

        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberWithParentThatHasLoansAndOneChildThatHasNoLoans() throws Exception {

        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(false);

        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testRemoveMemberWithParentThatHasLoansAndOneChildThatHasNoLoansWithNoRedirectToAttribute() throws Exception {

        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(false);

        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberWithParentThatHasNoLoansAndOneChildThatHasLoans() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(true);

        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testRemoveMemberWithParentThatHasNoLoansAndOneChildThatHasLoansWithNoRedirectToAttribute() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        
        
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(true);

        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberWithParentThatHasLoansAndOneChildThatHasLoans() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();


        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);

        
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(true);

        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testRemoveMemberWithParentThatHasLoansAndOneChildThatHasLoansWithNoRedirectToAttribute() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        Member testParentMember = ((Member) parentAndChild.get(0));
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long parentIdNotUsed = testParentMember.getId();

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(true);

        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    public void testAllMembers() throws Exception {
        mockMvc.perform(get("/director/allMembers"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewAllMembers"));
    }


    @Test
    public void testRemoveChildMemberWithChildThatHasNoLoans() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        long childIdNotUsed = testChildMember.getId();

        when(childMemberService.getChildById(childIdNotUsed)).thenReturn(testChildMember);
        when(childMemberService.doesChildHaveLoans(testChildMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeChildMember/{c}", childIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?success=true"));
    }
    @Test
    public void testRemoveChildMemberWithChildThatHasNoLoansWithNoRedirectToAttribute() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        Long childIdNotUsed = testChildMember.getId();

        when(childMemberService.getChildById(childIdNotUsed)).thenReturn(testChildMember);
        when(childMemberService.doesChildHaveLoans(testChildMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeChildMember/{c}", childIdNotUsed))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/allMembers?success=true"));
    }
    @Test
    public void testRemoveChildMemberWithChildThatHasLoans() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        Long childIdNotUsed = testChildMember.getId();

        when(childMemberService.getChildById(childIdNotUsed)).thenReturn(testChildMember);
        when(childMemberService.doesChildHaveLoans(testChildMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeChildMember/{c}", childIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testRemoveChildMemberWithChildThatHasLoansWithNoRedirectToAttribute() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        Long childIdNotUsed = testChildMember.getId();

        when(childMemberService.getChildById(childIdNotUsed)).thenReturn(testChildMember);
        when(childMemberService.doesChildHaveLoans(testChildMember)).thenReturn(true);

        mockMvc.perform(post("/director/removeChildMember/{c}", childIdNotUsed))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/allMembers?error=true"));
    }


    @Test
    public void testShowCommitteeMembers() throws Exception {
        mockMvc.perform(get("/director/committee"))
                .andExpect(status().isOk())
                .andExpect(view().name("dcommittee"));
    }


    @Test
    public void testTrainingBand() throws Exception {
        mockMvc.perform(get("/director/trainingBand"))
                .andExpect(status().isOk())
                .andExpect(view().name("trainingBand"));
    }


    @Test
    public void testSeniorBand() throws Exception {
        mockMvc.perform(get("/director/seniorBand"))
                .andExpect(status().isOk())
                .andExpect(view().name("seniorBand"));
    }


    @Test
    public void testRemoveMemberFromBandWhereBandIsSenior() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Senior)).thenReturn(null);
        mockMvc.perform(post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Senior)
                .param("redirectTo", "/director/seniorBand"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/seniorBand?success=true"));
    }
    @Test
    public void testRemoveMemberFromBandWhereBandIsSeniorWithNoRedirectToAttribute() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Senior)).thenReturn(null);
        mockMvc.perform(
                post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Senior))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveMemberFromBandWhereBandIsTraining() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Training)).thenReturn(null);
        mockMvc.perform(post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Training)
                .param("redirectTo", "/director/trainingBand"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/trainingBand?success=true"));
    }
    @Test
    public void testRemoveMemberFromBandWhereBandIsTrainingWithNoRedirectToAttribute() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Training)).thenReturn(null);
        mockMvc.perform(post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Training))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveFromBandWhereExceptionIsThrownWithNoRedirectToAttribute() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Training)).thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Training))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testRemoveFromBandWhereExceptionIsThrown() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Training)).thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Training)
                        .param("redirectTo", "/director/trainingBand"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/trainingBand?error=true"));

        when(memberService.removeMemberFromBand(idNotUsed, BandInPractice.Senior)).thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/removeFromBand/{memberId}/{newBand}", idNotUsed, BandInPractice.Senior)
                        .param("redirectTo", "/director/seniorBand"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/seniorBand?error=true"));
    }


    @Test
    public void testAddMemberToBandByEmailWithRedirectToAttributeAndNoException() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        Member testMember = new Member(
                idNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );

        when(memberRepository.findByEmail("test"))
                .thenReturn(Optional.of(testMember));
        when(memberService.addMemberToBand(idNotUsed, BandInPractice.Training))
                .thenReturn(null);

        mockMvc.perform(post("/director/addToBandByEmail")
                .param("email", "test")
                .param("band", "Training")
                .param("redirectTo", "/director/trainingBand"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/trainingBand?success=true"));
    }
    @Test
    public void testAddMemberToBandByEmailWithRedirectToAttributeAndException1() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        Member testMember = new Member(
                idNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );

        when(memberRepository.findByEmail("test"))
                .thenReturn(Optional.of(testMember));
        when(memberService.addMemberToBand(idNotUsed, BandInPractice.Training))
                .thenThrow(new Exception("Test Exception"));

        mockMvc.perform(post("/director/addToBandByEmail")
                .param("email", "test")
                .param("band", "Training")
                .param("redirectTo", "/director/trainingBand"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/trainingBand?error=true"));

    }
    @Test
    public void testAddMemberToBandByEmailWithNoRedirectToAttributeAndNoException() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        Member testMember = new Member(
                idNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );

        when(memberRepository.findByEmail("test"))
                .thenReturn(Optional.of(testMember));
        when(memberService.addMemberToBand(idNotUsed, BandInPractice.Training))
                .thenReturn(null);

        mockMvc.perform(post("/director/addToBandByEmail")
                .param("email", "test")
                .param("band", "Training"))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    public void testAddMemberToBandByEmailWithNoRedirectToAttributeAndException1() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        Member testMember = new Member(
                idNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );

        when(memberRepository.findByEmail("test"))
                .thenReturn(Optional.of(testMember));
        when(memberService.addMemberToBand(idNotUsed, BandInPractice.Training))
                .thenThrow(new Exception("Test Exception"));

        mockMvc.perform(post("/director/addToBandByEmail")
                        .param("email", "test")
                        .param("band", "Training"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testAddChildToBandByNameWithNoException() throws Exception {
        List<Object> parentAndChild = generateParentAndChild(memberRepository, childMemberRepository);
        ChildMember testChildMember = ((ChildMember) parentAndChild.get(1));
        testChildMember.setFirstName("Test");
        testChildMember.setLastName("Test");

        when(childMemberRepository.findByName("Test Test"))
                .thenReturn(Optional.of(testChildMember));
        when(childMemberService.addChildMemberToBand(testChildMember.getId()))
                .thenReturn(null);
        mockMvc.perform(post("/director/addChildToBandByName")
                        .param("fullName", "Test Test"))
                .andExpect(status().isOk());

    }
    @Test
    public void testAddChildToBandByNameWithException() throws Exception {
        when(childMemberRepository.findByName("Test"))
                .thenReturn(Optional.of(new ChildMember()));
        mockMvc.perform(post("/director/addChildToBandByName")
                .param("fullName", "Test Test"))
                .andExpect(status().isNotFound());

    }


    @Test
    public void testRemoveMemberFromBandForExistingChild() throws Exception {
        long childMemberId = ((ChildMember) generateParentAndChild(memberRepository, childMemberRepository).get(1)).getId();
        when(childMemberService.removeChildMemberFromBand(childMemberId))
                .thenReturn(null);
        mockMvc.perform(post("/director/removeChildFromBand/{childMemberId}", childMemberId))
                .andExpect(status().isOk());
    }
    @Test
    public void testRemoveMemberFromBandForNonExistingChild() throws Exception {
        long childMemberId = ((ChildMember) generateParentAndChild(memberRepository, childMemberRepository).get(1)).getId();
        when(childMemberService.removeChildMemberFromBand(childMemberId))
                .thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/removeChildFromBand/{childMemberId}", childMemberId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddMemberToCommitteeByEmailWithRedirectToAttributeAndNoException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findByEmail(testMember.getEmail()))
                .thenReturn(Optional.of(testMember));
        when(memberService.promoteMemberWithId(testMember.getId()))
                .thenReturn(null);
        mockMvc.perform(post("/director/addToCommitteeByEmail")
                .param("email", testMember.getEmail())
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?success=true"));
    }
    @Test
    public void testAddMemberToCommitteeByEmailWithNoRedirectToAttributeAndNoException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findByEmail(testMember.getEmail()))
                .thenReturn(Optional.of(testMember));
        when(memberService.promoteMemberWithId(testMember.getId()))
                .thenReturn(null);
        mockMvc.perform(post("/director/addToCommitteeByEmail")
                .param("email", testMember.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/committee?success=true"));
    }
    @Test
    public void testAddMemberToCommitteeByEmailWithRedirectToAttributeAndException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findByEmail(testMember.getEmail()))
                .thenReturn(Optional.of(testMember));
        when(memberService.promoteMemberWithId(testMember.getId()))
                .thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/addToCommitteeByEmail")
                .param("email", testMember.getEmail())
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }
    @Test
    public void testAddMemberToCommitteeByEmailWithNoRedirectToAttributeAndException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findByEmail(testMember.getEmail()))
                .thenReturn(Optional.of(testMember));
        when(memberService.promoteMemberWithId(testMember.getId()))
                .thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/addToCommitteeByEmail")
                .param("email", testMember.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/committee?error=true"));
    }


    @Test
    public void testShowParents() throws Exception {
        when(childMemberService.getParentsWithChildren())
                .thenReturn(Map.of());
        mockMvc.perform(get("/director/parents"))
                .andExpect(status().isOk())
                .andExpect(view().name("parents"));
    }


    @Test
    public void testRemoveMemberFromCommitteeWithRedirectToAttributeAndNoException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findById(testMember.getId()))
                .thenReturn(Optional.of(testMember));
        when(memberService.demoteMemberWithId(testMember.getId()))
                .thenReturn(null);
        mockMvc.perform(post("/director/committee/{id}", testMember.getId())
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?successR=true"));
    }
    @Test
    public void testRemoveMemberFromCommitteeWithNoRedirectToAttributeAndNoException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findById(testMember.getId()))
                .thenReturn(Optional.of(testMember));
        when(memberService.demoteMemberWithId(testMember.getId()))
                .thenReturn(null);
        mockMvc.perform(post("/director/committee/{id}", testMember.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/committee?successR=true"));
    }
    @Test
    public void testRemoveMemberFromCommitteeWithRedirectToAttributeAndException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findById(testMember.getId()))
                .thenReturn(Optional.of(testMember));
        when(memberService.demoteMemberWithId(testMember.getId()))
                .thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/committee/{id}", testMember.getId())
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?errorR=true"));
    }
    @Test
    public void testRemoveMemberFromCommitteeWithNoRedirectToAttributeAndException() throws Exception {
        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        when(memberRepository.findById(testMember.getId()))
                .thenReturn(Optional.of(testMember));
        when(memberService.demoteMemberWithId(testMember.getId()))
                .thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/director/committee/{id}", testMember.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director/committee?errorR=true"));
    }



    private static List<Object> generateParentAndChild(MemberRepository memberRepository, ChildMemberRepository childMemberRepository) {
        long parentIdNotUsed = 0;
        while (true) {
            if (memberRepository.findById(parentIdNotUsed).isPresent()) {
                parentIdNotUsed++;
            } else {
                break;
            }
        }
        Member testParentMember = new Member(
                parentIdNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );

        Long childIdNotUsed = 0L;
        while (true) {
            if (childMemberRepository.findById(childIdNotUsed).isPresent()) {
                childIdNotUsed++;
            } else {
                break;
            }
        }
        ChildMember testChildMember = new ChildMember(
                childIdNotUsed,
                "toBeRemoved",
                "toBeRemoved",
                testParentMember,
                LocalDate.of(LocalDate.now().getYear()-10, Month.AUGUST, 12)
        );
        return List.of(testParentMember, testChildMember);
    }
}
