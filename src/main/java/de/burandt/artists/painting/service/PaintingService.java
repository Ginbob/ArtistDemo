package de.burandt.artists.painting.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import de.burandt.artists.painting.exception.DuplicateFileNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.burandt.artists.painting.domain.Hauptkategorie;
import de.burandt.artists.painting.domain.Painting;
import de.burandt.artists.painting.repository.PaintingRepository;

@Service
public class PaintingService {

	private static final Logger LOG = LoggerFactory.getLogger(PaintingService.class);
	
	private final PaintingRepository paintingRepository;
	private final PaintingFileService paintingFileService;

	@Autowired
	public PaintingService(PaintingRepository paintingRepository,
						   PaintingFileService paintingFileService) {
		this.paintingRepository = paintingRepository;
		this.paintingFileService = paintingFileService;
	}
	
	public List<Painting> findAllPaintingsByCategory(Hauptkategorie hauptkategorie) {
		return paintingRepository.findByHauptkategorieOrderByEntstehungsjahrDesc(hauptkategorie.getHauptkategorie());
	}

	public boolean saveNewPainting(String paintingname,
								   String technique, 
								   String height, 
								   String width, 
								   String year, 
								   MultipartFile paintingFile, 
								   String hauptkategorie,
								   String unterkategorie) throws DuplicateFileNameException {
		if (paintingFileService.exists(paintingFile, hauptkategorie)) {
			throw new DuplicateFileNameException("File name already exists for this category");
		}
		paintingFileService.save(paintingFile, hauptkategorie);
		Double heightValue = Double.valueOf(height);
		Double widthValue = Double.valueOf(width);
		Integer yearValue = Integer.valueOf(year);
		Painting newPainting = new Painting(paintingname, yearValue, heightValue, widthValue, technique, paintingFile.getOriginalFilename(), hauptkategorie, unterkategorie, false);

		paintingRepository.save(newPainting);
		return true;
	}

    public void updatePaintings(List<Painting> paintings) {
        List<Painting> paintingsToDelete = paintings.stream().filter(painting -> painting.isMarkedAsDeleted()).collect(Collectors.toList());
        paintings.removeAll(paintingsToDelete);
        deletePaintings(paintingsToDelete);
	    paintingRepository.saveAll(paintings);
    }

    private void deletePaintings(List<Painting> paintingsToDelete) {
	    paintingsToDelete.forEach(painting -> {
	        try {
                paintingFileService.deleteFile(painting.getDatei(), Hauptkategorie.valueOf(painting.getHauptkategorie().toUpperCase()));
                paintingRepository.delete(painting);
            } catch (FileNotFoundException e) {
	            LOG.error("Datei zum Löschen konnte nicht gefunden werden!", e);
            }
        });
    }


}
