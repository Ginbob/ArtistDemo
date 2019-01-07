package de.burandt.artists.index.repository;

import de.burandt.artists.index.domain.Index;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexRepository extends JpaRepository<Index, Integer> {
}
