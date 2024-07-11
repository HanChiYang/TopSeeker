package hibernate.util.CompositeQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.topseeker.member.model.MemberVO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HibernateUtil_CompositeQuery_Mem {

    public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder, Root<MemberVO> root, String columnName, String value) {
        Predicate predicate = null;

        if ("memName".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("memAccount".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("memEmail".equals(columnName))
            predicate = builder.like(root.get(columnName), "%" + value + "%");
        else if ("memStatus".equals(columnName))
            predicate = builder.equal(root.get(columnName), Byte.valueOf(value));
        else if ("memDateRange".equals(columnName)) {
            // 處理日期區間查詢
            String[] dateRange = value.split(",");
            if (dateRange.length == 2) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = sdf.parse(dateRange[0]);
                    Date endDate = sdf.parse(dateRange[1]);
                    predicate = builder.between(root.get("memBirthday"), startDate, endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return predicate;
    }

    @SuppressWarnings("unchecked")
    public static List<MemberVO> getAllC(Map<String, String[]> map, Session session) {
        Transaction tx = session.beginTransaction();
        List<MemberVO> list = null;
        try {
            // 【●創建 CriteriaBuilder】
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // 【●創建 CriteriaQuery】
            CriteriaQuery<MemberVO> criteriaQuery = builder.createQuery(MemberVO.class);
            // 【●創建 Root】
            Root<MemberVO> root = criteriaQuery.from(MemberVO.class);

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
            System.out.println("predicateList.size()=" + predicateList.size());
            criteriaQuery.where(predicateList.toArray(new Predicate[0]));
            criteriaQuery.orderBy(builder.asc(root.get("memNo")));
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
