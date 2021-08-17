package com.megait.mymall.repository;

import com.megait.mymall.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Album, Long> {
}
