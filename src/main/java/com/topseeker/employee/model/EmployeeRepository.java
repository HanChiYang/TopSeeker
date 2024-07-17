// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.employee.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

	@Transactional
	public interface EmployeeRepository extends JpaRepository<EmployeeVO, Integer> {

		@Modifying
		@Query(value = "delete from employee where emp_no =?1", nativeQuery = true)
		void deleteByEmpNo(int empNo);

		//員工登入
		@Query(value = "select * from employee where emp_account=?1 and emp_password =?2", nativeQuery = true)
	    Optional<EmployeeVO> empLogin(String empAccount, String memPassword);

		
		@Query(value = "select * from employee where emp_account=?1", nativeQuery = true)
		EmployeeVO findByEmpAccount(String empAccount);

		 @Query(value = "select auth_no from emp_auth where emp_no = ?1", nativeQuery = true)
		    List<Integer> findAuthNoByEmpNo(int empNo);
		
	
//	//● (自訂)條件查詢
//	@Query(value = "from EmpVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<EmpVO> findByOthers(int empno , String ename , java.sql.Date hiredate);
}