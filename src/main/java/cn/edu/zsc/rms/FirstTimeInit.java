package cn.edu.zsc.rms;

import cn.edu.zsc.rms.domain.Authority;
import cn.edu.zsc.rms.domain.Department;
import cn.edu.zsc.rms.domain.Role;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.repository.DepartmentRepository;
import cn.edu.zsc.rms.repository.RoleRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hsj
 */
@Component
public class FirstTimeInit implements CommandLineRunner {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private DepartmentRepository departmentRepo;

    private PasswordEncoder passwordEncoder;

    public FirstTimeInit(UserRepository userRepo, RoleRepository roleRepo, DepartmentRepository departmentRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.departmentRepo = departmentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Department department = new Department("9527", "other", "other", Department.DeptType.COLLEGE,  null, LocalDateTime.now(), UUID.randomUUID());
        Optional<Department> optionalDepartment = departmentRepo.findByNumber(department.getNumber());
        if(!optionalDepartment.isPresent()) {
                departmentRepo.save(department);
        }else {
            department = optionalDepartment.get();
        }
        Set<Authority> authorities = new LinkedHashSet<>(Arrays.stream(Authority.values()).collect(Collectors.toSet()));
        Set<Role> roles = new LinkedHashSet<>();
        Role role = new Role("superman", Role.RoleType.CUSTOM,authorities, "", Role.DataScope.SCHOOL, LocalDateTime.now(), UUID.randomUUID());
        Optional<Role> optionalRole = roleRepo.findByName(role.getName());
        if(!optionalRole.isPresent()){
            roleRepo.save(role);
        }else {
            role = optionalRole.get();
        }
        roles.add(role);
        User user = new User(
                "007",
                passwordEncoder.encode("007"),
                roles,
                "superman",
                "13435589791",
                "670547309@qq.com",
                null,
                User.UserType.NON_STAFF,
                User.UserStatus.ENABLED,
                department,
                LocalDateTime.now(),
                UUID.randomUUID()
               );
        if(!userRepo.findByNumber(user.getNumber()).isPresent()){
            userRepo.save(user);
        }


    }
}
