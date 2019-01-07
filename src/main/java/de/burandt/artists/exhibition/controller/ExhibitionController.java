package de.burandt.artists.exhibition.controller;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import de.burandt.artists.exhibition.controller.viewModel.CurrentOrFutureExhibitionViewModel;
import de.burandt.artists.exhibition.controller.viewModel.PastExhibitionViewModel;
import de.burandt.artists.exhibition.controller.wrapper.ExhibitionWrapper;
import de.burandt.artists.exhibition.repository.ExhibitionPaintingRepository;
import de.burandt.artists.exhibition.repository.ExhibitionPosterRepository;
import de.burandt.artists.exhibition.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import de.burandt.artists.exhibition.domain.Exhibition;
import de.burandt.artists.exhibition.repository.ExhibitionRepository;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;

@Controller
@RequestMapping("/exhibition")
public class ExhibitionController {

    private final ExhibitionService exhibitionService;
    private final ExhibitionRepository exhibitionRepo;
    private final ExhibitionPaintingRepository exhibitionPaintingRepository;
	private final ExhibitionPosterRepository exhibitionPosterRepository;

    @Autowired
    public ExhibitionController(ExhibitionService exhibitionService,
                                ExhibitionRepository exhibitionRepo,
                                ExhibitionPaintingRepository exhibitionPaintingRepository,
                                ExhibitionPosterRepository exhibitionPosterRepository) {
        this.exhibitionService = exhibitionService;
        this.exhibitionRepo = exhibitionRepo;
        this.exhibitionPaintingRepository = exhibitionPaintingRepository;
        this.exhibitionPosterRepository = exhibitionPosterRepository;
    }

