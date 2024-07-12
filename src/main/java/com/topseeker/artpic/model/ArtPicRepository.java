package com.topseeker.artpic.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.article.model.ArticleVO;


public interface ArtPicRepository extends JpaRepository<ArtPicVO, Integer> {

	@Transactional
    @Modifying
    @Query(value = "delete from art_pic where art_pic_no =?1", nativeQuery = true)
    void deleteByArtPicNo(int artPicNo);

    @Transactional
    @Modifying
    @Query(value = "delete from art_pic where art_no = ?1", nativeQuery = true)
    void deleteByArticleVO(ArticleVO articleVO);

    @Query(value = "from ArtPicVO where art_pic_no=?1 order by art_pic")
    List<ArtPicVO> findByOthers(int artPicNo);
	
	

}
