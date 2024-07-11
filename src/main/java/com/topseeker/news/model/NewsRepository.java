// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.news.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
//<EmpVO, Integer>，<表格, 主鍵型別>
public interface NewsRepository extends JpaRepository<NewsVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from news where news_no =?1", nativeQuery = true)
	void deleteByNewsNo(int newsNo);

	
}