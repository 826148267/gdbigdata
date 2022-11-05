package edu.jnu.PO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月01日 19:31
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_tag_file")
public class TagFilePO {

    @Id
    @Column
    private String id = UUID.randomUUID().toString().replace("-", "");

    @NonNull
    @Column
    private String fileDir;

    @NonNull
    @Column
    private String fileName;
}
