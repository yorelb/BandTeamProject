package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;

import java.util.List;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    List<Member> findByMemberType(MemberType memberType);

    List<Member> findByBand(BandInPractice bandInPractice);

    Optional<Member> findByEmail(String username);

    List<Member> findByFirstName(String firstName);

    @Modifying
    @Query("DELETE FROM Member m WHERE m.id = :memberId")
    void deleteById(@Param("memberId") Long memberId);

}
