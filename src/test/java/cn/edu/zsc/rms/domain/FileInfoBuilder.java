package cn.edu.zsc.rms.domain;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

/**
 * @author hsj
 */
public class FileInfoBuilder {

    private FileInfo fileInfo;
    private Integer seq;
    private String name;
    private String url;
    public FileInfoBuilder(int seq) {
        this.fileInfo = new FileInfo(seq,
                randomAlphabetic(5),
                "/api/rest/file/download/" + randomNumeric(32));
    }

    public FileInfoBuilder seq(Integer seq) {
        fileInfo.setSeq(seq);
        return this;
    }

    public FileInfoBuilder name(String name) {
        fileInfo.setName(name);
        return this;
    }

    public FileInfoBuilder url(String url) {
        fileInfo.setUrl(url);
        return this;
    }

    public FileInfo build(){
        return fileInfo;
    }
}
