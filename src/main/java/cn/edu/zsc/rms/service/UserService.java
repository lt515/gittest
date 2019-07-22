package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.Department;
import cn.edu.zsc.rms.domain.Role;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.excel.ExcelUtil;
import cn.edu.zsc.rms.excel.model.ExcelUserModel;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.*;
import cn.edu.zsc.rms.repository.DepartmentRepository;
import cn.edu.zsc.rms.repository.RoleRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hsj
 */
@Service
public class UserService {
    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private DepartmentRepository departmentRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, DepartmentRepository departmentRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.departmentRepo = departmentRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> query(String id, String name, String phone, String email, Set<UUID> roles,
                            UUID departmentId, User.UserType type, User.UserStatus status, Pageable pageable
    ) {
        return userRepo.query(id, name, phone, email, roles, departmentId, type, status, pageable);
    }

    public User findById(UUID userId) {
        return userRepo.findById(userId).orElseThrow(() -> new EntityNotExistException(User.class));
    }

    public User findByNumber(String number) {
        return userRepo.findByNumber(number).orElseThrow(() -> new EntityNotExistException(User.class));
    }

    public List<User> list() {
        return userRepo.findAll();
    }

    public User create(String number, String password, String name, String phone, String email, String avatarUrl,
                       Set<UUID> roles, UUID departmentId, User.UserStatus status
    ) {
        checkUniquenessConstraint(number, phone, email);
        Set<Role> roleSet = roles.stream().map(this::mapRole).collect(Collectors.toSet());
        Department department = mapDepartment(departmentId);
        User user = new User(number, password, roleSet, name, phone, email, avatarUrl, User.UserType.NON_STAFF, status, department, LocalDateTime.now(), UUID.randomUUID());
        return userRepo.save(user);
    }

    public User update(UUID userId, String name, String phone, String email,
                       String avatarUrl, Set<UUID> roles, UUID departmentId, User.UserStatus status
    ) {
        User user = findById(userId);
        checkUniquenessConstraint(user, user.getNumber(), phone, email);
        checkImmutableConstraint(user, name, departmentId);
        if (name != null) {
            user.setName(name);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }
        if (roles != null) {
            Set<Role> roleSet = roles.stream().map(this::mapRole).collect(Collectors.toSet());
            user.setRoles(roleSet);
        }
        if (departmentId != null) {
            user.setDepartment(mapDepartment(departmentId));
        }
        if (status != null) {
            user.setStatus(status);
        }
        user.setUpdateTime(LocalDateTime.now());
        return userRepo.save(user);
    }

