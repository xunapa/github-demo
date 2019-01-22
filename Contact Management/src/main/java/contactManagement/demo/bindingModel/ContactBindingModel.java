package contactManagement.demo.bindingModel;

import javax.validation.constraints.NotNull;

public class ContactBindingModel {

    // fill the uer input in the html and validate it( fields should have the same names as in the create form
    // otherwise auto fill wont happen
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


    private String mobile;


    private String email;


    @NotNull
    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
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
}
