package contactManagement.demo.repository;

import contactManagement.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // SELECT id, email, full_name, password FROM users WHERE email={parameter}
    User findByEmail(String email);
}
