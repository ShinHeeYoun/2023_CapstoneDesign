package test.demo.models;

import javafx.beans.property.SimpleStringProperty;

public class Emitters {
    SimpleStringProperty Name;
    Double Emission;
    SimpleStringProperty Date;

    public Emitters(String name, double emission,String date) {
        Name = new SimpleStringProperty(name);
        Emission = emission;
        Date = new SimpleStringProperty(date);
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public Double getEmission() {
        return Emission;
    }

    /*public SimpleStringProperty emissionProperty() {
        return Emission;
    }*/

    public void setEmission(Double emission) {
        Emission = emission;
    }

    public String getDate() {
        return Date.get();
    }

    public Long getDateLong(){
        return Long.parseLong(Date.get());
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }


}