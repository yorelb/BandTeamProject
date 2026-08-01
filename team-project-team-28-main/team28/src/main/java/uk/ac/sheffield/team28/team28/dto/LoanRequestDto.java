package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.NotBlank;

public class LoanRequestDto {
    private Long instrumentId;
    @NotBlank(message = "Loan action cannot be blank.")
    private String action;
    @NotBlank(message = "Full name cannot be blank.")
    private String memberName;

    public LoanRequestDto() {}

    public LoanRequestDto(Long instrumentId, String action, String memberName) {
        this.instrumentId = instrumentId;
        this.action = action;
        this.memberName = memberName;
    }

    //getters and setters
    public Long getInstrumentId() {
        return instrumentId;
    }
    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

}
