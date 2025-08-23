package pauloh.main.adapter.input.dto.user;

import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email) {

}
