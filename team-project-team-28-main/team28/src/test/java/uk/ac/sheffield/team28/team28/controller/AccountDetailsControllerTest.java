package uk.ac.sheffield.team28.team28.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.model.Request;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.CustomUserDetailsService;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.service.RequestService;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountDetailsController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
public class AccountDetailsControllerTest {
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
    private RequestService requestService;




    @BeforeEach
    public void setup() {
        String encodedPassword = new BCryptPasswordEncoder().encode("Director1");
        User testUser = new User("director@test.com", encodedPassword, Collections.emptyList());
        when(customUserDetailsService.loadUserByUsername("director@test.com")).thenReturn(testUser);

        Member testMember = ((Member) generateParentAndChild(memberRepository, childMemberRepository).get(0));
        testMember.setEmail("director@test.com");
        testMember.setMemberType(MemberType.DIRECTOR);
        testMember.setPassword(encodedPassword);
        when(memberService.findMember()).thenReturn(testMember);
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


    @Test
    public void testAccountInfo() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));

        mockMvc.perform(get("/account-info"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account-info"));
    }


    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsNull() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", memberService.findMember().getEmail())
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", memberService.findMember().getPhone())
                        .param("firstName", memberService.findMember().getFirstName())
                        .param("lastName", memberService.findMember().getLastName())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authorise"));

    }
    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsFalse() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        when(requestService.addRequest(new Request(memberService.findMember(), false, memberService.findMember(), memberService.findMember())))
                .thenReturn(false);
        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", memberService.findMember().getEmail())
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", memberService.findMember().getPhone())
                        .param("firstName", memberService.findMember().getFirstName())
                        .param("lastName", memberService.findMember().getLastName())
                        .sessionAttr("isAuthorised", false)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authorise"));
    }
    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsTrue() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        when(requestService.addRequest(new Request(memberService.findMember(), false, memberService.findMember(), memberService.findMember())))
                .thenReturn(false);


        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", memberService.findMember().getEmail())
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", memberService.findMember().getPhone())
                        .param("firstName", memberService.findMember().getFirstName())
                        .param("lastName", memberService.findMember().getLastName())
                        .sessionAttr("isAuthorised", true)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("account-info"))
                .andExpect(model().attributeExists("hasBeenRequested"));
    }
    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsTrueAndFirstNameIsBlank() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        when(requestService.addRequest(new Request(memberService.findMember(), false, memberService.findMember(), memberService.findMember())))
                .thenReturn(false);


        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", memberService.findMember().getEmail())
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", memberService.findMember().getPhone())
                        .param("firstName", "")
                        .param("lastName", memberService.findMember().getLastName())
                        .sessionAttr("isAuthorised", true)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("errors"))
                .andExpect(view().name("account-info"));

    }
    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsTrueAndLastNameIsBlank() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        when(requestService.addRequest(new Request(memberService.findMember(), false, memberService.findMember(), memberService.findMember())))
                .thenReturn(false);


        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", memberService.findMember().getEmail())
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", memberService.findMember().getPhone())
                        .param("firstName", memberService.findMember().getFirstName())
                        .param("lastName", "")
                        .sessionAttr("isAuthorised", true)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("errors"))
                .andExpect(view().name("account-info"));

    }
    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsTrueAndPhoneIsBlank() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        when(requestService.addRequest(new Request(memberService.findMember(), false, memberService.findMember(), memberService.findMember())))
                .thenReturn(false);


        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", memberService.findMember().getEmail())
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", "")
                        .param("firstName", memberService.findMember().getFirstName())
                        .param("lastName", memberService.findMember().getLastName())
                        .sessionAttr("isAuthorised", true)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("errors"))
                .andExpect(view().name("account-info"));

    }
    @Test
    public void testRequestToUpdateAccountInfoWhenIsAuthorisedIsTrueAndEmailIsBlank() throws Exception {
        when(childMemberService.getChildByParent(memberService.findMember()))
                .thenReturn(List.of(new ChildMember()));
        when(requestService.addRequest(new Request(memberService.findMember(), false, memberService.findMember(), memberService.findMember())))
                .thenReturn(false);


        mockMvc.perform(post("/account/request-update")
                        .param("id", memberService.findMember().getId().toString())
                        .param("email", "")
                        .param("password", memberService.findMember().getPassword())
                        .param("band", memberService.findMember().getBand().toString())
                        .param("phone", memberService.findMember().getPhone())
                        .param("firstName", memberService.findMember().getFirstName())
                        .param("lastName", memberService.findMember().getLastName())
                        .sessionAttr("isAuthorised", true)
                ).andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("errors"))
                .andExpect(view().name("account-info"));

    }


    @Test
    public void testActualUpdateAccountInfoWithNoExceptions() throws Exception {
        when(requestService.getRequestWithId(-1L))
                .thenReturn(new Request());
        when(memberService.updateMemberInfo(
                memberService.findMember().getId(),
                null,
                null,
                null,
                null

        )).thenReturn(Collections.emptyList());
        when(requestService.deleteRequest(new Request()))
                .thenReturn(false);

        mockMvc.perform(post("/account/update/{id}", -1L))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attributeExists("hasBeenAccepted"))
                .andExpect(model().attributeExists("requests"))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void testActualUpdateAccountInfoWithExceptions() throws Exception {
       when(requestService.getRequestWithId(-1L))
               .thenReturn(new Request());
       when(memberService.updateMemberInfo(
                memberService.findMember().getId(),
                null,
                null,
                null,
                null

        )).thenReturn(List.of(new Exception("Test Exception")));
        when(requestService.deleteRequest(new Request()))
                .thenReturn(false);

        mockMvc.perform(post("/account/update/{id}", -1L))
                .andExpect(model().attributeExists("exceptionList"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attributeExists("hasBeenAccepted"))
                .andExpect(model().attributeExists("requests"))
                .andExpect(status().is2xxSuccessful());
    }

}
