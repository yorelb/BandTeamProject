package uk.ac.sheffield.team28.team28.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private InstrumentDto instrumentDto;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanService loanService;

    @Mock
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateLoan() {
        //Make a loan
        Loan loan = new Loan();
        //Mock the repo response
        when(loanRepository.save(loan)).thenReturn(loan);
        //Create a loan
        Loan createdLoan = loanService.createLoan(loan);
        //Check the loan has been created
        assertNotNull(createdLoan);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testFindLoanById() {
        // Make a loan
        Loan loan = new Loan(new Member(),new Item(), LocalDate.of(2021, 1, 1),LocalDate.of(2021, 1, 10));
        loan.setId(1L);
        //Mock the repo response
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        //Get the loan
        Optional<Loan> theLoan = loanService.findLoanById(1L);
        //Check the loan exists
        assertTrue(theLoan.isPresent());
        assertEquals(1L, theLoan.get().getId());
    }

    @Test
    void testGetLoansByMemberId() {
        //Make the loans
        Loan loan = new Loan();
        Loan loan2 = new Loan();
        Long memberId = 1L;
        //Put the loans in an array
        List<Loan> loans = List.of(loan,loan2);
        //Mock repo response
        when(loanRepository.findByMemberId(memberId)).thenReturn(loans);
        //Get the result
        List<Loan> result = loanService.getLoansByMemberId(memberId);
        //Check the result
        assertEquals(2, result.size());
        verify(loanRepository, times(1)).findByMemberId(memberId);
    }

    @Test
    void testGetActiveLoansByMemberId() {
        //Make loans
        Loan activeLoan = new Loan();
        activeLoan.setReturnDate(null);
        Loan returnedLoan = new Loan();
        returnedLoan.setReturnDate(LocalDate.now());
        Long memberId = 1L;
        //Put the loans into an array
        List<Loan> loans = List.of(activeLoan, returnedLoan);
        //Mock the repo response
        when(loanRepository.findByMemberId(memberId)).thenReturn(loans);
        //Get the active loans
        List<Loan> activeLoans = loanService.getActiveLoansByMemberId(memberId);
        // Check only the active loan was returned and that its actually active
        assertEquals(1, activeLoans.size());
        assertNull(activeLoans.get(0).getReturnDate());
    }

    @Test
    void testFindActiveLoanByItemId() {
        // Make the loan
        Loan activeLoan = new Loan();
        activeLoan.setReturnDate(null);
        Long itemId = 1L;
        //Mock the repo response
        when(loanRepository.findByItemId(itemId)).thenReturn(List.of(activeLoan));
        // Get the result
        Loan result = loanService.findActiveLoanByItemId(itemId);
        //Check the result
        assertNotNull(result);
        assertNull(result.getReturnDate());
    }

    @Test
    void testFindActiveLoanByItemId_WhenItemDoesntExist() {
        Long itemId = 1L;
        //Mock the repo response
        when(loanRepository.findByItemId(itemId)).thenReturn(new ArrayList<>());
        // Get the exception
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            loanService.findActiveLoanByItemId(itemId);
        });
        //Check the result
        assertEquals("No active loan found for item ID: "+itemId, exception.getMessage());
    }

    @Test
    void testGetAllLoans() {
        // Create the loans
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();
        loan1.setId(1L);
        loan2.setId(2L);
        // Mock the repo response
        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));
        // Get the result
        List<Loan> result = loanService.getAllLoans();
        // Check the result
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void testUpdateLoan() {
        // Make a loan
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReturnDate(LocalDate.of(2024, 12, 1));
        // Mock the repo response
        when(loanRepository.save(loan)).thenReturn(loan);
        // Update the loan
        Loan updatedLoan = loanService.updateLoan(loan);
        // Check loan is updated
        assertNotNull(updatedLoan);
        assertEquals(1L, updatedLoan.getId());
        assertEquals(LocalDate.of(2024, 12, 1), updatedLoan.getReturnDate());
        verify(loanRepository, times(1)).save(loan);
    }


    @Test
    void testDeleteLoan() {
        Loan loan = new Loan(new Member(),new Item(), LocalDate.of(2021, 1, 1),LocalDate.of(2021, 1, 10));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        loanService.deleteLoan(1L); // Delete the child
        //Verify Deleted loans
        verify(loanRepository, times(1)).deleteById(1L);
        }

    @Test
    void testLoanInstrument() {
        // Make the things needed for the loan
        Member member = new Member();
        member.setFirstName("Barry");
        member.setLastName("Allen");
        Item item = new Item();
        item.setInStorage(true);
        Instrument instrument = new Instrument();
        instrument.setItem(item);
        //Mock the repo and service responses
        when(memberService.findMemberByFullName("Barry Allen")).thenReturn(member);
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Loan the instrument
        loanService.loanInstrument(1L,"Barry Allen");
        // Test that the correct actions happen
        assertFalse(item.getInStorage());
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(instrumentRepository, times(1)).save(instrument);
    }

    @Test
    void testReturnInstrument() {

        Item item = new Item();
        item.setInStorage(false);
        Instrument instrument = new Instrument();
        instrument.setItem(item);

        Loan activeLoan = new Loan();
        activeLoan.setItem(item);
        activeLoan.setReturnDate(null);
        //Mock the repo responses
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));
        when(loanRepository.findByItemId(item.getId())).thenReturn(List.of(activeLoan));
        when(loanRepository.save(activeLoan)).thenReturn(activeLoan);

        // Retiurn the instrument
        loanService.returnInstrument(1L);

        // Check the correct actions were taken
        assertTrue(item.getInStorage());
        assertNotNull(activeLoan.getReturnDate());
        verify(loanRepository, times(1)).save(activeLoan);
        verify(instrumentRepository, times(1)).save(instrument);
    }

    @Test
    void testReturnInstrument_WhenInsturmentOnLoan() {
        //Make a new instrument
        Item item = new Item();
        item.setInStorage(true); //Isnt on loan
        Instrument instrument = new Instrument();
        instrument.setItem(item);
        //Mock the repo response
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));

        // Get the exception
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            loanService.returnInstrument(1L);
        });
        //Check the right exception is given
        assertEquals("Instrument is not currently on loan", exception.getMessage());
    }

    @Test
    void testReturnInstrument_WhenInsturmentDoesntExist() {
        //Mock the repo response
        when(instrumentRepository.findById(1L)).thenReturn(Optional.empty());
        // Get the exception
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            loanService.returnInstrument(1L);
        });
        //Check the right exception is given
        assertEquals("Instrument not found", exception.getMessage());
    }
}
