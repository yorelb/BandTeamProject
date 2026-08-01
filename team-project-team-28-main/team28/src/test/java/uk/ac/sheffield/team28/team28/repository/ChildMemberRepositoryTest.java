package uk.ac.sheffield.team28.team28.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class ChildMemberRepositoryTest {

    @Autowired
    private ChildMemberRepository childMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member parent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        parent = new Member(1L, "parent@test.com", "password", MemberType.ADULT, "0901234567", "John", "Doe");
        memberRepository.save(parent);

    }

    @Test
    void testFindAllByParent() {
        System.out.println("THIS IS THE PARENT  -----------" + parent);
        LocalDate date = LocalDate.of(2018, 1, 8);
        ChildMember child1 = new ChildMember("Johnny", "Doe", parent,date);
        ChildMember child2 = new ChildMember("Jane", "Doe", parent, date);
        childMemberRepository.save(child1);
        childMemberRepository.save(child2);
        List<ChildMember> children = childMemberRepository.findAllByParent(parent);
        assert ((children.size() == 2) &&  (children.stream().allMatch(c -> c.getParent().equals(parent))));
    }

    @Test
    void testFindAllChildren() {
        int currentSize = childMemberRepository.findAllChildren().size();
        LocalDate date = LocalDate.of(2018, 1, 8);
        ChildMember child1 = new ChildMember("Johnny", "Doe", parent,date);
        ChildMember child2 = new ChildMember("Jane", "Doe", parent, date);
        childMemberRepository.save(child1);
        childMemberRepository.save(child2);
        List<ChildMember> children = childMemberRepository.findAllChildren();
        assert (children.size() == currentSize+2);
    }

    @Test
    void testFindAllParents() {
        int currentSize = childMemberRepository.findAllParents().size();
        LocalDate date = LocalDate.of(2018, 1, 8);
        Member parent2 = new Member(1L, "parent@test2.com", "password", MemberType.ADULT, "0901234567", "Jack", "Doe");
        ChildMember child1 = new ChildMember("Johnny", "Doe", parent2,date);
        ChildMember child2 = new ChildMember("Jane", "Doe", parent2, date);
        childMemberRepository.save(child1);
        childMemberRepository.save(child2);
        memberRepository.save(parent2);
        List<Member> parents = childMemberRepository.findAllParents();
        for (Member member : parents) {
            System.out.println("Member: " + member.getFirstName());
        }
        assert (parents.contains(parent2));

    }


    @Test
    void testFindByName() {
        LocalDate date = LocalDate.of(2018, 1, 8);
        String firstName = "John";
        String lastName = "Doe";
        String fullName = firstName + " " + lastName;
        ChildMember child = new ChildMember(firstName, lastName, parent, date);
        childMemberRepository.save(child);
        Optional<ChildMember> theChild = childMemberRepository.findByName(fullName);
        assert(theChild.isPresent() && theChild.get().getFirstName().equals(firstName) && theChild.get().getLastName().equals(lastName));
    }

    @Test
    void testDeleteById() {
        LocalDate date = LocalDate.of(2018, 1, 8);
        ChildMember child = new ChildMember(245L,"John", "Doe", parent, date);
        childMemberRepository.save(child);
        childMemberRepository.deleteById(child.getId());
        Optional<ChildMember> deleted = childMemberRepository.findById(child.getId());
        assert(deleted.isEmpty());
    }

    @Test
    void testFindByBand() {
        BandInPractice band = BandInPractice.Training;
        int currentSize = childMemberRepository.findByBand(band).size();
        LocalDate date = LocalDate.of(2018, 1, 8);
        ChildMember child1 = new ChildMember("John", "Doe", parent, date);
        child1.setBand(band);
        ChildMember child2 = new ChildMember("Jane", "Doe", parent, date);
        child2.setBand(band);
        childMemberRepository.save(child1);
        childMemberRepository.save(child2);

        List<ChildMember> inBand = childMemberRepository.findByBand(band);
        assert (inBand.size() == currentSize + 2 && inBand.stream().allMatch(c -> c.getBand().equals(band)));
    }
}