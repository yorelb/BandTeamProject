package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // ManyToOne as each Request is made by one Member
    @JoinColumn(name = "requester_id", nullable = false) // Foreign key column name
    private Member requester;

    @Column(name = "accepted", nullable = false)
    private boolean accepted;

    @Column(name = "old_first_name", nullable = false)
    private String oldFirstName;

    @Column(name = "new_first_name", nullable = false)
    private String newFirstName;

    @Column(name = "old_last_name", nullable = false)
    private String oldLastName;

    @Column(name = "new_last_name", nullable = false)
    private String newLastName;

    @Column(name="old_phone")
    private String oldPhone;

    @Column(name="new_phone")
    private String newPhone;

    @Column(name = "old_email", nullable = false, unique = true)
    private String oldEmail;

    @Column(name = "new_email", nullable = false, unique = true)
    private String newEmail;


    // Constructors
    public Request() {}

    public Request(Member requester, boolean accepted, Member oldMemberInfo, Member newMemberInfo) {
        this.requester = requester;
        this.accepted = accepted;

        this.oldFirstName = oldMemberInfo.getFirstName();
        this.oldLastName = oldMemberInfo.getLastName();
        this.oldPhone = oldMemberInfo.getPhone();
        this.oldEmail = oldMemberInfo.getEmail();

        this.newFirstName = newMemberInfo.getFirstName();
        this.newLastName = newMemberInfo.getLastName();
        this.newPhone = newMemberInfo.getPhone();
        this.newEmail = newMemberInfo.getEmail();

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getRequester() {
        return requester;
    }

    public void setRequester(Member requester) {
        this.requester = requester;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getOldFirstName() {return oldFirstName;}
    public String getOldLastName() {return oldLastName;}
    public String getOldPhone() {return oldPhone;}
    public String getOldEmail() {return oldEmail;}

    public String getNewFirstName() {return newFirstName;}
    public String getNewLastName() {return newLastName;}
    public String getNewPhone() {return newPhone;}
    public String getNewEmail() {return newEmail;}


    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public void setOldPhone(String oldPhone) {
        this.oldPhone = oldPhone;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public void setOldLastName(String oldLastName) {
        this.oldLastName = oldLastName;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public void setOldFirstName(String oldFirstName) {
        this.oldFirstName = oldFirstName;
    }
}
