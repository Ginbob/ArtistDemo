package index.it;

import de.burandt.artists.Application;
import de.burandt.artists.index.domain.Index;
import de.burandt.artists.index.repository.IndexRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class IndexRepositoryIntegrationTest {

    @Autowired
    private IndexRepository indexRepository;

    @Test
    public void should_save_and_retrieve_index() {
        Index index = new Index();
        index.setParagraph("Das ist ein Test für Louis Website.");
        index.setHeadline("Test Titel für Louis Website");
        index.setImageFileName("louis.jpg");

        indexRepository.save(index);

        List<Index> result = indexRepository.findAll();
        Index retrieved = result.get(0);
        assert retrieved.getImageFileName().equals(index.getImageFileName());
        assert retrieved.getHeadline().equals(index.getHeadline());
        assert retrieved.getParagraph().equals(index.getParagraph());
    }
}
