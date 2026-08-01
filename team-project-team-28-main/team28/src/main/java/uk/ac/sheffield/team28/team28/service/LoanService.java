package uk.ac.sheffield.team28.team28.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LoanService {


    //private final LoanRepository loanRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ChildMemberService childMemberService;


    public Loan createLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Finds a loan by ID
    public Optional<Loan> findLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // Find all loans for a particular member
    public List<Loan> getLoansByMemberId(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    public List<Loan> getLoansByChildMemberId(Long memberId) {
        return loanRepository.findByChildMemberId(memberId);
    }

    public List<Loan> getActiveLoansByMemberId(Long memberId) {
        List<Loan> allLoans = this.getLoansByMemberId(memberId);
        return getActiveLoans(allLoans);
    }

    public List<Loan> getActiveLoansByChildMemberId(Long childMemberId) {
        List<Loan> allLoans = this.getLoansByChildMemberId(childMemberId);
        return getActiveLoans(allLoans);
    }

    // This returns only the loans that haven't been returned yet
    public List<Loan> getActiveLoans(List<Loan> allLoans){
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : allLoans) {
            if (loan.getReturnDate() == null) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    // Find active loan for a particular item using its id
    public Loan findActiveLoanByItemId(Long itemId) {
        List<Loan> loans = loanRepository.findByItemId(itemId);
        return loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active loan found for item ID: " + itemId));
    }

    // Retrieves all loans
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan updateLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Delete a loan by ID
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    public void loanInstrument(Long instrumentId, String memberName) {
        Member member = null;
        try {
            member = memberService.findMemberByFullName(memberName);
        } catch (IllegalArgumentException e) {
            System.out.println("Adult member not found, now trying children.");
        }

        Optional<ChildMember> child = childMemberService.getChildByFullName(memberName);

        if (member == null && child.isEmpty()) {
            throw new IllegalArgumentException("No member found with that name.");
        }

        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new NoSuchElementException("Instrument not found"));

        if (!instrument.getItem().getInStorage()) {
            throw new IllegalStateException("Instrument is already on loan");
        }

        Loan loan = new Loan();
        if (member != null) {
            loan.setMember(member);
        } else {
            loan.setChildMember(child.get());
        }

        loan.setItem(instrument.getItem());
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(null);
        loanRepository.save(loan);

        instrument.getItem().setInStorage(false);
        instrumentRepository.save(instrument);
    }

    public void returnInstrument(Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new NoSuchElementException("Instrument not found"));
        Item item = instrument.getItem();

        if (item.getInStorage()) {
            throw new IllegalStateException("Instrument is not currently on loan");
        }

        Loan loan = this.findActiveLoanByItemId(item.getId());
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        item.setInStorage(true);
        instrumentRepository.save(instrument);
    }
}
