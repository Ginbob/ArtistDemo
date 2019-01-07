package de.burandt.artists.about.controller;

import de.burandt.artists.about.domain.About;
import de.burandt.artists.about.domain.AboutWrapper;
import de.burandt.artists.about.repository.AboutRepository;
import de.burandt.artists.exhibition.controller.viewModel.FileUploadForm;
import de.burandt.artists.painting.config.PaintingFileProperties;
import de.burandt.artists.painting.domain.Hauptkategorie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;

@Controller
@RequestMapping("/about")
public class AboutController {

    private static final Logger LOG = LoggerFactory.getLogger(AboutController.class);

    private AboutRepository aboutRepository;
    private PaintingFileProperties paintingFileProperties;

    @Autowired
    public AboutController(AboutRepository aboutRepository, PaintingFileProperties paintingFileProperties) {
        this.aboutRepository = aboutRepository;
        this.paintingFileProperties = paintingFileProperties;
    }

    @GetMapping(path = "", name = "show_about")
    public ModelAndView about(Principal principal) {
        ModelAndView model = new ModelAndView("about/about");
        List<About> abouts = aboutRepository.findAll();
        About about = abouts.isEmpty() ? getEmptyAbout() : abouts.get(0);
        model.addObject("about", about);
        model.addObject("textParts", divideAboutText(about.getText()));
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    private About getEmptyAbout() {
        About about = new About();
        about.setText("");
        about.setTitle("");
        return about;
    }

    @GetMapping(path = "/edit", name = "show_edit_about")
    public ModelAndView showEditAbout(Principal principal) {
        ModelAndView model = new ModelAndView("about/editAbout");
        List<About> abouts = aboutRepository.findAll();
        About about = abouts.isEmpty() ? getEmptyAbout() : abouts.get(0);
        model.addObject("about", about);
        model.addObject("aboutWrapper", new AboutWrapper(about));
        model.addObject("category", Hauptkategorie.ABOUT.getHauptkategorie());
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @PostMapping(path = "/edit",  name = "save_about")
    public ModelAndView saveAbout(Principal principal,
                                  @ModelAttribute AboutWrapper aboutWrapper,
                                  @ModelAttribute FileUploadForm uploadForm) {
        List<About> abouts = aboutRepository.findAll();
        About about = abouts.isEmpty() ? getEmptyAbout() : abouts.get(0);

        about.setTitle(aboutWrapper.getAbout().getTitle());
        about.setText(aboutWrapper.getAbout().getText());
        aboutRepository.save(about);

        return new ModelAndView(new RedirectView(fromMappingName("show_about").arg(0, principal).build()));
    }

    private List<String> divideAboutText(String text) {
        List<String> textParts = new ArrayList<>();
        if (!text.equals("")) {
            BreakIterator breakIterator = BreakIterator.getSentenceInstance(Locale.GERMAN);
            breakIterator.setText(text);
            int start = breakIterator.first();
            int indexRunner = 0;
            StringBuffer sb = new StringBuffer("");
            int end = breakIterator.next();
            do {
                sb.append(text.substring(start, end));
                if (indexRunner != 0 && indexRunner % 3 == 0) {
                    textParts.add(sb.toString());
                    sb = new StringBuffer("");
                }
                start = end;
                end = breakIterator.next();
                indexRunner++;
            } while (end != BreakIterator.DONE);
            textParts.add(sb.toString());
            return textParts;
        }
        return textParts;
    }
}
