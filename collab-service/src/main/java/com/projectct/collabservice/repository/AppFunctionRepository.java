package com.projectct.collabservice.repository;

import com.projectct.collabservice.model.AppFunction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppFunctionRepository extends JpaRepository<AppFunction, Long> {
}