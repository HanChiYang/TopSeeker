/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

//import hibernate.util.HibernateUtil;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.topseeker.act.model.ActVO;


public class HibernateUtil_CompositeQuery_Act {

	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<ActVO> root, String columnName, String value) {
        Predicate predicate = null;

        if ("actTitle".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("actPlace".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("actContent".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("actStatus".equals(columnName))
            predicate = builder.equal(root.get(columnName), Byte.valueOf(value));
        else if ("actDateRange".equals(columnName)) {
            // 處理日期區間查詢
            String[] dateRange = value.split(",");
            if (dateRange.length == 2) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = sdf.parse(dateRange[0]);
                    Date endDate = sdf.parse(dateRange[1]);
                    predicate = builder.between(root.get("actStart"), startDate, endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<ActVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<ActVO> list = null;
        try {
            // 【●創建 CriteriaBuilder】
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 【●創建 CriteriaQuery】
            CriteriaQuery<ActVO> criteriaQuery = builder.createQuery(ActVO.class);
            // 【●創建 Root】
            Root<ActVO> root = criteriaQuery.from(ActVO.class);

            List<Predicate> predicateList = new ArrayList<>();
            
         // 排除狀態為已取消的活動
            Predicate notCancelled = builder.notEqual(root.get("actStatus"), 3);
            predicateList.add(notCancelled);

   
            
            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key)[0];
                if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
                    Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, value.trim());
                    if (predicate != null) {
                        predicateList.add(predicate);
                    }
                }
            }
            System.out.println("predicateList.size()=" + predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
            //讓活動依活動編號降序排列
            criteriaQuery.orderBy(builder.desc(root.get("actNo")));
            // 【●最後完成創建 javax.persistence.Query●】
            Query query = session.createQuery(criteriaQuery);
            list = query.getResultList();

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
            throw ex;
        } finally {
            session.close();
        }

        return list;
    }	
}
