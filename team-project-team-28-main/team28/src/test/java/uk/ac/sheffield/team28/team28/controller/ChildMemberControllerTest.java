package uk.ac.sheffield.team28.team28.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(ChildMemberController.class)
@ContextConfiguration(classes = Team28Application.class)
public class ChildMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildMemberService childMemberService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MockHttpSession session;
    @Mock
    private Model model;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "u", password = "p", roles = { "USER" })
    void shouldReturnChildDashboard() throws Exception {
        Long childMemberId = 123L;
        mockMvc.perform(get("/child/dashboard/{childMemberId}", childMemberId))
                .andExpect(status().isOk())
                .andExpect(view().name("childDashboard"))
                .andExpect(model().attributeExists("childMemberId"))
                .andExpect(model().attribute("childMemberId", childMemberId));
    }


    @Test
    @WithMockUser(username = "u", password = "p", roles = { "USER" })
    void shouldRedirectToAuthoriseWhenIsAuthorisedIsNull() throws Exception {
        mockMvc.perform(get("/child/allow-to-go-parent").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authorise"));
        session.setAttribute("referer", "/dashboard");
    }

//    @Test
//    @WithMockUser(username = "u", password = "p", roles = { "USER" })
//    void shouldRedirectToAuthoriseWhenIsAuthorisedIsFalse() throws Exception {
//        session.setAttribute("isAuthorised", false);
//
//        mockMvc.perform(get("/child/allow-to-go-parent").session(session))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/authorise"));
//
//        // Verify that "referer" is set
//        session.setAttribute("referer", "/dashboard");
//    }
//
//    @Test
//    @WithMockUser(username = "u", password = "p", roles = { "USER" })
//    void shouldRedirectToChildDashboardWhenIsAuthorisedIsTrue() throws Exception {
//        //session.setAttribute("isAuthorised", true);
////        Long childMemberId = 123L;
//        System.out.println("IM HHHHHHHHHHHHHEEEEEEEEEEEEEEEEEEEEERE" + session.getAttribute("isAuthorised"));
//
//        when(session.getAttribute("isAuthorised")).thenReturn(true);
//        when(model.getAttribute("childMemberId")).thenReturn(123L);
//        System.out.println("IM HHHHHHHHHHHHHEEEEEEEEEEEEEEEEEEEEERE" + session.getAttribute("isAuthorised"));
//
//        mockMvc.perform(get("/child/allow-to-go-parent").session(session))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/child/dashboard/{childMemberId}"));
//    }
//
//    @Test
//    @WithMockUser(username = "u", password = "p", roles = { "USER" })
//    void shouldRedirectToChildDashboardWhenIsAuthorisedIsTrue2() throws Exception {
//        MockHttpSession session = new MockHttpSession(); // Create a new session instance
//        session.setAttribute("isAuthorised", true);      // Set the isAuthorised attribute
//        session.setAttribute("childMemberId", 123L);    // Set the childMemberId attribute
//
//        mockMvc.perform(get("/child/allow-to-go-parent").session(session))
//                .andExpect(status().is3xxRedirection()) // Expect a redirection
//                .andExpect(redirectedUrl("/child/dashboard/{childMemberId}")); // Expect the resolved URL
//    }


    @Test
    void shouldRedirectToAuthoriseWhenSessionAttributeIsFalse() throws Exception {
        session.setAttribute("isAuthorised", false);
    }
}
