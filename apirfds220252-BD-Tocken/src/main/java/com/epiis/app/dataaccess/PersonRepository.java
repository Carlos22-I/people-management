package com.epiis.app.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epiis.app.entity.Person;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface PersonRepository extends JpaRepository<Person, String> {

    // 1. findById (ya existe por JpaRepository)
    // 2. getById (ya existe por JpaRepository)

    // 3. Paginación
    @NonNull
    Page<Person> findAll(@NonNull Pageable pageable);

    // 4. Containing
    List<Person> findByFirstNameContaining(String firstName);

    List<Person> findByFirstNameContainingOrSurNameContaining(String firstName, String surName);

    // 5. StartingWith
    List<Person> findBySurNameStartingWith(String surName);

    // 6. Between (fechas)
    List<Person> findByBirthDateBetween(Date start, Date end);

    // 7. Equals
    Optional<Person> findByDni(String dni);

    // 8. Exists
    boolean existsByDni(String dni);

    // 9. count() -> ya existe
    // 10. deleteById() -> ya existe
}
