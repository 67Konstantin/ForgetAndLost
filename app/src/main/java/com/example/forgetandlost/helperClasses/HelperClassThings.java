package com.example.forgetandlost.helperClasses;

public class HelperClassThings {
    String name, describing, conditions, area, image, data, userId;

    private String key;

    public HelperClassThings(String name, String describing, String conditions, String area, String data, String userId, String image, String key) {
        this.name = name;
        this.describing = describing;
        this.conditions = conditions;
        this.area = area;
        this.data = data;
        this.userId = userId;
        this.image = image;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescribing() {
        return describing;
    }

    public void setDescribing(String describing) {
        this.describing = describing;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public HelperClassThings() {
    }
}
