package de.burandt.artists.impressum;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("impressum")
public class ImpressumController {

    @GetMapping(name = "impressum", path = "")
    public ModelAndView showImpressum(Principal principal) {
        ModelAndView model = new ModelAndView("impressum/impressum");
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @GetMapping(name = "aboutDeveloper", path = "/developer")
    public ModelAndView aboutDeveloper(Principal principal) {
        ModelAndView model = new ModelAndView("impressum/aboutDeveloper");
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }
}
