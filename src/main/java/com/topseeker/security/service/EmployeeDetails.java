//package com.topseeker.security.service;
//
//import com.topseeker.employee.model.EmployeeVO;
//import com.topseeker.employee.model.EmployeeRepository;
//import com.topseeker.employee.model.EmployeeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class EmployeeDetails implements UserDetailsService {
//
//	@Autowired
//    private EmployeeRepository empRepository;
//
//	@Autowired
//    private EmployeeService empService;
//	
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public EmployeeDetails(EmployeeRepository empRepository, EmployeeService empService) {
//        this.empRepository = empRepository;
//        this.empService = empService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String empAccount) throws UsernameNotFoundException {
//        EmployeeVO employeeVO = empRepository.findByEmpAccount(empAccount);
//        if (employeeVO == null) {
//            throw new UsernameNotFoundException("User not found with username: " + empAccount);
//        }
//        // Load user roles (authorities)
//        List<Integer> authNoList = empRepository.findAuthNoByEmpNo(employeeVO.getEmpNo());
//        List<GrantedAuthority> authorities = authNoList.stream()
//                .map(authNo -> new SimpleGrantedAuthority("ROLE_" + authNo))
//                .collect(Collectors.toList());
//
//        return new User(employeeVO.getEmpAccount(), employeeVO.getEmpPassword(), authorities);
//    }
//}
