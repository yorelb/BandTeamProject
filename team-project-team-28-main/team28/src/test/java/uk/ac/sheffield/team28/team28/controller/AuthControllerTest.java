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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;
import uk.ac.sheffield.team28.team28.dto.MemberLoginDto;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
public class AuthControllerTest {

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
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/auth/login")
                        .param("success", "test")
                        .param("error", "test"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("login"));
    }

    @Test
    public void testPostLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .param("username", "director@test.com")
                        .param("password", "Director1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    public void testLogOut() throws Exception {
        mockMvc.perform(get("/auth/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(model().attributeExists("member"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));
    }

    @Test
    public void testPerformRegisterFormWithNoException() throws Exception {
        MemberRegistrationDto registrationDto = new MemberRegistrationDto();
        registrationDto.setEmail("test@test.com");
        registrationDto.setPassword("password");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("Test");
        when(memberService.registerMember(registrationDto))
                .thenReturn(null);
        mockMvc.perform(post("/auth/register")
                        .param("email", registrationDto.getEmail())
                                .param("password", registrationDto.getPassword())
                                .param("firstName", registrationDto.getFirstName())
                                .param("lastName", registrationDto.getLastName())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/login"));
    }

    @Test
    public void testPerformRegisterFormWithException() throws Exception {
        MemberRegistrationDto registrationDto = new MemberRegistrationDto();
        registrationDto.setEmail("test@test.com");
        registrationDto.setPassword("password");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("Test");
        when(memberService.registerMember(registrationDto))
                .thenThrow(new Exception("Test Exception"));
        mockMvc.perform(post("/auth/register")
                        .param("email", registrationDto.getEmail())
                        .param("password", registrationDto.getPassword())
                        .param("firstName", registrationDto.getFirstName())
                        .param("lastName", registrationDto.getLastName())
                ).andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("register"));
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
