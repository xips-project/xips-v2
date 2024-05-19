package cat.uvic.xips.entities;


import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public enum Role implements Serializable {

    USER,
    ADMIN

}
