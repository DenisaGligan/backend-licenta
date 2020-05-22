package licentaBackend.code.controllers;

import java.io.Serializable;

public class DTO implements Serializable {


    private String type;
    private String name;

    public DTO() {

    }
    public DTO(String type, String name) {
        this.type = type;
        this.name = name;
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
}
