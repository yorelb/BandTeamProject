package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.team28.team28.model.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByItemId(Long itemId);

    List<Loan> findByChildMemberId(Long childMemberId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Loan l WHERE l.member.id = :memberId AND l.returnDate IS NULL")
    boolean memberHasActiveLoans(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Loan l WHERE l.childMember.id = :childMemberId AND l.returnDate IS NULL")
    boolean childHasActiveLoans(@Param("childMemberId") Long childMemberId);

    @Modifying
    @Query("DELETE FROM Loan l WHERE l.member.id = :memberId")
    void deleteLoansByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Loan l WHERE l.childMember.id = :childMemberId")
    void deleteLoansByChildMemberId(@Param("childMemberId") Long childMemberId);
}