package com.topseeker.shop.cart.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_cart WHERE mem_no =?1" , nativeQuery = true)
	void deleteByMemNo(Integer memNo);
	
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM shop_cart WHERE mem_no =?1 AND prod_no =?2" , nativeQuery = true)
	void deleteByMemNoAndProdNo(Integer memNo, Integer prodNo);
	
	List<CartVO> findByMemberVO_MemNo(Integer memNo);
	
	
	@Query(value = "SELECT new com.topseeker.shop.cart.model.CartDetail(c.cartNo, c.memberVO.memNo, c.shopProductVO.prodNo, p.prodName, p.prodPrice, c.prodQty, (p.prodPrice * c.prodQty)) " +
            "FROM CartVO c " +
            "JOIN c.shopProductVO p " +
            "WHERE c.memberVO.memNo IN :memNos " +
            "AND c.shopProductVO.prodNo IN :prodNos")
	List<CartDetail> findCartDetail(Set<Integer> memNos, Set<Integer> prodNos);

	CartVO findByMemberVO_MemNoAndShopProductVO_ProdNo(Integer memNo, Integer prodNo);
	
	CartVO findByCartNo(Integer cartNo);
	
	@Modifying
	@Query(value = "update CartVO c set c.prodQty =?1 where c.cartNo =?2")
	void updateQty(Integer prodQty, Integer cartNo);

	
	
	
}
