package cat.uvic.xips.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ProductType {

    BOOKS("Books"),
    CLOTHING("Clothing"),
    ELECTRONICS("Electronics"),
    SPORTS("Sports"),
    GAMING("Gaming"),
    HOME("Home"),
    FILMS("Films"),
    TOOLS("Tools");

    private final String value;

    ProductType(String value){
        this.value = value;
    }


}
