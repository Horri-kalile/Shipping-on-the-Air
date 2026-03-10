package distributed_sota.user_service.application.dto;

public record EmailUpdatedResponseDTO(
        String id,
        String email,
        String message
) {
}
