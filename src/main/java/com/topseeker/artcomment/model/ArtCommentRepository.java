package com.topseeker.artcomment.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.article.model.ArticleVO;

public interface ArtCommentRepository extends JpaRepository<ArtCommentVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from art_comment where comment_no =?1", nativeQuery = true)
	void deleteByCommentNo(int commentNo);

	//● (自訂)條件查詢
	@Query(value = "from ArtCommentVO where comment_no=?1 and comment_content like?2 order by comment_no")
	List<ArtCommentVO> findByOthers(int commentNo , int commentContent);


}
