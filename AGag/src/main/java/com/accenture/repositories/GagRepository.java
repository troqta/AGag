package com.accenture.repositories;

import com.accenture.entities.Gag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GagRepository extends JpaRepository<Gag, Integer> {
}
