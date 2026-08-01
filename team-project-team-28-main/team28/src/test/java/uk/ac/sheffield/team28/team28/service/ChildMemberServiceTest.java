package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChildMemberServiceTest {

    @Mock
    private ChildMemberRepository childMemberRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private ChildMemberService childMemberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetChildByFullName() {
        String fullName = "john doe";
        String capitalizedFullName = "John Doe";

        ChildMember childMember = new ChildMember();
        when(childMemberRepository.findByName(capitalizedFullName)).thenReturn(Optional.of(childMember));

        Optional<ChildMember> result = childMemberService.getChildByFullName(fullName);

        assertTrue(result.isPresent());
        assertEquals(childMember, result.get());
        verify(childMemberRepository, times(1)).findByName(capitalizedFullName);
    }

    @Test
    void testGetChildByParent() {
        Member parent = new Member();
        List<ChildMember> children = Arrays.asList(new ChildMember(), new ChildMember());
        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);

        List<ChildMember> result = childMemberService.getChildByParent(parent);

        assertEquals(children, result);
        verify(childMemberRepository, times(1)).findAllByParent(parent);
    }

    @Test
    void testGetChildById_UsingRealId() throws Exception {
        Long childId = 1L;
        ChildMember childMember = new ChildMember();
        when(childMemberRepository.findById(childId)).thenReturn(Optional.of(childMember));

        ChildMember result = childMemberService.getChildById(childId);

        assertEquals(childMember, result);
        verify(childMemberRepository, times(1)).findById(childId);
    }

    @Test
    void testGetChildById_UsingInvalidId() {
        Long childId = 2L;
        when(childMemberRepository.findById(childId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            childMemberService.getChildById(childId);
        });
        assertEquals("Member not found with ID: " + childId, exception.getMessage());
        verify(childMemberRepository, times(1)).findById(childId);
    }


    @Test
    void testGetAllChildren() {
        List<ChildMember> children = Arrays.asList(new ChildMember(), new ChildMember());
        when(childMemberRepository.findAllChildren()).thenReturn(children);

        List<ChildMember> result = childMemberService.getAllChildren();

        assertEquals(children, result);
        verify(childMemberRepository, times(1)).findAllChildren();
    }

    @Test
    void testGetAllParents() {
        List<Member> parents = Arrays.asList(new Member(), new Member());
        when(childMemberRepository.findAllParents()).thenReturn(parents);

        List<Member> result = childMemberService.getAllParents();

        assertEquals(parents, result);
        verify(childMemberRepository, times(1)).findAllParents();
    }

    @Test
    void testDoAnyChildrenHaveLoans_ChildrenDoHaveLoans() {
        Member parent = new Member();
        ChildMember child = new ChildMember();
        child.setId(1L);
        List<ChildMember> children = List.of(child);

        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);
        when(loanRepository.childHasActiveLoans(1L)).thenReturn(true);

        boolean result = childMemberService.doAnyChildrenHaveLoans(parent);

        assertTrue(result);
        verify(loanRepository, times(1)).childHasActiveLoans(1L);
    }


    @Test
    void testDoAnyChildrenHaveLoans_NoChildrenHaveLoans() {
        Member parent = new Member();
        ChildMember child = new ChildMember();
        child.setId(1L);
        List<ChildMember> children = List.of(child);

        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);
        when(loanRepository.childHasActiveLoans(1L)).thenReturn(false);
        boolean result = childMemberService.doAnyChildrenHaveLoans(parent);
        assertFalse(result);
        verify(loanRepository, times(1)).childHasActiveLoans(1L);
    }


