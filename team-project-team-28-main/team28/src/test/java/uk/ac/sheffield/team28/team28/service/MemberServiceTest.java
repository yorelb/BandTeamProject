package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.MemberRegistrationDto;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Service
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ChildMemberRepository childMemberRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;



    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z]).{8,}$");


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllMembers_whenGetAllMembersIsCalled() {
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Member member2 = new Member(30L, "a@y.com", "password", MemberType.ADULT, "090376734", "Johnny", "Deoe");
        Member member3 = new Member(31L, "a@x.com", "password", MemberType.ADULT, "090375734", "Jon", "Doey");
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2, member3));

        List<Member> members = memberService.getAllMembers();
        assertEquals(3, members.size());
        assertEquals("John", members.get(0).getFirstName());
        assertEquals("Johnny", members.get(1).getFirstName());
        assertEquals("Jon", members.get(2).getFirstName());

    }

    @Test
    public void shouldPromoteMember_whenPromoteMemberWithIdCalled() {
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Long memberId = 29L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
        try {
            Member member = memberService.promoteMemberWithId(memberId);
            assertEquals(MemberType.COMMITTEE, member.getMemberType());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void shouldPromoteAdultMemberToCommittee() throws Exception {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);

        Member updatedMember = memberService.promoteMemberWithId(memberId);

        assertEquals(MemberType.COMMITTEE, updatedMember.getMemberType());
        verify(memberRepository).save(member);
    }

    @Test
    public void shouldThrowExceptionWhenPromotingNonAdultMember() {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.COMMITTEE, "090378734", "John", "Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Exception exception = assertThrows(Exception.class, () -> {
            memberService.promoteMemberWithId(memberId);
        });

        assertEquals("Member cannot be added.", exception.getMessage());
        verify(memberRepository, never()).save(member);
    }

    @Test
    public void shouldThrowExceptionWhenMemberNotFoundForPromotion() {
        Long memberId = 29L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            memberService.promoteMemberWithId(memberId);
        });

        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }


    @Test
    public void shouldDemoteCommitteeMemberToAdult() throws Exception {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.COMMITTEE, "090378734", "John", "Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);

        Member updatedMember = memberService.demoteMemberWithId(memberId);

        assertEquals(MemberType.ADULT, updatedMember.getMemberType());
        verify(memberRepository).save(member);
    }

    @Test
    public void shouldThrowExceptionWhenDemotingNonCommitteeMember() {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Exception exception = assertThrows(Exception.class, () -> {
            memberService.demoteMemberWithId(memberId);
        });

        assertEquals("Member cannot be demoted.", exception.getMessage());
        verify(memberRepository, never()).save(member);
    }

    @Test
    public void shouldThrowExceptionWhenMemberNotFoundForDemotion() {
        Long memberId = 29L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            memberService.demoteMemberWithId(memberId);
        });

        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }


    @Test
    public void shouldAddMemberToNewBand_whenMemberIsInNoneBand() throws Exception {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        member.setBand(BandInPractice.None);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);

        Member updatedMember = memberService.addMemberToBand(memberId, BandInPractice.Training);

        assertEquals(BandInPractice.Training, updatedMember.getBand());
    }

    @Test
    public void shouldUpdateBandToBoth_whenMemberIsAlreadyInOneBand() throws Exception {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        member.setBand(BandInPractice.Training);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);

        Member updatedMember = memberService.addMemberToBand(memberId, BandInPractice.Senior);

        assertEquals(BandInPractice.Both, updatedMember.getBand());
    }

    @Test
    public void shouldThrowException_whenAddingMemberToSameBand() {
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        member.setBand(BandInPractice.Training);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Exception exception = assertThrows(Exception.class, () -> {
            memberService.addMemberToBand(memberId, BandInPractice.Training);
        });

        assertEquals("Member is already in this band.", exception.getMessage());
    }

    @Test
    public void testAddMemberToBand_WhenMemberInBothBands() {
        //Make the member
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Set band to both
        member.setBand(BandInPractice.Both);
        //Mock the repo response
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        //Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.addMemberToBand(memberId, BandInPractice.Training);
        });
        assertEquals("Member is already in both bands.", exception.getMessage());
    }

    @Test
    public void testAddMemberToBand_WhenMemberIsntFound() {
        //Make the member
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Set band to both
        member.setBand(BandInPractice.Both);
        //Mock the repo response
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        //Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.addMemberToBand(memberId,BandInPractice.Senior);
        });
        //Compare the exceptions
        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    public void testRemoveMemberFromBand_whenMemberNotFound() {

        Long memberId = 29L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            memberService.removeMemberFromBand(memberId, BandInPractice.Training);
        });
        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    public void testRemoveMemberFromBand_whenMemberIsInSpecifiedBand() throws Exception {
        //Make member
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Set the member band
        member.setBand(BandInPractice.Training);
        //Mock repo responses
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        //Remove member from training band
        Member updatedMember = memberService.removeMemberFromBand(memberId, BandInPractice.Training);
        assertEquals(BandInPractice.None, updatedMember.getBand());
    }




    @Test
    public void testRemoveMemberFromBand_whenMemberIsInBothBandsButRemovedFromTraining() throws Exception {
        //Make member
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Put member in both bands
        member.setBand(BandInPractice.Both);
        //Mock member repo responses
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        //Remove member from training band
        Member updatedMember = memberService.removeMemberFromBand(memberId, BandInPractice.Training);
        assertEquals(BandInPractice.Senior, updatedMember.getBand()); //Check bands
    }

    @Test
    public void testRemoveMemberFromBand_whenMemberIsInBothBandsButRemovedFromSenior() throws Exception {
        //Make member
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Put member in both bands
        member.setBand(BandInPractice.Both);
        //Mock member repo responses
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        //Remove member from training band
        Member updatedMember = memberService.removeMemberFromBand(memberId, BandInPractice.Senior);
        assertEquals(BandInPractice.Training, updatedMember.getBand()); //Check bands
    }

    @Test
    public void testRemoveMemberFromBand_whenMemberIsInNoBand() throws Exception {
        //Make member
        Long memberId = 29L;
        Member member = new Member(memberId, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Put member in no bands
        member.setBand(BandInPractice.None);
        //Mock member repo responses
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.removeMemberFromBand(memberId,BandInPractice.Senior);
        });
        //Remove member from no band
        assertEquals("Member is already not assigned to any band.", exception.getMessage());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void testGetAdultMembers() {
        //Create Adults
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Member member2 = new Member(30L, "a@y.com", "password", MemberType.ADULT, "090376734", "Jane", "Doe");
        //Mock repo response
        when(memberRepository.findByMemberType(MemberType.ADULT)).thenReturn(Arrays.asList(member1, member2));
        //Get the adult members
        List<Member> adultMembers = memberService.getAdultMembers();
        //Compare the list to the adults
        assertEquals(2, adultMembers.size());
        assertEquals("John", adultMembers.get(0).getFirstName());
        assertEquals("Jane", adultMembers.get(1).getFirstName());
    }



    @Test
    void testGetCommitteeMembers() {
        //Create adults
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.COMMITTEE, "090378734", "John", "Doe");
        Member member2 = new Member(30L, "a@y.com", "password", MemberType.COMMITTEE, "090376734", "Jane", "Doe");
        //Mock repo response
        when(memberRepository.findByMemberType(MemberType.COMMITTEE)).thenReturn(Arrays.asList(member1, member2));
        //Get the committee members
        List<Member> committeeMembers = memberService.getCommitteeMembers();
        //Compare committee to the list elements
        assertEquals(2, committeeMembers.size());
        assertEquals("John", committeeMembers.get(0).getFirstName());
        assertEquals("Jane", committeeMembers.get(1).getFirstName());
    }

    @Test
    void testGetSeniorBandMembers() {
        //Create the adults
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Member member2 = new Member(30L, "a@y.com", "password", MemberType.ADULT, "090376734", "Jane", "Doe");
        //Set the bands
        member1.setBand(BandInPractice.Both);
        member2.setBand(BandInPractice.Senior);
        //Mock the repo response
        when(memberRepository.findByBand(BandInPractice.Senior)).thenReturn(Arrays.asList(member1, member2));
        //Get the training band members
        List<Member> seniorBandMembers = memberService.getSeniorBandMembers();
        //Compare elements
        assertEquals(2, seniorBandMembers.size());
        assertEquals("John", seniorBandMembers.get(0).getFirstName());
        assertEquals("Jane", seniorBandMembers.get(1).getFirstName());

    }

    @Test
    void testGetTrainingBandMembers() {
        //Create the adults
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Member member2 = new Member(30L, "a@y.com", "password", MemberType.ADULT, "090376734", "Jane", "Doe");
        //Set the bands
        member1.setBand(BandInPractice.Both);
        member2.setBand(BandInPractice.Training);
        //Mock the repo response
        when(memberRepository.findByBand(BandInPractice.Training)).thenReturn(Arrays.asList(member1, member2));
        //Get the training band members
        List<Member> trainingBandMembers = memberService.getTrainingBandMembers();
        //Compare elements
        assertEquals(2, trainingBandMembers.size());
        assertEquals("John", trainingBandMembers.get(0).getFirstName());
        assertEquals("Jane", trainingBandMembers.get(1).getFirstName());

    }

    @Test
    void testGetAllMembersBands() {
        //Create the adults
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        Member member2 = new Member(30L, "a@y.com", "password", MemberType.ADULT, "090376734", "Jane", "Doe");
        Member member3 = new Member(31L, "a@x.com", "password", MemberType.ADULT, "090376731", "Bruce", "Wayne");
        Member member4 = new Member(32L, "a@v.com", "password", MemberType.ADULT, "090375731", "Clark", "Kent");

        //Set the bands
        member1.setBand(BandInPractice.Both);
        member2.setBand(BandInPractice.Training);
        member3.setBand(BandInPractice.Senior);
        member4.setBand(BandInPractice.None);
        //Mock the repo response
        when(memberRepository.findByBand(BandInPractice.Training)).thenReturn(Arrays.asList(member1, member2, member3, member4));
        //Get the training band members
        List<Member> allMembersBands = memberService.getAllMembersBands();
        //Compare elements
        assertEquals(4, allMembersBands.size());
        assertEquals("John", allMembersBands.get(0).getFirstName());
        assertEquals("Jane", allMembersBands.get(1).getFirstName());
        assertEquals("Bruce", allMembersBands.get(2).getFirstName());
        assertEquals("Clark", allMembersBands.get(3).getFirstName());
    }

    @Test
    public void shouldAuthoriseMember_whenCredentialsMatch() throws Exception {
        Long memberId = 29L;
        String firstPassword = "password";
        Member member = new Member(memberId, "a@z.com", "$2a$10$hashedpassword", MemberType.ADULT, "090378734", "John", "Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        //String passwordEncoder;
        when(passwordEncoder.matches(firstPassword, member.getPassword())).thenReturn(true);

        boolean isAuthorised = memberService.authorise(memberId, firstPassword);

        assertTrue(isAuthorised);
    }

    @Test
    public void shouldNotAuthoriseMember_whenCredentialsDoNotMatch() throws Exception {
        Long memberId = 29L;
        String firstPassword = "wrongPassword";
        Member member = new Member(memberId, "a@z.com", "$2a$10$hashedpassword", MemberType.ADULT, "090378734", "John", "Doe");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(firstPassword, member.getPassword())).thenReturn(false);

        boolean isAuthorised = memberService.authorise(memberId, firstPassword);

        assertFalse(isAuthorised);
    }


    @Test
    public void testAuthorise_whenMemberNotFound() {
        //Make member
        Long memberId = 29L;
        String firstPassword = "password";
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.authorise(memberId, firstPassword);
        });
        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testFindMemberById_MemberExists() throws Exception {
        //Make member
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        // Make the repo response
        when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));
        //Get the member by id
        Member result = memberService.findMemberById(member1.getId());
        //Check the result
        assertNotNull(result);
        assertEquals(member1.getId(), result.getId());
        verify(memberRepository, times(1)).findById(member1.getId());
    }

    @Test
    void testFindMemberById_MemberDoesntExist() {
        Long memberId = 209L;
        //Mock the repo response
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        //Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.findMemberById(memberId);
        });
        //Compare the exception
        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void testDoesMemberHaveLoans_MemberHasLoans() {
        //Make member
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Mock the repo response
        when(loanRepository.memberHasActiveLoans(member1.getId())).thenReturn(true);
        //Get the result
        boolean result = memberService.doesMemberHaveLoans(member1);
        // Validate it
        assertTrue(result);
        verify(loanRepository, times(1)).memberHasActiveLoans(member1.getId());
    }

    @Test
    void testDoesMemberHaveLoans_MemberHasNoLoans() {
        //Make member
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Mock the repo response
        when(loanRepository.memberHasActiveLoans(member1.getId())).thenReturn(false);
        //Get the result
        boolean result = memberService.doesMemberHaveLoans(member1);
        // Validate it
        assertFalse(result);
        verify(loanRepository, times(1)).memberHasActiveLoans(member1.getId());
    }

    @Test
    void testDeleteMember_MemberFound() throws Exception {
        //Make member
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        // Mock the repo response
        when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));
        // Delete member
        memberService.deleteMember(member1.getId());
        // Verify other stuff are deleted
        verify(loanRepository, times(1)).deleteLoansByMemberId(member1.getId());
        verify(orderRepository, times(1)).deleteOrdersByMemberId(member1.getId());
        verify(memberRepository, times(1)).deleteById(member1.getId());
    }

    @Test
    void testDeleteMember_MemberNotFound() {
        Long memberId = 29L;
        //Mock the repo response
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        // Get the exception
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.deleteMember(memberId);
        });
        //Che correct message
        assertEquals("Member not found with ID: " + memberId, exception.getMessage());
        verify(memberRepository, times(1)).findById(memberId);
        //Make sure nothing is deleted
        verify(loanRepository, never()).deleteLoansByMemberId(memberId);
        verify(orderRepository, never()).deleteOrdersByMemberId(memberId);
        verify(memberRepository, never()).deleteById(memberId);
    }

    @Test
    void testFindMemberByFullName_whenMemberExists() {
        String fullName = "John Doe";
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        List<Member> membersWithFirstName = List.of(member1);
        //Mock repo response
        when(memberRepository.findByFirstName("John")).thenReturn(membersWithFirstName);
        //get results
        Member result = memberService.findMemberByFullName(fullName);
        //Check the result
        assertNotNull(result);
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testFindMemberByFullName_whenNameIsInvalid() {
        String invalidName = "John";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.findMemberByFullName(invalidName);
        });
        assertEquals("Full name must include both first and last name.", exception.getMessage());
    }

    @Test
    void findMemberByFullName_whenMemberDoesntExist() {
        String fullName = "John Kent";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.findMemberByFullName(fullName);
        });
        assertEquals("No member found with the full name: John Kent", exception.getMessage());
    }

    @Test
    void testFindMember_UserFound() {
        //Mock the classes
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        SecurityContextHolder.setContext(securityContext);
        //Mock the responses
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("a@z.com");
        //Make member mock memeber return
        Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
        when(memberRepository.findByEmail("a@z.com")).thenReturn(Optional.of(member1));
        //Get result
        Member result = memberService.findMember();
        //Check the result
        assertNotNull(result);
        assertEquals("a@z.com", result.getEmail());
    }

    @Test
    void testFindMember_UserIsntFound() {
        //Mock the classes
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        SecurityContextHolder.setContext(securityContext);
        //Mock the responses
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("a@z.com");
        //Make member mock memeber return
        when(memberRepository.findByEmail("a@z.com")).thenReturn(Optional.empty());
        Member result = memberService.findMember();
        //Check the result is null
        assertNull(result);
    }



    @Test
    void testValidPasswords() {
        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z]).{8,}$");
        assertTrue(memberService.isValidPassword("Password123"));
        assertTrue(memberService.isValidPassword("Abcdefgh"));
        assertTrue(memberService.isValidPassword("Averylongpassword1"));
        assertFalse(memberService.isValidPassword("Pass"));
        assertFalse(memberService.isValidPassword("A"));
        assertFalse(memberService.isValidPassword("..."));
    }



    @Test
    void testRegisterMember_SuccessfulRegistration() throws Exception {
        // Mock dto
        MemberRegistrationDto dto = new MemberRegistrationDto();
        dto.setEmail("a@r.com");
        dto.setPassword("Password1");
        dto.setPhone("1478932489790");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddChild(false);

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        Member expectedMember = new Member();
        expectedMember.setEmail("a@r.com");
        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

        Member result = memberService.registerMember(dto);

        assertNotNull(result);
        assertEquals("a@r.com", result.getEmail());

        verify(memberRepository).existsByEmail(dto.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(memberRepository).save(any(Member.class));
        verifyNoInteractions(childMemberRepository);
    }

    @Test
    void testRegisterMember_EmailAlreadyExists() {
        MemberRegistrationDto dto = new MemberRegistrationDto();
        dto.setEmail("a@y.com");
        dto.setPassword("Password1");

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> memberService.registerMember(dto));
        assertEquals("Email is already in use. Please login to continue.", exception.getMessage());

        verify(memberRepository).existsByEmail(dto.getEmail());
        verifyNoInteractions(passwordEncoder, childMemberRepository);
    }

    @Test
    void testRegisterMember_InvalidPassword() {

        MemberRegistrationDto dto = new MemberRegistrationDto();
        dto.setEmail("a@r.com");
        dto.setPassword("...");

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.registerMember(dto));
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter.", exception.getMessage());

        verify(memberRepository).existsByEmail(dto.getEmail());
        verifyNoInteractions(passwordEncoder, childMemberRepository);
    }

    @Test
    void testRegisterMember_AddChildWithEmptyFields() {
        MemberRegistrationDto dto = new MemberRegistrationDto();
        dto.setEmail("a@r.com");
        dto.setPassword("Password1");
        dto.setAddChild(true);
        dto.setChildFirstName("");
        dto.setChildLastName("");

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> memberService.registerMember(dto));
        assertEquals("Child first and/or last name cannot be empty", exception.getMessage());

        verify(memberRepository).existsByEmail(dto.getEmail());
        verifyNoInteractions(passwordEncoder, childMemberRepository);
    }

    @Test
    void testRegisterMember_AddChildSuccessfully() throws Exception {
        MemberRegistrationDto dto = new MemberRegistrationDto();
        dto.setEmail("a@r.com");
        dto.setPassword("Password1");
        dto.setPhone("1478932489790");
        dto.setFirstName("Clark");
        dto.setLastName("Kent");
        dto.setAddChild(true);
        dto.setChildFirstName("Jane");
        dto.setChildLastName("Kent");
        dto.setChildDateOfBirth(LocalDate.of(2010, 1, 1));

        when(memberRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        Member expectedMember = new Member();
        expectedMember.setEmail("a@r.com");
        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

        Member result = memberService.registerMember(dto);

        assertNotNull(result);
        assertEquals("a@r.com", result.getEmail());

        verify(memberRepository).existsByEmail(dto.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(memberRepository).save(any(Member.class));
        verify(childMemberRepository).save(any(ChildMember.class));
    }



}
