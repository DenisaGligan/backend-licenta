package licentaBackend.code.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String type;

    @NotBlank
    @Size(max = 20)
    private String name;


    private  Integer price;


    private String imgUrl;

    public Product() {
    }

    public Product(@NotBlank @Size(max = 20) String type, @NotBlank @Size(max = 20) String name, Integer price, String imgUrl) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
