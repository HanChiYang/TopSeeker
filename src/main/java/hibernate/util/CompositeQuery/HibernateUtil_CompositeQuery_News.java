/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
//import hibernate.util.HibernateUtil;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面

import com.topseeker.act.model.ActVO;
import com.topseeker.news.model.NewsVO;

public class HibernateUtil_CompositeQuery_News {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<NewsVO> root, String columnName, String value) {

        Predicate predicate = null;

        if ("newsTitle".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");

        else if ("newsContent".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");

        else if ("startDate".equals(columnName) || "endDate".equals(columnName)) {
            // 日期範圍處理
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now();

            if ("startDate".equals(columnName)) {
                startDate = LocalDate.parse(value);
                predicate = builder.greaterThanOrEqualTo(root.get("newsPublishTime").as(LocalDate.class), startDate);
            } else if ("endDate".equals(columnName)) {
                endDate = LocalDate.parse(value);
                predicate = builder.lessThanOrEqualTo(root.get("newsPublishTime").as(LocalDate.class), endDate);
            }
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<NewsVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<NewsVO> list = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<NewsVO> criteriaQuery = builder.createQuery(NewsVO.class);
            Root<NewsVO> root = criteriaQuery.from(NewsVO.class);

            List<Predicate> predicateList = new ArrayList<>();

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
            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
            criteriaQuery.orderBy(builder.desc(root.get("newsNo")));
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
