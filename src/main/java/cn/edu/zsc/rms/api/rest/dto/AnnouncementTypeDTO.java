package cn.edu.zsc.rms.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author hsj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementTypeDTO {
    private String name;
    private UUID parentId;
}
