package com.uitility.personal.work.entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "EmailDetails")
public class EmailEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String HRName;
    @Column
    private String EmailId;
    @Column
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column
    private String status;
    @Column
    private String mobileNo;
    @Column
    private String companyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHRName() {
        return HRName;
    }

    public void setHRName(String HRName) {
        this.HRName = HRName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
