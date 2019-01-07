package exhibition.it;

import de.burandt.artists.Application;
import de.burandt.artists.exhibition.domain.Exhibition;
import de.burandt.artists.exhibition.repository.ExhibitionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ExhibitionRepositoryIntegrationTest {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Test
    public void should_persist_new_exhibition() {
        Calendar start = Calendar.getInstance();
        start.set(2017, 12, 1);
        Calendar end = Calendar.getInstance();
        start.set(2018, 11, 30);
        Exhibition exhibition = new Exhibition(
                start.getTime(),
                end.getTime(),
                "Testausstellung",
                "Testaussteller",
                "TestOrt",
                "Testzeit",
                "Vernissage, 19 Uhr",
                "www.test.de",
                "Testbeschreibung zur Testausstellung");
        exhibitionRepository.save(exhibition);
        List<Exhibition> result = exhibitionRepository.findAll();

        assertThat(result).hasSize(1);
        assert result.get(0).getDescription().equals("Testbeschreibung zur Testausstellung");

        exhibitionRepository.deleteAll();
    }

    @Test
    public void should_separate_between_past_and_currentOrFuture_exhibition() {
        Calendar start1 = Calendar.getInstance();
        start1.set(2017, 12, 1);
        Calendar end1 = Calendar.getInstance();
        end1.set(2017, 12, 31);
        Exhibition exhibitionPast = new Exhibition(
                start1.getTime(),
                end1.getTime(),
                "Ausstellung in Vergangenheit",
                "Testaussteller",
                "TestOrt",
                "Testzeit",
                "Vernissage, 19 Uhr",
                "www.test.de",
                "Testbeschreibung zur Ausstellung in der Vergangenheit");

        Calendar start2 = Calendar.getInstance();
        start2.set(2017, 12, 1);
        Calendar end2 = Calendar.getInstance();
        end2.set(2100, 12, 31);
        Exhibition exhibitionCurrentFuture = new Exhibition(
                start2.getTime(),
                end2.getTime(),
                "Aktuelle oder künftige Ausstellung",
                "Testaussteller",
                "TestOrt",
                "Testzeit",
                "Vernissage, 19 Uhr",
                "www.testausstellung.de",
                "Testbeschreibung zur aktuellen bzw. zukünftigen Ausstellung");

        exhibitionRepository.save(exhibitionPast);
        exhibitionRepository.save(exhibitionCurrentFuture);
        List<Exhibition> resultPast = exhibitionRepository.findAllByCurrentFutureOrderByStartDateAsc(false);
        List<Exhibition> resultCurrentFuture = exhibitionRepository.findAllByCurrentFutureOrderByStartDateAsc(true);

        assertThat(resultPast).hasSize(1);
        assertThat(resultCurrentFuture).hasSize(1);

        assert resultCurrentFuture.get(0).getDescription().equals("Testbeschreibung zur aktuellen bzw. zukünftigen Ausstellung");
        assert resultPast.get(0).getDescription().equals("Testbeschreibung zur Ausstellung in der Vergangenheit");

        exhibitionRepository.deleteAll();
    }
}
