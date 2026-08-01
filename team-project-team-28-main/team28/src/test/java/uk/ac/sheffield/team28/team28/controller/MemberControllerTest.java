package uk.ac.sheffield.team28.team28.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.param;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MemberController.class)
@ContextConfiguration(classes = Team28Application.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    @WithMockUser(username = "u", password = "p", roles = { "USER" })
//    void shouldAddChildSuccessfully() throws Exception {
//        // Arrange
//        Member parent = new Member(1L, "parent@example.com", "password", MemberType.ADULT, "123456789", "ParentFirst", "ParentLast");
//        when(memberService.findMember()).thenReturn(parent);
//
//        // Act & Assert
//        mockMvc.perform(get("/addChild")
//        //mockMvc.perform(post("/addChild")
//                        .param("firstName", "john")
//                        .param("lastName", "doe")
//                        .param("dateOfBirth", "2010-05-20"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/dashboard"));
//    }
//



//    @Test
//    void shouldReturnErrorWhenInvalidDateOfBirth() throws Exception {
//        // Act & Assert
//        mockMvc.perform(post("/addChild")
//                        .param("firstName", "john")
//                        .param("lastName", "doe")
//                        .param("dateOfBirth", "invalid-date"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("error"))
//                .andExpect(model().attributeExists("error"));
//    }
//
//    @Test
//    void shouldReturnErrorWhenParentNotFound() throws Exception {
//        // Arrange
//        when(memberService.findMember()).thenThrow(new RuntimeException("Parent not found"));
//
//        // Act & Assert
//        mockMvc.perform(post("/addChild")
//                        .param("firstName", "john")
//                        .param("lastName", "doe")
//                        .param("dateOfBirth", "2010-05-20"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("error"))
//                .andExpect(model().attributeExists("error"))
//                .andExpect(model().attribute("error", "An error occurred: Parent not found"));
//    }

}
