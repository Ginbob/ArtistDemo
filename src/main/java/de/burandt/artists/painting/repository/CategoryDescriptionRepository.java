package de.burandt.artists.painting.repository;

import de.burandt.artists.painting.domain.CategoryDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDescriptionRepository extends JpaRepository<CategoryDescription, String> {
}
