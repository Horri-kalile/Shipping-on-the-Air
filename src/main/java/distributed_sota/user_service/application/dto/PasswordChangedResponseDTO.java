package distributed_sota.user_service.application.dto;

public record PasswordChangedResponseDTO(
        String userId,
        String message
) {
}
