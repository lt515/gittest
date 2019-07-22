package cn.edu.zsc.rms.api.rest.dto;

import cn.edu.zsc.rms.domain.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author hsj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDTO {
    String title;
    UUID announcementTypeId;
    Boolean topping;
    String content;
    List<FileInfo> files;
}
