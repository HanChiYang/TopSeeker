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

import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.sale.model.SaleVO;

public class HibernateUtil_CompositeQuery_Order {
	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<OrderVO> root, String columnName, String value) {

		Predicate predicate = null;

		if ("orderNo".equals(columnName)||"orderStatus".equals(columnName)||"paymentStatus".equals(columnName)
				||"paymentMethod".equals(columnName)||"deliveryMethod".equals(columnName)) // 用於Integer
			predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//		else if ("orderName".equals(columnName)) // 用於varchar
//			predicate = builder.like(root.get(columnName), "%" + value + "%");
//		else if ("orderDate".equals(columnName)) // 用於date
//			predicate = builder.equal(root.get(columnName), java.sql.Timestamp.valueOf(value));
//		else if ("saleNo".equals(columnName)) {
//			SaleVO saleVO = new SaleVO();
//			saleVO.setSaleNo(Integer.valueOf(value));
//			predicate = builder.equal(root.get("saleVO"), saleVO);
		else if ("memNo".equals(columnName)) {
			MemberVO memberVO = new MemberVO();
			memberVO.setMemNo(Integer.valueOf(value));
			predicate = builder.equal(root.get("memberVO"), memberVO);
		}
		return predicate;
	}

	@SuppressWarnings("unchecked")
	public static List<OrderVO> getAllC(Map<String, String[]> map, Session session) {

		Transaction tx = session.beginTransaction();
		List<OrderVO> list = null;
		try {
			// 【●創建 CriteriaBuilder】
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// 【●創建 CriteriaQuery】
			CriteriaQuery<OrderVO> criteriaQuery = builder.createQuery(OrderVO.class);
			// 【●創建 Root】
			Root<OrderVO> root = criteriaQuery.from(OrderVO.class);

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
