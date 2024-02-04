package org.example.ssnproject.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {

    @NotNull
    @Size(min=3 ,max=25 , message = "Id must be between 3 to 25 letters")
    public String username;

    @NotNull
    @Size(min =3 ,max = 25 , message = "Password must be between 3 to 25 letters")
    public String password1;

//  Checking password
//  We do not need size argument since we would check for equality
    @NotNull
    public String password2;

    @NotNull
    @Email
    public String email;


}
