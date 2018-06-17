package models;

public class Feedback {
    private String hos_id;
    private Integer type;
    private String fdbk_info;
    private String fdbk_note;
    private String phone;
    private String email;

    public String getHos_id() {
        return hos_id;
    }

    public void setHos_id(String hos_id) {
        this.hos_id = hos_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFdbk_info() {
        return fdbk_info;
    }

    public void setFdbk_info(String fdbk_info) {
        this.fdbk_info = fdbk_info;
    }

    public String getFdbk_note() {
        return fdbk_note;
    }

    public void setFdbk_note(String fdbk_note) {
        this.fdbk_note = fdbk_note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Feedback() {
    }

    public Feedback(String hos_id, Integer type, String fdbk_info, String fdbk_note, String phone, String email) {
        this.hos_id = hos_id;
        this.type = type;
        this.fdbk_info = fdbk_info;
        this.fdbk_note = fdbk_note;
        this.phone = phone;
        this.email = email;
    }
}
