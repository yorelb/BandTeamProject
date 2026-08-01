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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;
import uk.ac.sheffield.team28.team28.service.*;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = DashboardController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
public class DashboardControllerTest {
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
    MockHttpSession session;

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
    public void testShowDashboardForAllTrainingMembers() throws Exception {
        memberService.findMember().setBand(BandInPractice.Training);
        mockMvc.perform(get("/dashboard")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("musics"))
                .andExpect(view().name("dashboard"));
    }

    @Test
    public void testShowDashboardForAllSeniorMembers() throws Exception {
        memberService.findMember().setBand(BandInPractice.Senior);
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("musics"))
                .andExpect(view().name("dashboard"));
    }

    @Test
    public void testShowDashboardForAllMembersInABand() throws Exception {
        memberService.findMember().setBand(BandInPractice.Both);
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("musics"))
                .andExpect(view().name("dashboard"));
    }

    @Test
    public void testShowDashboardForNonBandMembers() throws Exception {
      mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    public void testAddInstrument() throws Exception {
        mockMvc.perform(post("/addInstrument")
                .param("instrumentInput", "test")
                .param("make", "test")
                .param("serialNumber", "test")
                .param("inStorage", "false")
                .param("note", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/dashboard"));

    }

    @Test
    public void testEditInstrument() throws Exception {

        mockMvc.perform(post("/editInstrument")
                .param("instrumentInput", "test")
                .param("make", "test")
                .param("serialNumber", "test")
                .param("inStorage", "false")
                .param("note", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/dashboard"));

    }

    @Test
    public void testDeleteInstrument() throws Exception {
        mockMvc.perform(post("/deleteInstrument")
                .param("instrumentId", Long.toString(-1L)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/dashboard"));

    }

    @Test
    public void testGetLoanDetails() throws Exception {
        when(loanService.findLoanById(-1L))
                .thenReturn(Optional.of(new Loan()));
        mockMvc.perform(get("/loanDetails")
                .param("loanId", Long.toString(-1L))
                .param("memberType", MemberType.DIRECTOR.toString()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("dashboard"));

    }

    @Test
    public void testAddMusic() throws Exception {
        mockMvc.perform(post("/addMusic")
                .param("musicInput", "test")
                .param("composer", "test")
                .param("arranger", "test")
                .param("bandInPractice", "None")
                .param("suitableForTraining", "True"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/dashboard"));

    }

    @Test
    public void testOrderMusic() throws Exception {
        when(itemService.findById(-1L))
                .thenReturn(new Item());
        mockMvc.perform(post("/orderMusic")
                .param("itemId", Long.toString(-1L))
                .param("orderDate", "")
                .param("isFulfilled", "")
                .param("note", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

    }

    @Test
    public void testAddMisc() throws Exception {
        mockMvc.perform(post("/addMisc")
                .param("miscName", "test")
                .param("miscMake", "test")
                .param("miscSerialNumber", "test")
                .param("miscQuantity", "test")
                        .param("miscNote", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/dashboard"));

    }

    @Test
    public void testEditMisc() throws Exception {
        mockMvc.perform(post("/editMisc")
                        .param("miscName", "test")
                        .param("miscMake", "test")
                        .param("miscSerialNumber", "test")
                        .param("miscQuantity", "test")
                        .param("miscNote", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/dashboard"));

    }




}