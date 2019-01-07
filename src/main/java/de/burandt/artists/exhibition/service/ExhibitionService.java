package de.burandt.artists.exhibition.service;

import de.burandt.artists.exhibition.domain.Exhibition;
import de.burandt.artists.exhibition.domain.ExhibitionPainting;
import de.burandt.artists.exhibition.domain.ExhibitionPoster;
import de.burandt.artists.exhibition.repository.ExhibitionPaintingRepository;
import de.burandt.artists.exhibition.repository.ExhibitionPosterRepository;
import de.burandt.artists.exhibition.repository.ExhibitionRepository;
import de.burandt.artists.painting.config.PaintingFileProperties;
import de.burandt.artists.painting.service.PaintingFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExhibitionService {

    private static final Logger LOG = LoggerFactory.getLogger(ExhibitionService.class);

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionPaintingRepository exhibitionPaintingRepository;
    private final ExhibitionPosterRepository exhibitionPosterRepository;
    private final PaintingFileProperties fileProperties;

    @Autowired
    public ExhibitionService(ExhibitionRepository exhibitionRepository,
                             ExhibitionPaintingRepository exhibitionPaintingRepository,
                             ExhibitionPosterRepository exhibitionPosterRepository,
                             PaintingFileProperties fileProperties) {
        this.exhibitionRepository = exhibitionRepository;
        this.exhibitionPaintingRepository = exhibitionPaintingRepository;
        this.exhibitionPosterRepository = exhibitionPosterRepository;
        this.fileProperties = fileProperties;

    }

    public boolean saveNewExhibition(Date startDate, Date endDate,
                                     String title, String exhibitor, String location, String openingTimes,
                                     String vernissage, String link, String description, MultipartFile[] pictures,
                                     MultipartFile poster) {
        if (pictures == null || Arrays.asList(pictures).isEmpty()) {
            return false;
        }
        Exhibition newExhibition = new Exhibition(startDate, endDate, title, exhibitor, location, openingTimes, vernissage, link, description);
        final Exhibition savedNewExhibition = exhibitionRepository.save(newExhibition);
        List<ExhibitionPainting> paintings = Stream.of(pictures)
            .filter(image -> !image.getOriginalFilename().equals(""))
            .map(image -> {
            saveExhibitionPaintingFileOnDisc(image);
            return new ExhibitionPainting(image.getOriginalFilename(), savedNewExhibition);
        }).collect(Collectors.toList());

        exhibitionPaintingRepository.saveAll(paintings);
        saveExhibitionPaintingFileOnDisc(poster);
        if (!poster.getOriginalFilename().equals("")) {
            exhibitionPosterRepository.save(new ExhibitionPoster(savedNewExhibition, poster.getOriginalFilename()));
        }
        return true;
    }

    private void saveExhibitionPaintingFileOnDisc(MultipartFile image) {
        try {
            image.transferTo(new File(fileProperties.getExhibition() + image.getOriginalFilename()));
        } catch (IOException e) {
            LOG.info("Something went wrong while saving exhibition painting files", e);
        }
    }

    public boolean saveNewPaintingsForExhibition(String exhibitionId, MultipartFile[] images) {
        if(images == null || images.length == 0) {
            return false;
        }
        List<ExhibitionPainting> paintings = Stream.of(images).map(image -> {
            saveExhibitionPaintingFileOnDisc(image);
            return new ExhibitionPainting(image.getOriginalFilename(),
                exhibitionRepository.findById(exhibitionId).orElseThrow(EntityNotFoundException::new));

        }).collect(Collectors.toList());

        exhibitionPaintingRepository.saveAll(paintings);
        return true;
    }

    public boolean saveNewPosterForExhibition(String exhibitionId, MultipartFile image) {
        if(image == null) {
            return false;
        }
        saveExhibitionPaintingFileOnDisc(image);
        ExhibitionPoster poster = new ExhibitionPoster(
            exhibitionRepository.findById(exhibitionId).orElseThrow(EntityNotFoundException::new),
            image.getOriginalFilename());

        exhibitionPosterRepository.save(poster);
        return true;
    }

    public void deletePainting(Integer id) {
        ExhibitionPainting painting = exhibitionPaintingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        deleteFileFromDisc(painting.getDatei());
        exhibitionPaintingRepository.deleteById(id);
    }

    public void deletePoster(Integer id) {
        ExhibitionPoster poster = exhibitionPosterRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        deleteFileFromDisc(poster.getFilename());
        exhibitionPosterRepository.deleteById(id);
    }

    private boolean deleteFileFromDisc(String filename) {
        File paintingFile = new File(fileProperties.getExhibition() + filename);
        try {
            PaintingFileService.checkPaintingFileExists(paintingFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return paintingFile.delete();
    }
}
