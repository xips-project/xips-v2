package cat.uvic.xips.entities;


import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Role implements Serializable {

    USER,
    ADMIN

}