    @GetMapping(path="", name="exhibition")
    public ModelAndView exhibitions(Principal principal) {
        updateExhibitions();
        List<PastExhibitionViewModel> pastExhibitions = getPastExhibitionViewModels(exhibitionRepo.findAllByCurrentFutureOrderByStartDateAsc(false));
        List<CurrentOrFutureExhibitionViewModel> currentOrFutureExhibitions = convertToViewModels(exhibitionRepo.findAllByCurrentFutureOrderByStartDateAsc(true));

        ModelAndView model = new ModelAndView("exhibition/exhibition")
                .addObject("pastExhibitions", pastExhibitions)
                .addObject("currentOrFutureExhibitions", currentOrFutureExhibitions);
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    private List<CurrentOrFutureExhibitionViewModel> convertToViewModels(List<Exhibition> allByCurrentFuture) {
        return allByCurrentFuture.stream().map(exhibition -> {
            if (exhibition.getPoster() != null) {
                return new CurrentOrFutureExhibitionViewModel(exhibition.getId(), exhibition.getTitle(), exhibition.getStartDate(),
                        exhibition.getEndDate(), exhibition.getLocation(), exhibition.getPoster().getFilename());
            } else {
                return new CurrentOrFutureExhibitionViewModel(exhibition.getId(), exhibition.getTitle(), exhibition.getStartDate(),
                        exhibition.getEndDate(), exhibition.getLocation(), null);
            }
        }).collect(Collectors.toList());
    }

    /**
     * checks whether or not current or future exhibitions should be flagged as past exhibitions
     */
    private void updateExhibitions() {
        List<Exhibition> currentOrFutureExhibitions = exhibitionRepo.findAllByCurrentFutureOrderByStartDateAsc(true);
        List<Exhibition> exhibitionsToBeUpdated = new ArrayList<>();
        currentOrFutureExhibitions.forEach(exhibition -> {
            if(Date.from(Instant.now()).after(exhibition.getEndDate())) {
                exhibition.setCurrentFuture(isCurrentOrFuture(exhibition));
                exhibitionsToBeUpdated.add(exhibition);
            }
        });
        if(!exhibitionsToBeUpdated.isEmpty()) {
            exhibitionRepo.saveAll(exhibitionsToBeUpdated);
        }
    }

    private boolean isCurrentOrFuture(Exhibition exhibition) {
        if(Date.from(Instant.now()).after(exhibition.getEndDate())) {
            return false;
        }
        return true;
    }

    private List<PastExhibitionViewModel> getPastExhibitionViewModels(List<Exhibition> pastExhibitions) {
        return pastExhibitions.stream().map(exhibition -> {
            String exhibitionDate;
            Calendar calendarStartDate = Calendar.getInstance();
            Calendar calendarEndDate = Calendar.getInstance();
            calendarStartDate.setTime(exhibition.getStartDate());
            calendarEndDate.setTime(exhibition.getEndDate());
            if(calendarStartDate.get(Calendar.YEAR) == calendarEndDate.get(Calendar.YEAR)) {
                exhibitionDate = calendarEndDate.get(Calendar.YEAR) + "";
            } else {
                exhibitionDate = calendarStartDate.get(Calendar.YEAR) + " / " + calendarEndDate.get(Calendar.YEAR);
            }
            return new PastExhibitionViewModel(exhibition.getId(), exhibitionDate, exhibition.getTitle(), null);
        }).collect(Collectors.toList());
    }


    @GetMapping(path="/new")
    public ModelAndView newExhibition(Principal principal) {
        ModelAndView model = new ModelAndView("exhibition/newExhibition");
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @PostMapping(path="/new", name="save_new_exhibition")
    public ModelAndView saveNewExhibition(@RequestParam(name="startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam(name="enddate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                          @RequestParam(name="description") String description,
                                          @RequestParam(name="link") String link,
                                          @RequestParam(name="title") String title,
                                          @RequestParam(name="exhibitor") String exhibitor,
                                          @RequestParam(name="location") String location,
                                          @RequestParam(name="openingtimes") String openingTimes,
                                          @RequestParam(name="vernissage") String vernissage,
                                          @RequestParam(name="exhibition-pictures") MultipartFile[] picturesUpload,
                                          @RequestParam(name="exhibition-poster") MultipartFile exhibitionPoster,
                                          Principal principal) {
        if (!link.startsWith("http")) {
            link = "https://" + link;
        }
        exhibitionService.saveNewExhibition(startDate, endDate, title, exhibitor, location, openingTimes, vernissage, link, description, picturesUpload, exhibitionPoster);
        ModelAndView model = new ModelAndView(new RedirectView(fromMappingName("exhibition").build()));
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @GetMapping(path="/{id}", name="show_exhibition_detail")
    public ModelAndView showExhibitionDetail(@PathVariable(value = "id") String id,
                                             Principal principal) {
        Exhibition exhibition = exhibitionRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        ModelAndView model = new ModelAndView("exhibition/exhibitionDetail")
                .addObject("exhibition", exhibition);
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @GetMapping(path="/{id}/edit", name="edit_exhibitions")
    public ModelAndView editExhibitions(@PathVariable(name = "id") String id,
                                        Principal principal) {
        Exhibition exhibition = exhibitionRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        ModelAndView model = new ModelAndView("exhibition/editExhibition")
                .addObject("exhibition", exhibition)
                .addObject("exhibitionWrapper", new ExhibitionWrapper(exhibition));
        if (principal != null) {
            model.addObject("loggedIn", true);
        }
        return model;
    }

    @PostMapping(path="/edit/{id}", name="save_edited_exhibition")
    public ModelAndView saveExhibitions(@ModelAttribute ExhibitionWrapper wrapper,
                                        @RequestParam(name="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                        @RequestParam(name="endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                        Principal principal) {
        Exhibition exhibitionToUpdate = wrapper.getExhibition();
        if (!exhibitionToUpdate.getLink().startsWith("http")) {
            exhibitionToUpdate.setLink("https://" + exhibitionToUpdate.getLink());
        }
        if (startDate != null) {
            exhibitionToUpdate.setStartDate(startDate);
        }
        if (endDate != null) {
            exhibitionToUpdate.setEndDate(endDate);
        }
        exhibitionToUpdate.setCurrentFuture(isCurrentOrFuture(exhibitionToUpdate));
        exhibitionRepo.save(exhibitionToUpdate);
        return new ModelAndView(new RedirectView(fromMappingName("show_exhibition_detail").arg(0, exhibitionToUpdate.getId()).arg(1, principal).build()));
    }

    @PostMapping(path="/painting/delete/{paintingId}/{exhibitionId}", name="delete_exhibitionPainting")
    public ModelAndView deleteExhibitionPainting(@PathVariable(value = "paintingId") Integer paintingId,
                                                 @PathVariable(value = "exhibitionId") String exhibitionId,
                                                 Principal principal) {
        exhibitionService.deletePainting(paintingId);
        return new ModelAndView(new RedirectView(fromMappingName("edit_exhibitions").arg(0, exhibitionId).arg(1, principal).build()));
    }

    @PostMapping(path="/painting/add/{exhibitionId}", name="add_exhibitionPaintings")
    public ModelAndView addExhibitionPaintings(@PathVariable(value = "exhibitionId") String exhibitionId,
                                               @RequestParam(name = "new-paintings") MultipartFile[] newPaintings,
                                               Principal principal) {
        // ToDo: Arbeite mit flash messages irgendwann
        exhibitionService.saveNewPaintingsForExhibition(exhibitionId, newPaintings);
        return new ModelAndView(new RedirectView(fromMappingName("edit_exhibitions").arg(0, exhibitionId).arg(1, principal).build()));
    }

    @PostMapping(path="/poster/delete/{posterId}/{exhibitionId}", name="delete_exhibitionPainting")
    public ModelAndView deleteExhibitionPoster(@PathVariable(value = "posterId") Integer posterId,
                                               @PathVariable(value = "exhibitionId") String exhibitionId,
                                               Principal principal) {
        exhibitionService.deletePoster(posterId);
        return new ModelAndView(new RedirectView(fromMappingName("edit_exhibitions").arg(0, exhibitionId).arg(1, principal).build()));
    }

    @PostMapping(path="/poster/add/{exhibitionId}", name="add_exhibitionPaintings")
    public ModelAndView addExhibitionPoster(@PathVariable(value = "exhibitionId") String exhibitionId,
                                            @RequestParam(value = "new-poster") MultipartFile newPoster,
                                            Principal principal) {
        // ToDo: Arbeite mit flash messages irgendwann
        exhibitionService.saveNewPosterForExhibition(exhibitionId, newPoster);
        return new ModelAndView(new RedirectView(fromMappingName("edit_exhibitions").arg(0, exhibitionId).arg(1, principal).build()));
    }

    @PostMapping(path="/delete/{id}", name="delete_exhibition")
    public ModelAndView deleteExhibition(@PathVariable(value = "id") String id,
                                         Principal principal) {
        exhibitionRepo.deleteById(id);
        return new ModelAndView(new RedirectView(fromMappingName("exhibition").arg(0, principal).build()));
    }
}
