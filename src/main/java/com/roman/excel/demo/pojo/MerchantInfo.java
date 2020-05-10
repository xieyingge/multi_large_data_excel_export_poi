package com.roman.excel.demo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-05-10
 */
@TableName("t_merchant_info")
public class MerchantInfo extends Model<MerchantInfo> {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String address;

    private String phone;

    private String money;

    private String descDesc;

    private String status;

    private String farenPresent;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDescDesc() {
        return descDesc;
    }

    public void setDescDesc(String descDesc) {
        this.descDesc = descDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFarenPresent() {
        return farenPresent;
    }

    public void setFarenPresent(String farenPresent) {
        this.farenPresent = farenPresent;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MerchantInfo{" +
        "id=" + id +
        ", name=" + name +
        ", address=" + address +
        ", phone=" + phone +
        ", money=" + money +
        ", descDesc=" + descDesc +
        ", status=" + status +
        ", farenPresent=" + farenPresent +
        "}";
    }
}
