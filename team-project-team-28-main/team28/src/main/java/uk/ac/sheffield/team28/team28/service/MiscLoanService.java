package uk.ac.sheffield.team28.team28.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.model.MiscLoan;
import uk.ac.sheffield.team28.team28.repository.LoanRepository;
import uk.ac.sheffield.team28.team28.repository.MiscLoanRepository;
import uk.ac.sheffield.team28.team28.repository.MiscRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MiscLoanService {


    private MiscLoanRepository miscLoanRepository;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MiscRepository miscRepository;
    public MiscLoanService(MiscLoanRepository miscLoanRepository) {
        this.miscLoanRepository = miscLoanRepository;
    }

    public MiscLoan createMiscLoan(MiscLoan miscLoan) {
        return miscLoanRepository.save(miscLoan);
    }

    // Finds a loan by ID
    public Optional<MiscLoan> findLoanById(Long id) {
        return miscLoanRepository.findById(id);
    }




}
