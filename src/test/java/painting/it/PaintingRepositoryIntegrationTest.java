package painting.it;

import de.burandt.artists.Application;
import de.burandt.artists.painting.domain.Hauptkategorie;
import de.burandt.artists.painting.domain.Painting;
import de.burandt.artists.painting.repository.PaintingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PaintingRepositoryIntegrationTest {

    @Autowired
    private PaintingRepository paintingRepository;

    @Test
    public void should_persist_and_retrieve_painting() {
        Painting painting = new Painting("Test", 2015, 32.5, 42.5, "Öl/Tempera", "test.jpg", "abstract", null, false);
        paintingRepository.save(painting);

        List<Painting> result = paintingRepository.findAll();
        assertThat(result).hasSize(1);
        assert result.get(0).getDatei().equals("test.jpg");
        assert result.get(0).getEntstehungsjahr().equals(2015);

    }
}
