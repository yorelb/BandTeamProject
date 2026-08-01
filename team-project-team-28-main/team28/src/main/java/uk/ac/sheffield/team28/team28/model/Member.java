package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType = MemberType.ADULT;

    @Column(name = "band", nullable = false)
    private BandInPractice band = BandInPractice.None;

    @Column(name="phone")
    private String phone;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

//    @Enumerated(EnumType.STRING)
//    @Column
//    private BandInPractice bandInPractice = BandInPractice.None;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChildMember> childMembers = new HashSet<>();


    public Member() {}

    public Member(Long id, String email, String password, MemberType memberType, String phone, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberType = memberType != null ? memberType : MemberType.ADULT;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;

    }

    public Member(Long id, String email, String password, String phone, String firstName, String lastName) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.memberType = MemberType.ADULT;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BandInPractice getBand() {
        return band;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public void setBand(BandInPractice band) {
        this.band = band;
    }

    public void setPhone(String phone) {
    this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Member convertedObj = (Member) obj;
        return this.id.equals(convertedObj.id) &&
                this.email.equals(convertedObj.email) &&
                this.password.equals(convertedObj.password) &&
                this.memberType == convertedObj.memberType &&
                this.firstName.equals(convertedObj.firstName) &&
                this.lastName.equals(convertedObj.lastName) &&
                this.phone.equals(convertedObj.phone) &&
                this.band == convertedObj.band;
    }

}

