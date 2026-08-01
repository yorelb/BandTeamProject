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
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.model.BandInPractice;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Team28Application.class)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsByEmail() {
        //Create the members
        String email = "abi@abi.com";
        Member member = new Member(29L, email, "password", MemberType.ADULT, "090378734", "John", "Doe");
        //Save the member
        memberRepository.save(member);
        //Check if that new member exists
        boolean exists = memberRepository.existsByEmail(email);
        assert (exists);
    }

     @Test
     void testFindByMemberType() {
        // Get database size

        //Make everyone into an adult
         MemberType memberType = MemberType.ADULT;

         Member member1 = new Member(29L, "a@z.com", "password", memberType, "090378734", "John", "Doe");
         Member member2 = new Member(30L, "a@y.com", "password", memberType, "090376734", "Johnny", "Deoe");
         //Save the members
         memberRepository.save(member1);
         memberRepository.save(member2);
         //Find those members
         List<Member> members = memberRepository.findByMemberType(memberType);
         List<Member> theNewMembers = members.subList(members.size() - 2, members.size());
         assert (theNewMembers.stream().allMatch(m -> m.getMemberType() == memberType) && (theNewMembers.size() == 2));
     }
 
     @Test
     void testFindByBand() {
        int initialSize = memberRepository.findByBand(BandInPractice.Training).size();
        //Make members in the training band
         Member member1 = new Member(29L, "a@z.com", "password", MemberType.ADULT, "090378734", "John", "Doe");
         member1.setBand(BandInPractice.Training);
         Member member2 = new Member(30L, "a@y.com", "password", MemberType.ADULT, "090376734", "Johnny", "Deoe");
         member2.setBand(BandInPractice.Training);
         //Save them
         memberRepository.save(member1);
         memberRepository.save(member2);
         //Find all the trainign band memebers
         List<Member> members = memberRepository.findByBand(BandInPractice.Training);
         //Should be 2 members
         List<Member> theNewMembers = members.subList(members.size() - 2, members.size());
         assert members.size() == initialSize + 2;
         assert theNewMembers.stream().allMatch(m -> m.getBand().equals(BandInPractice.Training));
     }
 
     @Test
     void testFindByEmail() {
         // Make new member with email
         String email = "abi@abi.com";
         Member member1 = new Member(29L, email, "password", MemberType.ADULT, "090378734", "John", "Doe");
         member1.setEmail(email);
         //Save them
         memberRepository.save(member1);
         //Find the member
         Optional<Member> foundMember = memberRepository.findByEmail(email);
         assert foundMember.isPresent();
         assert foundMember.get().getEmail().equals(email);
     }

    @Test
    void testDeleteById() {
        Member member1 = new Member(99L, "z@x.com", "password", MemberType.ADULT, "090378734", "Joehn", "Doe");
        memberRepository.save(member1);
        memberRepository.deleteById(member1.getId());
        Optional<Member> deleted = memberRepository.findById(member1.getId());
        assert(deleted.isEmpty());
    }
}