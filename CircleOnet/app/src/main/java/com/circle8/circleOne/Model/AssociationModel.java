package com.circle8.circleOne.Model;

/**
 * Created by admin on 10/04/2017.
 */

import java.io.Serializable;

public class AssociationModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private boolean isSelected;

    public AssociationModel() {

    }

    public AssociationModel(String id, String name) {

        this.id = id;
        this.name = name;

    }

    public AssociationModel(String id, String name, boolean isSelected) {

        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
