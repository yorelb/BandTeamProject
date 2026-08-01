package uk.ac.sheffield.team28.team28.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.model.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Team28Application.class)
public class  LoanRepositoryTest {

    @Autowired
    private  LoanRepository  loanRepository;
    @Autowired
    private  MemberRepository  memberRepository;
    @Autowired
    private  ItemRepository  itemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testDeleteById() {
//        Member member = new Member(29L, "x@x.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
//        Item item = new Item(ItemType.Instrument, "Trumpet", "Yamaha","None");
//        Loan newLoan =  new Loan(member, item, LocalDate.of(2018, 1, 8), LocalDate.of(2018, 2, 8));
//        memberRepository.save(member);
//        itemRepository.save(item);
//        loanRepository.save(newLoan);
//        loanRepository.deleteLoansByMemberId(29L);
//        Optional<Loan> deleted = loanRepository.findById(newLoan.getId());
//        assert(deleted.isEmpty());
//    }
}