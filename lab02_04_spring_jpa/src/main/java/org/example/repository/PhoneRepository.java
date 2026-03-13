package org.example.repository;

import org.example.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    @Query("select p from Phone p where p.number like :num ")
    List<Phone> getPhoneLike(@Param("num") String num);

    List<Phone> findByNumberLike(String num);
}