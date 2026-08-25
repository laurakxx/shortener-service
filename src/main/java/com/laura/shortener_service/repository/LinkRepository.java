package com.laura.shortener_service.repository;

import com.laura.shortener_service.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {
  Optional<Link> findByShortCode(String shortCode);
}
