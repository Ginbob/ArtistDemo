package de.burandt.artists.exhibition.repository;

import de.burandt.artists.exhibition.domain.ExhibitionPoster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionPosterRepository extends JpaRepository<ExhibitionPoster, Integer> {
}
