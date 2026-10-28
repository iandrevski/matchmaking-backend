package sportify.matchmakingbackend.dto;

import sportify.matchmakingbackend.model.User;
import sportify.matchmakingbackend.model.enums.Role;

// јавни податоци за корисникот (без лозинка!)
public record UserDto(Long id, String username, String email, Role role) {

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}