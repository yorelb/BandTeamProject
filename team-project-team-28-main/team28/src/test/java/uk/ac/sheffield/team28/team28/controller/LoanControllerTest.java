package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
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
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.*;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoanController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
class LoanControllerTest {

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
    private ItemService itemService;

    @MockBean
    private LoanService loanService;

    @MockBean
    private MockHttpSession session;


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
    public void testHandleLoanActionWithLoan() throws Exception {
        mockMvc.perform(post("/loans/loanAction")
                        .param("instrumentId", Long.toString(-1L))
                        .param("action", "loan")
                        .param("memberName", "Test Test"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testHandleLoanActionWithReturn() throws Exception {
        mockMvc.perform(post("/loans/loanAction")
                        .param("instrumentId", Long.toString(-1L))
                        .param("action", "return")
                        .param("memberName", "Test Test"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testHandleLoanActionWithNoAction() throws Exception {
        mockMvc.perform(post("/loans/loanAction")
                        .param("instrumentId", Long.toString(-1L))
                        .param("action", "exit")
                        .param("memberName", "Test Test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testHandleLoanActionShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/loans/loanAction"))
                .andExpect(status().isBadRequest());
    }
}
