package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "child_member_id")
    private ChildMember childMember;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    public Loan() {
    }

    public Loan(Member member, Item item, LocalDate loanDate, LocalDate returnDate) {
        this.member = member;
        this.item = item;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Loan(Member member, Item item, LocalDate loanDate, LocalDate returnDate, ChildMember childMember) {
        this.member = member;
        this.item = item;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.childMember = childMember;
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public ChildMember getChildMember() {
        return childMember;
    }
    public void setChildMember(ChildMember childMember) {
        this.childMember = childMember;
    }
}
