package com.laura.shortener_service.repository;

import com.laura.shortener_service.entity.Link;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {
  Optional<Link> findByShortCode(String shortCode);

  @Modifying
  @Query("""
      update Link l 
      set l.clicks = l.clicks+1
      where l.shortCode = :shortCode
      """)
  int incrementClicks(@Param("shortCode") String shortCode);
}
