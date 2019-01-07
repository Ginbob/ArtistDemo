package de.burandt.artists.painting.service;

import de.burandt.artists.painting.config.PaintingFileProperties;
import de.burandt.artists.painting.domain.Hauptkategorie;
import de.burandt.artists.painting.exception.CannotResolvePaintingException;
import de.burandt.artists.painting.exception.InvalidCategoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static de.burandt.artists.painting.domain.Hauptkategorie.*;

@Service
public class PaintingFileService {

    private static final Logger LOG = LoggerFactory.getLogger(PaintingFileService.class);

    private PaintingFileProperties properties;

    @Autowired
    public PaintingFileService(PaintingFileProperties paintingFileProperties) {
        this.properties = paintingFileProperties;
    }

    public byte[] mapFileNameToByteArray(Hauptkategorie kategorie, String filename) throws IOException, CannotResolvePaintingException {
        File image = null;
        if (kategorie.equals(ABSTRACT)) {
            image = new File(properties.getAbstractpath() + filename);
        } else if (kategorie.equals(REPRESENTATIONAL)) {
            image = new File(properties.getRepresentational() + filename);
        } else if (kategorie.equals(EXHIBITION)) {
            image = new File(properties.getExhibition() + filename);
        } else if (kategorie.equals(CURRENT)) {
            image = new File(properties.getCurrent() + filename);
        } else if (kategorie.equals(COLLAGE)) {
            image = new File(properties.getCollage() + filename);
        } else if (kategorie.equals(DRAWING)) {
            image = new File(properties.getDrawing() + filename);
        } else if (kategorie.equals(ABOUT)) {
            image = new File(properties.getAbout() + filename);
        } else if (kategorie.equals(INDEX)) {
            image = new File(properties.getIndex() + filename);
        }
        if(image == null) {
            throw new CannotResolvePaintingException("Invalid Category or filename");
        }
        return Files.readAllBytes(image.toPath());
    }

    public boolean deleteFile(String filename, Hauptkategorie kategorie) throws FileNotFoundException {
        File paintingFile;
        switch (kategorie) {
            case REPRESENTATIONAL:
                paintingFile = new File(properties.getRepresentational() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            case ABSTRACT:
                paintingFile = new File(properties.getAbstractpath() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            case CURRENT:
                paintingFile = new File(properties.getCurrent() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            case DRAWING:
                paintingFile = new File(properties.getDrawing() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            case COLLAGE:
                paintingFile = new File(properties.getCollage() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            case ABOUT:
                paintingFile = new File(properties.getAbout() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            case INDEX:
                paintingFile = new File(properties.getIndex() + filename);
                checkPaintingFileExists(paintingFile);
                return paintingFile.delete();
            default:
                LOG.warn("Ungültige oder noch nicht im Service eingepflegte Hauptkategorie: " + kategorie.name());
                return false;
        }
    }

    public static void checkPaintingFileExists(File paintingFile) throws FileNotFoundException {
        if (!paintingFile.exists()) {
            throw new FileNotFoundException("Datei '" + paintingFile.getAbsolutePath() + "' konnte nicht gefunden und damit nicht gelöscht werden!");
        }
    }

    public void save(MultipartFile paintingFile, String hauptkategorie) {

        try {
            paintingFile.transferTo(new File(properties.getPathForCategory(hauptkategorie) + paintingFile.getOriginalFilename()));
        } catch (IllegalStateException | IOException | InvalidCategoryException e) {
            LOG.error("Etwas ist schief gelaufen beim Speichern der Bild-Datei: ", e);
        }
    }

    public boolean exists(MultipartFile paintingFile, String hauptkategorie) {
        try {
            return new File(properties.getPathForCategory(hauptkategorie) + paintingFile.getOriginalFilename()).exists();
        } catch (InvalidCategoryException e) {
            LOG.error("Angegebene Hauptkategorie " + hauptkategorie + " exisitiert nicht");
            return true; //Bricht die Erfassung des Bildes ab
        }
    }
}
