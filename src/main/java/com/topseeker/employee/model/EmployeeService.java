package com.topseeker.employee.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topseeker.authority.model.AuthorityRepository;
import com.topseeker.authority.model.AuthorityVO;
import com.topseeker.empauth.model.EmpAuthRepository;
import com.topseeker.empauth.model.EmpAuthVO;
import com.topseeker.employee.model.EmployeeVO;
import com.topseeker.employee.model.EmployeeRepository;

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emps;


@Service("EmployeeService")
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private EmpAuthRepository empAuthRepository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addEmp(EmployeeVO employeeVO) {
		employeeRepository.save(employeeVO);
	}

	public void updateEmp(EmployeeVO employeeVO) {
		employeeRepository.save(employeeVO);
	}

	public void deleteEmp(Integer empNo) {
		if (employeeRepository.existsById(empNo))
			employeeRepository.deleteByEmpNo(empNo);
//		    repository.deleteById(empno);
	}

	public EmployeeVO getOneEmp(Integer empNo) {
		Optional<EmployeeVO> optional = employeeRepository.findById(empNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<EmployeeVO> getAll() {
		return employeeRepository.findAll();
	}

//	public List<EmployeeVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_Emps.getAllC(map,sessionFactory.openSession());
//	}

	//以下測試
	
    public void addEmployeeWithAuthorities(EmployeeVO employeeVO, List<Integer> authNoList) {
    	//接收到emplyeeVO，更新資料庫
    	employeeRepository.save(employeeVO);
        
        //將權限新增於員工表格中
        Set<EmpAuthVO> empAuths = new HashSet<>();
        for (Integer authNo : authNoList) {
            AuthorityVO authority = authorityRepository.findById(authNo).orElseThrow();
            EmpAuthVO empAuth = new EmpAuthVO();
            empAuth.setEmployeeVO(employeeVO);
            empAuth.setAuthorityVO(authority);
            empAuths.add(empAuth);
        }
        empAuthRepository.saveAll(empAuths);
    }
	
    public void updateEmployeeWithAuthorities(EmployeeVO employeeVO, List<Integer> authNoList) {
    	//接收到emplyeeVO，更新資料庫
    	employeeRepository.save(employeeVO);

        //去除員工原本就有的權限
        empAuthRepository.deleteByEmployeeVO(employeeVO);

        //將權限新增於員工表格中，此段與新增的意義相同，只是用匿名函式寫出
        List<EmpAuthVO> empAuthVOs = authNoList.stream().map(authNo -> {
            EmpAuthVO empAuthVO = new EmpAuthVO();
            empAuthVO.setEmployeeVO(employeeVO);
            empAuthVO.setAuthorityVO(authorityRepository.findById(authNo).orElseThrow());
            return empAuthVO;
        }).collect(Collectors.toList());

        empAuthRepository.saveAll(empAuthVOs);
    }

    public EmployeeVO findById(Integer empNo) {
        return employeeRepository.findById(empNo).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

	public Optional<EmployeeVO> empLogin(String empAccount, String empPassword) {
		
		return employeeRepository.empLogin(empAccount, empPassword);
	}

}