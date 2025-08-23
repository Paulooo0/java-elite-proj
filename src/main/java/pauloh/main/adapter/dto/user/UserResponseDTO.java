package pauloh.main.adapter.dto.user;

import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email) {

}
