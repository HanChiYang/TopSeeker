// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.actpicture.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

//<EmpVO, Integer>，<表格, 主鍵型別>
public interface ActPictureRepository extends JpaRepository<ActPictureVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from act_picture where act_pic_no =?1", nativeQuery = true)
	void deleteByActPicNo(int actPicNo);

	@Query(value = "from ActPictureVO where act_No= ?1")
	List<ActPictureVO> getActPictureVOList(Integer prodNo);
	
	
	//拿第一張圖片
	@Query(value = "SELECT * FROM act_picture WHERE act_no=?1 limit 1", nativeQuery = true)
	ActPictureVO getFirstActPicVO(Integer actNo);
	
	//拿取單一活動的全部圖片
	@Query(value = "SELECT * FROM act_picture WHERE act_pic_no=?1", nativeQuery = true)
	List<ActPictureVO> getAllActPic(Integer actPicNo);
	
	//修改 getAllActPic 方法，使其根據 act_no 來查找圖片
	@Query(value = "SELECT * FROM act_picture WHERE act_no = ?1", nativeQuery = true)
	List<ActPictureVO> getDetailsActPic(Integer actNo);
	
}