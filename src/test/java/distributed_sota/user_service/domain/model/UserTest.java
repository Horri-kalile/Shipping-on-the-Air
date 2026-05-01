package distributed_sota.user_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Should create a valid user and verify password matches")
    void createValidUser() {
        UserId id = new UserId("user-123");
        Email email = new Email("test@example.com");
        Password password = new Password("Secure123"); // Meets strength requirements

        User user = new User(id, password, email);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail().value()).isEqualTo("test@example.com");
        
        // We verify using the .matches() method because the password is hashed!
        assertThat(user.getPassword().matches(new Password("Secure123"))).isTrue();
    }

    @Test
    @DisplayName("Should throw exception if email is invalid")
    void invalidEmail() {
        assertThatThrownBy(() -> new Email("invalid-email"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
