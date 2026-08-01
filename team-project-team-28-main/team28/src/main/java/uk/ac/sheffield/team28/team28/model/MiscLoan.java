package uk.ac.sheffield.team28.team28.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name="MiscLoan")


public class MiscLoan {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn
    private ChildMember childMember;

    @ManyToOne
    @JoinColumn
    private Item item;

    @Column
    private LocalDate miscLoanDate;

    @Column
    private LocalDate miscReturnDate;

    @Column
    private int miscLoanQuantity;


   public MiscLoan() {}

   public MiscLoan(Member member, Item item, LocalDate miscLoanDate, LocalDate miscReturnDate, int miscLoanQuantity) {
       this.member = member;
       this.item = item;
       this.miscLoanDate = miscLoanDate;
       this.miscReturnDate = miscReturnDate;
       this.miscLoanQuantity =miscLoanQuantity;

   }
   public MiscLoan(Member member, ChildMember childMember, Item item, LocalDate miscLoanDate, LocalDate miscReturnDate, int miscLoanQuantity) {
       this.member = member;
       this.childMember = childMember;
       this.item = item;
       this.miscLoanDate = miscLoanDate;
       this.miscReturnDate = miscReturnDate;
       this.miscLoanQuantity =miscLoanQuantity;
   }

   public Long getId(){
       return id;
   }
   public void setId(Long id){
       this.id = id;
   }
   public Member getMember() {
       return member;
   }
   public void setMember(Member member) {
       this.member = member;
   }
   public ChildMember getChildMember() {
       return childMember;
   }
   public void setChildMember(ChildMember childMember) {
       this.childMember = childMember;
   }
   public Item getItem() {
       return item;
   }
   public void setItem(Item item) {
       this.item = item;
   }
   public LocalDate getMiscLoanDate() {
       return miscLoanDate;
   }
   public void setMiscLoanDate(LocalDate miscLoanDate) {
       this.miscLoanDate = miscLoanDate;
   }
   public LocalDate getMiscReturnDate() {
       return miscReturnDate;
   }
   public void setMiscReturnDate(LocalDate miscReturnDate) {
       this.miscReturnDate = miscReturnDate;
   }
   public int getMiscLoanQuantity() {
       return miscLoanQuantity;
   }

}
