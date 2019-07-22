package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

/**
 * @author hsj
 */
public class DepartmentBuilder {
    private Department department;

    public DepartmentBuilder() {
        department = new Department(
                randomNumeric(2),
                randomAlphabetic(5),
                randomAlphabetic(2),
                Department.DeptType.COLLEGE,
                null,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }

    public DepartmentBuilder number(String number) {
        department.setNumber(number);
        return this;
    }

    public DepartmentBuilder name(String name) {
        department.setName(name);
        return this;
    }

    public DepartmentBuilder abbreviation(String abbreviation) {
        department.setAbbreviation(abbreviation);
        return this;
    }

    public DepartmentBuilder type(Department.DeptType type) {
        department.setType(type);
        return this;
    }

    public DepartmentBuilder parentId(UUID parentId) {
        department.setParentId(parentId);
        return this;
    }

    public DepartmentBuilder id(UUID id) {
        department.setId(id);
        return this;
    }

    public Department build() {
        return department;
    }
}
