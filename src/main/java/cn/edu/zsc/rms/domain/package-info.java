/**
 * @author hsj
 */
@TypeDef(
        name = "id",
        typeClass = UUIDCharType.class,
        defaultForType = UUID.class
)
package cn.edu.zsc.rms.domain;

import org.hibernate.annotations.TypeDef;
import org.hibernate.type.UUIDCharType;

import java.util.UUID;