package uk.ac.sheffield.team28.team28.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.team28.team28.model.Loan;
import uk.ac.sheffield.team28.team28.model.MiscLoan;

import java.util.List;


public interface MiscLoanRepository extends JpaRepository<MiscLoan, Long> {
    List<MiscLoan> findByMemberId(Long memberId);
    List<MiscLoan> findByItemId(Long itemId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM MiscLoan l WHERE l.member.id = :memberId AND l.miscReturnDate IS NULL")
    boolean memberHasActiveLoans(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END " +
            "FROM MiscLoan l WHERE l.childMember.id = :childMemberId AND l.miscReturnDate IS NULL")
    boolean childHasActiveLoans(@Param("childMemberId") Long childMemberId);

    @Modifying
    @Query("DELETE FROM MiscLoan l WHERE l.member.id = :memberId")
    void deleteLoansByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM MiscLoan l WHERE l.childMember.id = :childMemberId")
    void deleteLoansByChildMemberId(@Param("childMemberId") Long childMemberId);


}
