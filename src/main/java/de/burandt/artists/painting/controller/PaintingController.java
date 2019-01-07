package de.burandt.artists.painting.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import de.burandt.artists.painting.controller.wrapper.PaintingWrapper;
import de.burandt.artists.painting.domain.CategoryDescription;
import de.burandt.artists.painting.exception.DuplicateFileNameException;
import de.burandt.artists.painting.repository.CategoryDescriptionRepository;
import de.burandt.artists.painting.service.PaintingFileService;
import de.burandt.artists.painting.service.PaintingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import de.burandt.artists.painting.domain.Hauptkategorie;
import de.burandt.artists.painting.exception.CannotResolvePaintingException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

@Controller
@RequestMapping("/art/{category}")
public class PaintingController {

    private static final Logger LOG = LoggerFactory.getLogger(PaintingController.class);
    private static final Map<String, String> UI_TEXTS;

    static {
        UI_TEXTS = new HashMap<>();
        UI_TEXTS.put("abstract.title.show","Nicht-gegenständliche Malerei");
        UI_TEXTS.put("abstract.title.new","Nicht-gegenständliche Malerei - neues Werk erfassen");
        UI_TEXTS.put("abstract.title.edit","Nicht-gegenständliche Malerei - Bearbeiten");
        UI_TEXTS.put("representational.title.show","Gegenständliche Malerei");
        UI_TEXTS.put("representational.title.new","Gegenständliche Malerei - neues Werk erfassen");
        UI_TEXTS.put("representational.title.edit", "Gegenständliche Malerei - Bearbeiten");
        UI_TEXTS.put("current.title.show", "Aktuelle Werke");
        UI_TEXTS.put("current.title.new", "Aktuelle Werke - neues Werk erfassen");
        UI_TEXTS.put("current.title.edit", "Aktuelle Werke - Bearbeiten");
        UI_TEXTS.put("collage.title.show", "Collagen");
        UI_TEXTS.put("collage.title.new", "Collagen - neues Werk erfassen");
        UI_TEXTS.put("collage.title.edit", "Collagen - Bearbeiten");
        UI_TEXTS.put("drawing.title.show", "Zeichnungen");
        UI_TEXTS.put("drawing.title.new", "Zeichnungen - neues Werk erfassen");
        UI_TEXTS.put("drawing.title.edit", "Zeichnungen - Bearbeiten");
    }

    private final PaintingService paintingService;
    private final PaintingFileService paintingFileService;
    private final CategoryDescriptionRepository categoryDescriptionRepository;

    @Autowired
    public PaintingController(PaintingService paintingService,
                              PaintingFileService paintingFileService,
                              CategoryDescriptionRepository categoryDescriptionRepository) {
        this.paintingService = paintingService;
        this.paintingFileService = paintingFileService;
        this.categoryDescriptionRepository = categoryDescriptionRepository;
    }

	@GetMapping(path = "/image/{filename:.+}", produces = {"image/png", "image/jpg", "image/jpeg"})
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "filename") String filename,
                           @PathVariable(value = "category") String category) throws IOException, CannotResolvePaintingException {
    	return paintingFileService.mapFileNameToByteArray(Hauptkategorie.valueOf(category.toUpperCase()), filename);
    }


    @GetMapping(path="", name="show")
    public ModelAndView showCategory(@PathVariable(value = "category") String category,
                                     Principal principal) {
        ModelAndView model = new ModelAndView("painting/view");
        model.addObject("paintings", paintingService.findAllPaintingsByCategory(Hauptkategorie.valueOf(category.toUpperCase())));
        model.addObject("title", UI_TEXTS.get(category + ".title.show"));
        model.addObject("category", category);
        model.addObject("description", getCategoryDescription(category));
        if(principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @GetMapping(name="edit_paintings", path="/edit")
    public ModelAndView editCategory(@PathVariable(value = "category") String category,
                                     Principal principal) {

        return new ModelAndView("painting/editPaintings")
            .addObject("wrapper", new PaintingWrapper(paintingService.findAllPaintingsByCategory(Hauptkategorie.valueOf(category.toUpperCase()))))
            .addObject("title", UI_TEXTS.get(category + ".title.edit"))
            .addObject("category", category)
            .addObject("description", getCategoryDescription(category));
    }

    private CategoryDescription getCategoryDescription(String category) {
        return categoryDescriptionRepository.findById(category)
            .orElseGet(() -> {
                CategoryDescription newDescription = new CategoryDescription(category, "", "");
                return categoryDescriptionRepository.save(newDescription);
            });
    }


    @PostMapping(name="edit_paintings", path="/edit")
    public ModelAndView updatePaintings(@PathVariable(value = "category") String category,
                                        @RequestParam(name = "description-teaser") String descriptionTeaser,
                                        @RequestParam(name = "description-more") String descriptionMore,
                                        @ModelAttribute PaintingWrapper paintingWrapper,
                                        Principal principal) {
	    paintingService.updatePaintings(paintingWrapper.getPaintings());
	    categoryDescriptionRepository.save(new CategoryDescription(category, descriptionTeaser, descriptionMore));
        return new ModelAndView(new RedirectView(fromMappingName("show").arg(0, category).arg(1, principal).build()));
    }



    @GetMapping(name="new_painting", path="/new")
    public ModelAndView newPainting(@PathVariable(value = "category") String category,
                                    Principal principal) {
        ModelAndView model = new ModelAndView("painting/newPainting")
                .addObject("category", category)
                .addObject("title", UI_TEXTS.get(category + ".title.new"));
        return model;
    }

    @PostMapping(name="save_new_painting", path="/new")
    public ModelAndView saveNewRepresentational(
            @RequestParam(name = "paintingname") String paintingname,
            @RequestParam(name = "technique") String technique,
            @RequestParam(name = "height") String height,
            @RequestParam(name = "width") String width,
            @RequestParam(name = "year") String year,
            @PathVariable(value = "category") String category,
            @RequestParam(name = "file") MultipartFile paintingFile,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            paintingService.saveNewPainting(paintingname, technique, height, width, year, paintingFile, category, null);
        } catch (DuplicateFileNameException e) {
            LOG.warn("Dateiname existiert bereits für diese Kategorie");
            redirectAttributes.addFlashAttribute("flashMessage", "Dateiname " + paintingFile.getOriginalFilename() + " existiert bereits für diese Kategorie");
            return new ModelAndView(new RedirectView(fromMethodCall(on(PaintingController.class).newPainting(category, principal)).toUriString()));
        }
        return new ModelAndView(new RedirectView(fromMappingName("show").arg(0, category).arg(1, principal).build()));
    }
}
