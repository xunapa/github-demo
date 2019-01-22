package contactManagement.demo.controller;

import contactManagement.demo.bindingModel.ContactBindingModel;
import contactManagement.demo.entity.Contact;
import contactManagement.demo.entity.User;
import contactManagement.demo.repository.ContactRepository;
import contactManagement.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contact/")
public class ContactController {
    // get access to the users and journals in the database
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/create")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String create(Model model) {

        // key is 'view' (to be rendered) and load create html from contact folder
        model.addAttribute("view", "contact/create");

        return "base-layout";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String createProcess(ContactBindingModel contactBindingModel) {
        // this method expects data that it needs to auto fill in the binding model
        // get the current logged in user
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // extract the current entity user from the database (username = email)
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        // create a new entry and upload it to the database
        // edit the header.html in /fragments
        Contact contactEntity = new Contact(contactBindingModel.getFirstName(), contactBindingModel.getLastName(), contactBindingModel.getMobile(),
                                            contactBindingModel.getEmail(), userEntity);
        this.contactRepository.saveAndFlush(contactEntity);

        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String details(Model model, @PathVariable Integer id) {
        // check if contact with such id is in the database
        if (!this.contactRepository.exists(id)) {
            return "redirect:/";
        }
        // get the contact from the database
        Contact contact = this.contactRepository.findOne(id);

        // send the contact and the view to the layout
        model.addAttribute("contact", contact);
        model.addAttribute("view", "contact/details");

        return "base-layout";
    }

    ///////////////// Edit  ////////////////////

    @RequestMapping(method = RequestMethod.GET, value = "/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String edit(@PathVariable Integer id, Model model) {

        if (!this.contactRepository.exists(id)) {
            return "redirect:/";
        }

        // get the article
        Contact contact = this.contactRepository.findOne(id);
        // create edit html in contact template folder
        model.addAttribute("view", "contact/edit");
        model.addAttribute("contact", contact);
        return "base-layout";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String editProcess(@PathVariable Integer id, ContactBindingModel contactBindingModel) {
        // use the binding model to validate the user input

        Contact contact = this.contactRepository.findOne(id);
        if (!isAdminOrAuthor(contact)) {
            return "redirect:/";
        }
        contact.setLastName(contactBindingModel.getLastName());
        contact.setFirstName(contactBindingModel.getFirstName());
        contact.setMobile(contactBindingModel.getMobile());
        contact.setEmail(contactBindingModel.getEmail());

        this.contactRepository.saveAndFlush(contact);
        return "redirect:/contact/" + contact.getId();
    }
    // hide the buttons in details view html

    //////////////////////////    Delete   ////////////////////////////////

    //    @GetMapping("/contact/delete/{id}")
    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Model model, @PathVariable Integer id) {
        if (!this.contactRepository.exists(id)) {
            return "redirect:/";
        }
        Contact contact = this.contactRepository.findOne(id);

        model.addAttribute("view", "contact/delete");
        model.addAttribute("contact", contact);
        return "base-layout";
    }

    // services and implementation
    // controllers only announce the action
    @RequestMapping(method = RequestMethod.POST, value = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteProcess(@PathVariable Integer id) {

        Contact contact = this.contactRepository.findOne(id);

        if (!isAdminOrAuthor(contact)) {
            return "redirect:/";
        }

        this.contactRepository.delete(contact);

        return "redirect:/";
    }

    public boolean isAdminOrAuthor(Contact contact) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = this.userRepository.findByEmail(userDetails.getUsername());
        return user.isAdmin() || user.isAuthor(contact);
    }

}
