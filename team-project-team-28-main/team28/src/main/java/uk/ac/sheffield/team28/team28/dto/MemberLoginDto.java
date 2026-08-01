package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MemberLoginDto {
    @NotBlank(message = "Please enter a valid email address")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Please enter a password")
    @NotBlank(message = "Please enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public MemberLoginDto() {}

    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
