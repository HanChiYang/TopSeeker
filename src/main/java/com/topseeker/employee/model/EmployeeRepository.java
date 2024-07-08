// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.employee.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

	@Transactional
public interface EmployeeRepository extends JpaRepository<EmployeeVO, Integer> {

	@Modifying
	@Query(value = "delete from employee where empno =?1", nativeQuery = true)
	void deleteByEmpNo(int empNo);

//	//● (自訂)條件查詢
//	@Query(value = "from EmpVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<EmpVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
}