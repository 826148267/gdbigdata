package edu.jnu.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月23日 22:23
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class AuxiliaryTranslator {

    private String aux;

    private String v;

    private String w;
}
