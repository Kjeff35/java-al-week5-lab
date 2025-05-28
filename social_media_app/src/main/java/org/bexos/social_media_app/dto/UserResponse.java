package org.bexos.social_media_app.dto;

import lombok.Getter;
import lombok.Setter;
import org.bexos.social_media_app.model.User;

@Getter
@Setter
public class UserResponse{
    String firstName;
    String lastName;
    String email;

    public UserResponse(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
