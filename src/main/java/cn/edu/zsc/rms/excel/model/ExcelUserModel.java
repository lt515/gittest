package cn.edu.zsc.rms.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author hsj
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ExcelUserModel extends BaseRowModel {
    @ExcelProperty(value = "工号", index = 0)
    private String number;
    @ExcelProperty(value = "姓名", index = 1)
    private String name;
    @ExcelProperty(value = "手机号", index = 2)
    private String phone;
    @ExcelProperty(value = "邮箱", index = 3)
    private String email;
    @ExcelProperty(value = "部门编号", index = 4)
    private String deptNumber;
}
