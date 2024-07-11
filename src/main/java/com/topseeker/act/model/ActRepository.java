package com.topseeker.act.model;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface ActRepository extends JpaRepository<ActVO, Integer> {

}
