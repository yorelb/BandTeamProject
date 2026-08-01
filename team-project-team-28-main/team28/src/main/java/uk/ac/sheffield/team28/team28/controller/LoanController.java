package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.dto.LoanRequestDto;
import uk.ac.sheffield.team28.team28.service.LoanService;
import uk.ac.sheffield.team28.team28.model.Loan;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loanAction")
    public ResponseEntity<String> handleLoanAction(@ModelAttribute LoanRequestDto loanRequestDto, HttpServletResponse response) {
        if (loanRequestDto.getAction() == null) {
            return ResponseEntity.badRequest().body("Action is required");
        }
        System.out.println("Loan request "+loanRequestDto.getAction());
        System.out.println("Member loan request "+loanRequestDto.getMemberName());
        System.out.println("InstrumentId loan request "+loanRequestDto.getInstrumentId());
        try {
            if (loanRequestDto.getAction().contains("loan")) {
                loanService.loanInstrument(loanRequestDto.getInstrumentId(), loanRequestDto.getMemberName());
            } else if (loanRequestDto.getAction().contains("return")) {
                loanService.returnInstrument(loanRequestDto.getInstrumentId());
            } else {
                return ResponseEntity.badRequest().body("Invalid action specified: " + loanRequestDto.getAction());
            }
            response.sendRedirect("/committee/dashboard");
            return ResponseEntity.ok("Action processed successfully");
        } catch (IllegalStateException | NoSuchElementException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
