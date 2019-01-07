package de.burandt.artists.exhibition.repository;

import de.burandt.artists.exhibition.domain.ExhibitionPainting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionPaintingRepository extends JpaRepository<ExhibitionPainting, Integer> {
}
