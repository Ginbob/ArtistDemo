package de.burandt.artists.security.service;

import org.springframework.data.jpa.repository.JpaRepository;

import de.burandt.artists.security.user.ArtistUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ArtistUser, String>{

}
