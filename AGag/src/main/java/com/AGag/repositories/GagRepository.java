package com.AGag.repositories;

import com.AGag.entities.Gag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GagRepository extends JpaRepository<Gag, Integer> {
    Gag findById(int id);
    Gag findByName(String name);
}
