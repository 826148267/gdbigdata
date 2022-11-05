package edu.jnu.PO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月01日 19:25
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "tb_key_file")
public class KeyFilePO {

    @Id
    @Column
    private String id = UUID.randomUUID().toString().replace("-","");

    @NonNull
    @Column
    private String fileDir;

    @NonNull
    @Column
    private String fileName;
}
