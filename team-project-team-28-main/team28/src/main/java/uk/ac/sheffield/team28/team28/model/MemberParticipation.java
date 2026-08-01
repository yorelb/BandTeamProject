package uk.ac.sheffield.team28.team28.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "MemberParticipation")
public class MemberParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "performance_id", nullable = false)
    @JsonBackReference
    private Performance performance;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "will_participate", nullable = false)
    private boolean willParticipate;

    // Constructors
    public MemberParticipation() {}

    public MemberParticipation(Performance performance, Member member, boolean willParticipate) {
        this.performance = performance;
        this.member = member;
        this.willParticipate = willParticipate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isWillParticipate() {
        return willParticipate;
    }

    public void setWillParticipate(boolean willParticipate) {
        this.willParticipate = willParticipate;
    }
}