//--------------------------------------------------------------------------------------------------------------------
    @Test
    void testAddChildMemberToBand_ChildNotFound() {
        Long childMemberId = 1L;
        ChildMember child = new ChildMember();
        when(childMemberRepository.findById(childMemberId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            childMemberService.addChildMemberToBand(childMemberId);
        });
        assertEquals("Child member not found with ID: " + childMemberId, exception.getMessage());
        verify(childMemberRepository, times(1)).findById(childMemberId);
    }

    @Test
    void testAddChildMemberToBand_AlreadyInTrainingBand() throws Exception {
        Long childMemberId = 1L;
        ChildMember child = new ChildMember();
        child.setBand(BandInPractice.Training);
        when(childMemberRepository.findById(childMemberId)).thenReturn(Optional.of(child));
        Exception exception = assertThrows(Exception.class, () -> {
            childMemberService.addChildMemberToBand(childMemberId);
        });
        assertEquals("Child member is already in this band.", exception.getMessage());
        verify(childMemberRepository, times(1)).findById(childMemberId);
    }

    @Test
    void testAddChildMemberToBand_NoneBand() throws Exception {
        Long childMemberId = 1L;
        ChildMember child = new ChildMember();
        child.setBand(BandInPractice.None);
        when(childMemberRepository.findById(childMemberId)).thenReturn(Optional.of(child));
        ChildMember updatedChildMember = childMemberService.addChildMemberToBand(childMemberId);
        assertEquals(BandInPractice.Training, updatedChildMember.getBand());
        verify(childMemberRepository, times(1)).findById(childMemberId);
        verify(childMemberRepository, times(1)).save(updatedChildMember);
    }

    @Test
    void testAddChildMemberToBand_Success() throws Exception {
        Long childMemberId = 1L;
        ChildMember child = new ChildMember();
        child.setBand(BandInPractice.None);
        when(childMemberRepository.findById(childMemberId)).thenReturn(Optional.of(child));
        ChildMember updatedChildMember = childMemberService.addChildMemberToBand(childMemberId);
        assertNotNull(updatedChildMember);
        assertEquals(BandInPractice.Training, updatedChildMember.getBand());
        verify(childMemberRepository, times(1)).findById(childMemberId);
        verify(childMemberRepository, times(1)).save(updatedChildMember);
    }

    @Test
    void testRemoveChildMemberFromBand_ChildNotFound() {
        Long childMemberId = 1L;
        ChildMember child = new ChildMember();
        when(childMemberRepository.findById(childMemberId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            childMemberService.removeChildMemberFromBand(childMemberId);
        });
        assertEquals("Child member not found with ID: " + childMemberId, exception.getMessage());
        verify(childMemberRepository, times(1)).findById(childMemberId);
    }

    @Test
    void testRemoveChildMemberFromBand_ChildNotInBand() throws Exception {
        Long childMemberId = 1L;
        ChildMember child = new ChildMember();
        child.setBand(BandInPractice.None);
        when(childMemberRepository.findById(childMemberId)).thenReturn(Optional.of(child));
        Exception exception = assertThrows(Exception.class, () -> {
            childMemberService.removeChildMemberFromBand(childMemberId);
        });
        assertEquals("Child member is already not assigned to a band", exception.getMessage());
        verify(childMemberRepository, times(1)).findById(childMemberId);
    }

    @Test
    void testRemoveChildMemberFromBand_ChildInBand() throws Exception {
        //Create child
        Long childId = 1L;
        ChildMember child = new ChildMember();
        //Set band
        child.setBand(BandInPractice.Training);
        //Mock repo response
        when(childMemberRepository.findById(childId)).thenReturn(Optional.of(child));
        //Remove the child from the band
        ChildMember result = childMemberService.removeChildMemberFromBand(childId);
        assertEquals(BandInPractice.None, result.getBand()); //Check if removed
        verify(childMemberRepository, times(1)).save(child);
    }

    @Test
    void testDeleteChildMembers() throws Exception {
        //Make parent
        Member parent = new Member();
        //Make children
        ChildMember child1 = new ChildMember();
        child1.setId(1L);
        ChildMember child2 = new ChildMember();
        child2.setId(2L);
        //Put them in a list
        List<ChildMember> children = Arrays.asList(child1, child2);
        //Mock the method response
        when(childMemberRepository.findAllByParent(parent)).thenReturn(children);
        childMemberService.deleteChildMembers(parent);
        //Delete the childrens loans
        verify(loanRepository, times(1)).deleteLoansByChildMemberId(1L);
        verify(loanRepository, times(1)).deleteLoansByChildMemberId(2L);
        //Delete the children
        verify(childMemberRepository, times(1)).deleteById(1L);
        verify(childMemberRepository, times(1)).deleteById(2L);
    }

    @Test
    void testGetTrainingBandMembers() {
        //Make children
        ChildMember child1 = new ChildMember("Johnny", "Doe", null, LocalDate.of(2010, 1, 1));
        ChildMember child2 = new ChildMember("Jane", "Smith", null, LocalDate.of(2011, 5, 20));
        //Mock the repo response
        when(childMemberRepository.findByBand(BandInPractice.Training))
                .thenReturn(List.of(child1, child2));
        //Get the training band members
        List<ChildMember> trainingBandMembers = childMemberService.getTrainingBandMembers();
        assertNotNull(trainingBandMembers); //Check not empty
        assertEquals(2, trainingBandMembers.size()); //Check size
        //Check elements
        assertTrue(trainingBandMembers.contains(child1));
        assertTrue(trainingBandMembers.contains(child2));
        verify(childMemberRepository, times(1)).findByBand(BandInPractice.Training);
    }

    @Test
    void testGetParentsWithChildren() {
        //Make parents
        Member parent1 = new Member(1L, "parent1@test.com", "password", MemberType.ADULT, "0901234567", "John", "Doe");
        Member parent2 = new Member(2L, "parent2@test.com", "password", MemberType.ADULT, "0909876543", "Jane", "Smith");
        //Make Children
        ChildMember child1 = new ChildMember("Johnny", "Doe", parent1, LocalDate.of(2010, 1, 1));
        ChildMember child2 = new ChildMember("Jenny", "Doe", parent1, LocalDate.of(2011, 5, 20));
        ChildMember child3 = new ChildMember("Alice", "Smith", parent2, LocalDate.of(2012, 3, 15));
        //Mock method call responses
        when(childMemberService.getAllParents()).thenReturn(List.of(parent1, parent2));
        when(childMemberService.getChildByParent(parent1)).thenReturn(List.of(child1, child2));
        when(childMemberService.getChildByParent(parent2)).thenReturn(List.of(child3));
        //Create the map
        Map<Member, List<ChildMember>> parentsWithChildren = childMemberService.getParentsWithChildren();
        assertNotNull(parentsWithChildren); //Check it's not empty
        //Check the size, and that the parents map to the children
        assertEquals(2, parentsWithChildren.size());
        assertEquals(List.of(child1, child2), parentsWithChildren.get(parent1));
        assertEquals(List.of(child3), parentsWithChildren.get(parent2));

        // Verify method calls
//        verify(childMemberService, times(1)).getAllParents();
//        verify(childMemberService, times(1)).getChildByParent(parent1);
//        verify(childMemberService, times(1)).getChildByParent(parent2);
    }



    @Test
    void testDoesChildDoesHaveLoans_ChildHasLoans() {
        //Make child
        ChildMember child = new ChildMember("Johnny", "Doe", null, LocalDate.of(2010, 1, 1));
        child.setId(1L);
        //Mock response
        when(loanRepository.childHasActiveLoans(child.getId())).thenReturn(true);
        //Get the result
        boolean hasLoans = childMemberService.doesChildHaveLoans(child);
        assertTrue(hasLoans); //Check result
        verify(loanRepository, times(1)).childHasActiveLoans(child.getId());
    }

    @Test
    void testDoesChildHaveLoans_ChildHasNoLoans() {
        //Make child
        ChildMember child = new ChildMember("Johnny", "Doe", null, LocalDate.of(2010, 1, 1));
        child.setId(1L);
        //Mock repo response
        when(loanRepository.childHasActiveLoans(child.getId())).thenReturn(false);
        //Get result
        boolean hasLoans = childMemberService.doesChildHaveLoans(child);
        assertFalse(hasLoans); //Check result
        verify(loanRepository, times(1)).childHasActiveLoans(child.getId());
    }


    @Test
    void testDeleteChildMember() throws Exception {
        //Make a child
        ChildMember child = new ChildMember("Johnny", "Doe", null, LocalDate.of(2010, 1, 1));
        child.setId(1L);
        //Mock the repo response
        when(childMemberRepository.existsById(child.getId())).thenReturn(true);
        childMemberService.deleteChildMember(child); // Delete the child
        //Verify Deleted loans
        verify(loanRepository, times(1)).deleteLoansByChildMemberId(child.getId());
        //Verify deleted child
        verify(childMemberRepository, times(1)).deleteById(child.getId());
    }
}