    public User setPassword(UUID userId, String oldPassword, String password) {
        if (null == password || password.isEmpty()) {
            throw new PasswordValidationException("password", password);
        }
        if (null == oldPassword || oldPassword.isEmpty()) {
            throw new PasswordValidationException("old password", password);
        }
        User user = findById(userId);
        String encodeOldPassword = passwordEncoder.encode(oldPassword);
        if (!encodeOldPassword.equals(user.getPassword())) {
            throw new OldPasswordValidationException("old password");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    public User setPassword(UUID userId, String password) {
        if (null == password || password.isEmpty()) {
            throw new PasswordValidationException("password", password);
        }
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    private Role mapRole(UUID roleId) {
        return roleRepo.findById(roleId)
                .orElseThrow(() ->
                        new AssociationEntityDoseNotExistException("roleId", roleId)
                );
    }

    private Department mapDepartment(UUID departmentId) {
        return departmentRepo.findById(departmentId).orElseThrow(() ->
                new AssociationEntityDoseNotExistException("departmentId", departmentId)
        );
    }

    private void checkUniquenessConstraint(User user, String number, String phone, String email) {
        if (null == user) {
            throw new NullPointerException("user");
        }
        if (!user.getNumber().equals(number) && userRepo.existsByNumber(number)) {
            throw new UniquenessConstraintValidationException("number", number);
        }
        if (!user.getPhone().equals(phone) && userRepo.existsByPhone(phone)) {
            throw new UniquenessConstraintValidationException("phone", phone);
        }
        if (!user.getEmail().equals(email) && userRepo.existsByEmail(email)) {
            throw new UniquenessConstraintValidationException("email", email);
        }
    }

    private void checkUniquenessConstraint(String number, String phone, String email) {
        if (userRepo.existsByNumber(number)) {
            throw new UniquenessConstraintValidationException("number", number);
        }
        if (userRepo.existsByPhone(phone)) {
            throw new UniquenessConstraintValidationException("phone", phone);
        }
        if (userRepo.existsByEmail(email)) {
            throw new UniquenessConstraintValidationException("email", email);
        }
    }

    private void checkImmutableConstraint(User user, String name, UUID departmentId) {
        if (null == user) {
            throw new NullPointerException("user");
        }
        if (name != null && !user.getName().equals(name)) {
            throw new ImmutableConstraintValidationException("name");
        }
        if (departmentId != null && !user.getDepartment().getId().equals(departmentId)) {
            throw new ImmutableConstraintValidationException("departmentId");
        }
    }

    public void excelExport(OutputStream out, String number, String name, String phone, String email, Set<UUID> roles,
                            UUID departmentId, User.UserType type, User.UserStatus status) {
        List<User> users = query(number, name, phone, email, roles, departmentId, type, status, null).getContent();
        ExcelUtil.excelExport(out, ExcelUserModel.class, users.stream().map(User::toExcelModel).collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void excelImport(InputStream in) {
        List<ExcelUserModel> excelUserModels = ExcelUtil.excelImport(in, ExcelUserModel.class, 1);
        userRepo.saveAll(mapUsers(excelUserModels));
    }

    private List<User> mapUsers(List<ExcelUserModel> excelUserModels){
        List<User> list = new LinkedList<>();
        excelUserModels.forEach(excelUserModel -> {
                    Optional<User> optionalUser = userRepo.findByNumber(excelUserModel.getNumber());
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        list.add(mapUserWhenExists(excelUserModel, user));
                    } else {
                        checkUniquenessConstraint(excelUserModel.getNumber(),excelUserModel.getPhone(), excelUserModel.getEmail());
                        list.add(mapUserWhenNonExists(excelUserModel));
                    }
                }
        );
        return list;
    }

    private User mapUserWhenExists(ExcelUserModel excelUserModel, User user){
        Department department = departmentRepo.findByNumber(excelUserModel.getDeptNumber()).orElseThrow(() ->
                new AssociationEntityDoseNotExistException("deptNo", excelUserModel.getDeptNumber())
        );
        checkUniquenessConstraint(user, excelUserModel.getNumber(), excelUserModel.getPhone(), excelUserModel.getEmail());
        checkImmutableConstraint(user, excelUserModel.getName(), department.getId());
        user.setName(excelUserModel.getName());
        user.setPhone(excelUserModel.getPhone());
        user.setEmail(excelUserModel.getEmail());
        user.setDepartment(department);
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    private User mapUserWhenNonExists(ExcelUserModel excelUserModel){
        Department department = departmentRepo.findByNumber(excelUserModel.getDeptNumber()).orElseThrow(() ->
                new AssociationEntityDoseNotExistException("deptNumber", excelUserModel.getDeptNumber())
        );
        checkUniquenessConstraint(excelUserModel.getNumber(),excelUserModel.getPhone(), excelUserModel.getEmail());
        return new User(
                excelUserModel.getNumber(),
                excelUserModel.getNumber(),
                new LinkedHashSet<>(),
                excelUserModel.getName(),
                excelUserModel.getPhone(),
                excelUserModel.getEmail(),
                null,
                User.UserType.NON_STAFF,
                User.UserStatus.ENABLED,
                department,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }
}