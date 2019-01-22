package contactManagement.demo.bindingModel;

import javax.validation.constraints.NotNull;

public class UserBindingModel {

    // this class takes data from the register form and then use it to create a new user
    // it must have the same fields as the form -> then create a method registerProcess in userConroller
    @NotNull
    private String email;

    @NotNull
    private String fullName;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    @NotNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@NotNull String fullName) {
        this.fullName = fullName;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotNull String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
