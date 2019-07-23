package cn.edu.zsc.rms.api.rest.dto;


import cn.edu.zsc.rms.domain.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    String name;
    String type;
    String remark;
    List<FileInfo> files;

}
