package com.samta.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",message = "Password at least one upper case alphabet, one digit, one special symbol")
    private String password;

}
