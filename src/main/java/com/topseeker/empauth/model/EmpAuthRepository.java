// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.topseeker.empauth.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.topseeker.employee.model.EmployeeVO;

	@Transactional
public interface EmpAuthRepository extends JpaRepository<EmpAuthVO, Integer> {

	@Modifying
	@Query(value = "delete from emp_auth where emp_no =?1", nativeQuery = true)
	void deleteEmpAuth(Integer empNo);
	
	@Modifying
	@Query(value = "INSERT INTO emp_auth (emp_no, auth_no) VALUES (?1, ?2)", nativeQuery = true)
	void addEmpAuth(Integer empNo, Integer authNo);


//	● (自訂)條件查詢
	@Query(value = "from EmpAuthVO where empNo=?1")
	List<EmpAuthVO> takeOneEmpAuth(Integer empNo);
	
	@Modifying
    void deleteByEmployeeVO(EmployeeVO employeeVO);
}