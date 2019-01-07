package about.it;

import de.burandt.artists.Application;
import de.burandt.artists.about.domain.About;
import de.burandt.artists.about.repository.AboutRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AboutRepositoryIntegrationTest {

    @Autowired
    private AboutRepository aboutRepository;

    @Test
    public void should_save_and_retrieve() {
        About about = new About();
        about.setTitle("Ich");
        about.setText("bin leidenschaftlicher Basketballspieler, Softwareentwickler, Zocker und bin sehr interessiert an polititschen Themen.");

        aboutRepository.save(about);

        List<About> result = aboutRepository.findAll();
        assertThat(result).hasSize(1);
    }
}
