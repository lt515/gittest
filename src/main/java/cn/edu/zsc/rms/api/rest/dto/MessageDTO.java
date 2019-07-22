package cn.edu.zsc.rms.api.rest.dto;

import cn.edu.zsc.rms.domain.FileInfo;
import cn.edu.zsc.rms.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    Set<UUID> userIds;
    @NotNull
    private String title;
    private Message.MessageType type;
    private Message.CreateBy createBy;
    private String content;
    private String briefContent;
    private Set<Message.PushMethod> pushMethods;
    private List<FileInfo> files;
}
