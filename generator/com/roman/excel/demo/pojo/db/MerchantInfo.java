package com.roman.excel.demo.pojo.db;

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
public class MerchantInfo extends Model<MerchantInfo> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String address;

    private Integer phone;

    private Integer money;

    private String desc;

    private Integer status;

    private String farenPresent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
        ", desc=" + desc +
        ", status=" + status +
        ", farenPresent=" + farenPresent +
        "}";
    }
}
