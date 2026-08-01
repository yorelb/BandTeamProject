package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import uk.ac.sheffield.team28.team28.model.MemberType;

import java.time.LocalDate;
import java.util.Objects;

public class MemberRegistrationDto {

    @NotBlank(message = "Please enter a valid email address")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Please enter a password")
    @NotBlank(message = "Please enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private MemberType memberType;

    private String phone;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    //Child Member fields
    private Boolean addChild;
    private String childFirstName;

    private String childLastName;
    private LocalDate childDateOfBirth;

    public MemberRegistrationDto() {}

    public MemberRegistrationDto(String email, String password, MemberType memberType, String phone, String firstName, String lastName,
                                 Boolean addChild, String childFirstName, String childLastName, LocalDate dateOfBirth) {
        this.email = email;
        this.password = password;
        this.memberType = memberType;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;

        this.addChild = addChild;
        this.childFirstName = childFirstName;
        this.childLastName = childLastName;
        this.childDateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getChildFirstName() {
        return childFirstName;
    }

    public String getChildLastName() {
        return childLastName;
    }

    public void setChildFirstName(String childFirstName) {
        this.childFirstName = childFirstName;
    }

    public void setChildLastName(String childLastName) {
        this.childLastName = childLastName;
    }

    public Boolean getAddChild() {
        return addChild;
    }

    public void setAddChild(Boolean addChild) {
        this.addChild = addChild;
    }

    public LocalDate getChildDateOfBirth() {
        return childDateOfBirth;
    }

    public void setChildDateOfBirth(LocalDate childDateOfBirth) {
        this.childDateOfBirth = childDateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberRegistrationDto that = (MemberRegistrationDto) o;
        return Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword()) && getMemberType() == that.getMemberType() && Objects.equals(getPhone(), that.getPhone()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getAddChild(), that.getAddChild()) && Objects.equals(getChildFirstName(), that.getChildFirstName()) && Objects.equals(getChildLastName(), that.getChildLastName()) && Objects.equals(getChildDateOfBirth(), that.getChildDateOfBirth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassword(), getMemberType(), getPhone(), getFirstName(), getLastName(), getAddChild(), getChildFirstName(), getChildLastName(), getChildDateOfBirth());
    }
}
