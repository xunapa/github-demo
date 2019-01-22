package contactManagement.demo.repository;

import contactManagement.demo.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
        // primary key of Contact entity is integer
}
