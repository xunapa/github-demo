package contactManagement.demo.controller;


import contactManagement.demo.entity.Contact;
import contactManagement.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ContactRepository contactRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index(Model model) {

        List<Contact> contacts = this.contactRepository.findAll();

        model.addAttribute("view", "home/index");
        //  add all contacts to the model and send them to the view
        model.addAttribute("contact", contacts);
        return "base-layout";
    }
}
