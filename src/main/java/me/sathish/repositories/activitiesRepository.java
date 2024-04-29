package me.sathish.repositories;

import me.sathish.entities.Activities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface activitiesRepository extends JpaRepository<Activities, Long> {}
