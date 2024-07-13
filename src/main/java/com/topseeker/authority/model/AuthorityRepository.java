// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.authority.model;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
@Transactional
public interface AuthorityRepository extends JpaRepository<AuthorityVO, Integer> {

}