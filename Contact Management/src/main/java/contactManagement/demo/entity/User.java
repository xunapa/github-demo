package contactManagement.demo.entity;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    // eager- roles will be loaded together with the user
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    private Set<Role> roles;

//    @OneToMany(mappedBy = "creator")
//    private Set<Contact> contacts;

    // go to the contact entity and find the author, then get the properties of the relation and use them as a base to create the foreign key
    // constraints in the db
    @OneToMany(mappedBy = "author")
    private Set<Contact> contacts;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Transient
    public boolean isAdmin(){
        return this.getRoles()
            .stream()
            .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Contact contact){
        return Objects.equals(
            this.getId(),
            contact.getAuthor().getId()
        );
    }

//    public Set<Contact> getContacts() {
//        return contacts;
//    }
//    public void setContacts(Set<Contact> contacts) {
//        this.contacts = contacts;
//    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // to keep all the contacts created by a given User(unordered)
    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public User() {
    }

    public User(String email, String fullName, String password) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;


        this.roles = new HashSet<>();

        this.contacts = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
