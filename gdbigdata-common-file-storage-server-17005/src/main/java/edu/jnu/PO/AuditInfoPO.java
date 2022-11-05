package edu.jnu.PO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月23日 22:33
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "tb_audit_info")
public class AuditInfoPO {
    @Id
    @Column
    private String dataFileId;

    @NonNull
    @Column
    private String tagFileId;

    @NonNull
    @Column
    private String aux;

    @NonNull
    @Column
    private String v;

    @NonNull
    @Column
    private String w;
}
