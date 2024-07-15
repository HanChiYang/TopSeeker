// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.newspic.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.actpicture.model.ActPictureVO;
//<EmpVO, Integer>，<表格, 主鍵型別>
public interface NewsPicRepository extends JpaRepository<NewsPicVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from news_pic where news_img_no =?1", nativeQuery = true)
	void deleteByNewsImgNo(int newsImgNo);
	
	//拿第一張圖片
	@Query(value = "SELECT * FROM news_pic WHERE news_no=?1 limit 1", nativeQuery = true)
	NewsPicVO getFirstNewsPic(Integer newsNo);

	
	//拿取單一商品的全部圖片
	@Query(value = "SELECT * FROM news_pic WHERE news_img_no=?1", nativeQuery = true)
	List<NewsPicVO> getAllNewsPic(Integer newsImgNo);


	//● (自訂)條件查詢
//	@Query(value = "from EmpVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<ActVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
}