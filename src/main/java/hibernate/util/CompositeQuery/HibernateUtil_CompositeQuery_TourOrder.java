package hibernate.util.CompositeQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.topseeker.tourGroup.model.TourGroupVO;
import com.topseeker.tourOrder.model.TourOrderVO;

public class HibernateUtil_CompositeQuery_TourOrder {

	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<TourOrderVO> root, String columnName, String value) {

		Predicate predicate = null;

		if ("orderNo".equals(columnName) || "memNo".equals(columnName) || "groupNo".equals(columnName) || "orderNums".equals(columnName) || "orderPrice".equals(columnName)) // 用於Integer
			predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//		else if ("sal".equals(columnName) || "comm".equals(columnName)) // 用於Double
//			predicate = builder.equal(root.get(columnName), Double.valueOf(value));
//		else if ("tourName".equals(columnName) ) // 用於varchar
//			predicate = builder.like(root.get(columnName), "%" + value + "%");
//		else if ("tourStatus".equals(columnName) ) // 用於varchar
//			predicate = builder.like(root.get(columnName),  value );
		else if ("orderDate".equals(columnName) || "departureDate".equals(columnName)) // 用於date
			predicate = builder.equal(root.get(columnName), java.sql.Date.valueOf(value));
		else if("orderPay".equals(columnName) || "orderStatus".equals(columnName))
			predicate = builder.equal(root.get(columnName), Byte.valueOf(value));
//		else if ("tourNo".equals(columnName)) {
//			TourVO tourVO = new TourVO();
//			tourVO.setTourNo(Integer.valueOf(value));
//			predicate = builder.equal(root.get("tourVO"), tourVO);
//		}

		return predicate;
	}
	
	@SuppressWarnings("unchecked")
	public static List<TourOrderVO> getAllC(Map<String, String[]> map, Session session) {

//		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		List<TourOrderVO> list = null;
		try {
			// 【●創建 CriteriaBuilder】
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// 【●創建 CriteriaQuery】
			CriteriaQuery<TourOrderVO> criteriaQuery = builder.createQuery(TourOrderVO.class);
			// 【●創建 Root】
			Root<TourOrderVO> root = criteriaQuery.from(TourOrderVO.class);

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
			criteriaQuery.orderBy(builder.asc(root.get("orderNo")));
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
