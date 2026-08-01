package uk.ac.sheffield.team28.team28.controller;


import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.*;
import uk.ac.sheffield.team28.team28.service.*;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = CommitteeController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
public class CommitteeControllerTest {
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

    @MockBean
    MockitoSession session;

    @MockBean
    LoanService loanService;

    @MockBean
    InstrumentRepository instrumentRepository;

    @MockBean
    InstrumentService instrumentService;

    @MockBean
    MusicRepository musicRepository;

    @MockBean
    MusicService musicService;

    @MockBean
    OrderService orderService;

    @MockBean
    ItemService itemService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    MiscService miscService;

    @MockBean
    MiscRepository miscRepository;

    @Autowired
    private HttpSession httpSession;



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
    public void testCommitteeDashboardWithDirector() throws Exception {
        mockMvc.perform(get("/committee/dashboard"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("committee-dashboard"));
    }

    @Test
    public void testCommitteeDashboardWithCommittee() throws Exception {
        memberService.findMember().setMemberType(MemberType.COMMITTEE);
        mockMvc.perform(get("/committee/dashboard"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("committee-dashboard"));
    }


    @Test
    public void testCommitteeDashboardWithAdult() throws Exception {
        memberService.findMember().setMemberType(MemberType.ADULT);
        mockMvc.perform(get("/committee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }


    @Test
    public void testAllowToGoToDirectorWhenNotAuthorised() throws Exception {
        mockMvc.perform(get("/committee/allow-to-go-to-director")
                        .sessionAttr("isAuthorised", false))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authorise"));
    }
    @Test
    public void testAllowToGoToDirectorWhenAuthorisedIsNull() throws Exception {

        mockMvc.perform(get("/committee/allow-to-go-to-director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/authorise"));
    }
    @Test
    public void testAllowToGoToDirectorWhenAuthorised() throws Exception {

        mockMvc.perform(get("/committee/allow-to-go-to-director")
                        .sessionAttr("isAuthorised", true))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/committee/dashboard"));
    }


}

