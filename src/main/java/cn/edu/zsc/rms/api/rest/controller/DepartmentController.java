package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.domain.Department;
import cn.edu.zsc.rms.service.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/department")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("list")
    public List<Department> list() {
        return departmentService.list();
    }
}
