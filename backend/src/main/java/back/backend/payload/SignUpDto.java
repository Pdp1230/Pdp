package back.backend.payload;

import lombok.Data;

@Data
public class SignUpDto {
    private String name;
    private String surname;
    private String email;
    private String password;
}