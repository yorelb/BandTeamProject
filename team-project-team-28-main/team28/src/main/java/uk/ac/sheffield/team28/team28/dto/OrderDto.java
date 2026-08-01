package uk.ac.sheffield.team28.team28.dto;

import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.model.Member;

import java.time.LocalDate;

public class OrderDto {

    private Long orderId;

    private Member member;

    private Long itemId;

    private Item item;

    private LocalDate orderDate;

    private boolean isFulfilled;

    private String note;

    //Constructors
    public OrderDto(){}

    public OrderDto(Long itemId, LocalDate orderDate, String note){
        this.itemId = itemId;
        this.orderDate = orderDate;
        isFulfilled = false;
        this.note = note;
    }

    //Getters and setters

    public Item getItem() {
        return item;
    }

    public String getNote() {
        return note;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public boolean isFulfilled() {
        return isFulfilled;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setFulfilled(boolean fulfilled) {
        isFulfilled = fulfilled;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
