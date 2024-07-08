/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package hibernate.util.CompositeQuery;

//import hibernate.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.topseeker.shop.product.model.ShopProductVO;
import com.topseeker.shop.producttype.model.ShopProductTypeVO;

public class HibernateUtil_CompositeQuery_ShopProduct {

	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<ShopProductVO> root, String columnName, String value) {

		Predicate predicate = null;

		if ("prodTypeNo".equals(columnName)) { // 用於Integer
			ShopProductTypeVO shopProductTypeVO = new ShopProductTypeVO();
			shopProductTypeVO.setProdTypeNo(Integer.valueOf(value));
			predicate = builder.equal(root.get("shopProductTypeVO"), shopProductTypeVO);
			}
		else if ("prodName".equals(columnName))
			predicate = builder.like(root.get(columnName), "%" + value + "%");
		
		else if ("prodInfo".equals(columnName))
			predicate = builder.like(root.get(columnName), "%" + value + "%");
		
		else if ("prodStatus".equals(columnName)) // 用於Integer
			predicate = builder.equal(root.get(columnName), Integer.valueOf(value));

		return predicate;
	}

	@SuppressWarnings("unchecked")
	public static List<ShopProductVO> getAllC(Map<String, String[]> map, Session session) {

//		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		List<ShopProductVO> list = null;
		try {
			// 【●創建 CriteriaBuilder】
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// 【●創建 CriteriaQuery】
			CriteriaQuery<ShopProductVO> criteriaQuery = builder.createQuery(ShopProductVO.class);
			// 【●創建 Root】
			Root<ShopProductVO> root = criteriaQuery.from(ShopProductVO.class);

			List<Predicate> predicateList = new ArrayList<Predicate>();
			
			Set<String> keys = map.keySet();
			int count = 0;
			for (String key : keys) {
				String value = map.get(key)[0];
				if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
					count++;
					predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
					System.out.println("有送出查詢資料的欄位數count = " + count);
				}
			}
			System.out.println("predicateList.size()="+predicateList.size());
			criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
			criteriaQuery.orderBy(builder.asc(root.get("prodNo")));
			// 【●最後完成創建 javax.persistence.Query●】
			Query query = session.createQuery(criteriaQuery); //javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面
			list = query.getResultList();

			tx.commit();
		} catch (RuntimeException ex) {
			if (tx != null)
				tx.rollback();
			throw ex; // System.out.println(ex.getMessage());
		} finally {
			session.close();
			// HibernateUtil.getSessionFactory().close();
		}

		return list;
	}
	
	
	
}
