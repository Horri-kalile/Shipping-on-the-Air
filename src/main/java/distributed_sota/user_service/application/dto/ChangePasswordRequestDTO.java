package distributed_sota.user_service.application.dto;

public record ChangePasswordRequestDTO(
        String userId,
        String oldPassword,
        String newPassword
) {
}
