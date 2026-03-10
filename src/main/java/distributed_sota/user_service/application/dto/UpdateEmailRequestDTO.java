package distributed_sota.user_service.application.dto;

public record UpdateEmailRequestDTO(
        String name,
        String password,
        String email
) {
}
