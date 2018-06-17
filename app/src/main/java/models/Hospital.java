package models;

import java.util.Date;

public class Hospital {
    private String hosId;
    private Double latitude;
    private Double longitude;
    private String name;
    private String address;
    private Date openTime;
    private Date closeTime;
    private String phone;
    private Integer scale;
    private String spectialist;

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getSpectialist() {
        return spectialist;
    }

    public void setSpectialist(String spectialist) {
        this.spectialist = spectialist;
    }

    public Hospital() {
    }

    public Hospital(String hosId, Double latitude, Double longitude, String name, String address, Date openTime, Date closeTime, String phone, Integer scale, String spectialist) {
        this.hosId = hosId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.phone = phone;
        this.scale = scale;
        this.spectialist = spectialist;
    }
}
