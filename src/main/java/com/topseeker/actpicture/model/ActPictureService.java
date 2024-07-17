package com.topseeker.actpicture.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;








@Service("actPictureService")
public class ActPictureService {

	@Autowired
	ActPictureRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	//新增
	public void addActPicture(ActPictureVO actPictureVO) {
		repository.save(actPictureVO);
	}
	//修改
	public void updateActPicture(ActPictureVO actPictureVO) {
		repository.save(actPictureVO);
	}
	// 刪除
	public boolean deleteActPicture(Integer actPicNo) {
		if (repository.existsById(actPicNo)) {
			repository.deleteByActPicNo(actPicNo);			
		
		    return true;
		} else {			
			return false;
		}
	}
	// 取單一圖片
	public List<ActPictureVO> getOneActPicture(Integer actNo) {
		List<ActPictureVO> optional = repository.getActPictureVOList(actNo);
		if (optional.isEmpty())
			return null;

		return optional;
		// 有資料則回傳其值，沒有則回傳null
	}
	
	// 只取第一張圖片(測試用)
	public ActPictureVO getFirstActPicVO(Integer actNo) {
		return repository.getFirstActPicVO(actNo);
		// 有資料則回傳其值，沒有則回傳null
	}
	
	// 取單一活動全數圖片用
	public List<ActPictureVO> getAllActPic(Integer actPicNo) {
		return repository.getAllActPic(actPicNo);
	}
	
	// 取活動詳情全數圖片用
	public List<ActPictureVO> getDetailsActPic(Integer actNo) {
		return repository.getDetailsActPic(actNo);
	}
	
	// 取全數圖片
	public List<ActPictureVO> getAll() {
		return repository.findAll();
	}
	
	public List<ActPictureVO> getActPicVOList(Integer actNo) {
		return repository.getActPictureVOList(actNo);
	}

}