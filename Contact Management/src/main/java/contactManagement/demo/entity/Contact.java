package contactManagement.demo.entity;

import contactManagement.demo.helper.exception.CheckAtLeastOneNotNull;

import javax.persistence.*;

@Entity
@CheckAtLeastOneNotNull(fieldNames = {"mobile", "email"})
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    // text does not have limit on the field
    @Column(columnDefinition = "text", nullable = false)
    private String lastName;


    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;


    // one user will have many entries in the contact and we join columns to create a column to keep the relations(it cannot be null)
    @ManyToOne
    @JoinColumn(nullable = false, name = "authorId")
    private User author;

    // this method should not be saved in our database
    @Transient
    public String getSummary() {
        // get half of the lastName
        return this.getLastName().substring(0, this.getLastName().length()/2) + "...";
    }

    public Contact() {
    }

    public Contact(String firstName, String lastName, String mobile, String email, User author) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;

        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
