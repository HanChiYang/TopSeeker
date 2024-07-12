package com.topseeker.article.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface ArticleRepository extends JpaRepository<ArticleVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from article where art_no =?1", nativeQuery = true)
	void deleteByActMsgNo(int actMsgNo);

	//● (自訂)條件查詢
	@Query(value = "from ArticleVO where art_no=?1 and art_title like?2 order by art_no")
	List<ArticleVO> findByOthers(int artNo , int artTitle);
	
	List<ArticleVO> findByArtStatusNot(Integer artStatus);


}
