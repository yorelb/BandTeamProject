package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name="child_member")
public class ChildMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "band", nullable = false)
    private BandInPractice band = BandInPractice.None;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Member parent;

    @Column
    LocalDate dateOfBirth;

    public ChildMember(){}

    public ChildMember(Long id, String firstName, String lastName, Member parent, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
        this.dateOfBirth = dateOfBirth;
    }

    public ChildMember(String firstName, String lastName, Member parent, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.parent = parent;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public Member getParent() {
        return parent;
    }

    public BandInPractice getBand() {return band;}

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setParent(Member parent) {
        this.parent = parent;
    }

    public void setBand(BandInPractice band) {this.band = band;}

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int calculateAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
