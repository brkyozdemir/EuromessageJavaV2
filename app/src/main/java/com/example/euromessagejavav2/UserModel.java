package com.example.euromessagejavav2;

import java.util.List;

public class UserModel {
    private String id;
    private int group_id;
    private int default_billing;
    private int default_shipping;
    private String created_at;
    private String updated_at;
    private String created_in;
    private String dob;
    private String email;
    private String firstname;
    private String lastname;
    private int gender;
    private int store_id;
    private int website_id;
    private List<AddressBean> addresses;
    private int disable_auto_group_change;
    private SubscribeBean extension_attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getDefault_billing() {
        return default_billing;
    }

    public void setDefault_billing(int default_billing) {
        this.default_billing = default_billing;
    }

    public int getDefault_shipping() {
        return default_shipping;
    }

    public void setDefault_shipping(int default_shipping) {
        this.default_shipping = default_shipping;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_in() {
        return created_in;
    }

    public void setCreated_in(String created_in) {
        this.created_in = created_in;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getWebsite_id() {
        return website_id;
    }

    public void setWebsite_id(int website_id) {
        this.website_id = website_id;
    }

    public List<AddressBean> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressBean> addresses) {
        this.addresses = addresses;
    }

    public int getDisable_auto_group_change() {
        return disable_auto_group_change;
    }

    public void setDisable_auto_group_change(int disable_auto_group_change) {
        this.disable_auto_group_change = disable_auto_group_change;
    }

    public SubscribeBean getExtension_attributes() {
        return extension_attributes;
    }

    public void setExtension_attributes(SubscribeBean extension_attributes) {
        this.extension_attributes = extension_attributes;
    }

    public static class AddressBean{
        private int id;
        private int customer_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }
    }

    public static class SubscribeBean{
        private boolean is_subscribed;

        public boolean isIs_subscribed() {
            return is_subscribed;
        }

        public void setIs_subscribed(boolean is_subscribed) {
            this.is_subscribed = is_subscribed;
        }
    }


}