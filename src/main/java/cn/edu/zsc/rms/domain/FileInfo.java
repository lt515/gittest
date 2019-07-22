package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author hsj
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private Integer seq;
    private String name;
    @Column(nullable = false)
    private String url;
}
