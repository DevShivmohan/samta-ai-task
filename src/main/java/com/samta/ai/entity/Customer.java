package com.samta.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @Pattern(regexp = "[a-zA-Z]+",message = "Only alphabets are allowed")
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Column(unique = true,nullable = false)
    @Email
    @NotBlank
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*d)(?=.*[@$!%*#?&])[A-Za-zd@$!%*#?&]{8,}$",message = "Password at least one upper case alphabet, one digit, one special character")
    private String password;

}
