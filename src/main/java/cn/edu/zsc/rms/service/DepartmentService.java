package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.Department;
import cn.edu.zsc.rms.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hsj
 */
@Service
public class DepartmentService {
    private DepartmentRepository departmentRepo;

    public DepartmentService(DepartmentRepository departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public List<Department> list(){
        return departmentRepo.findAll();
    }
}
