package de.burandt.artists.about.repository;

import de.burandt.artists.about.domain.About;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutRepository extends JpaRepository<About, Integer> {

}
