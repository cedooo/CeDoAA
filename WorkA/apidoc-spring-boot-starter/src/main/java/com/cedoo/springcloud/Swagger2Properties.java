package com.cedoo.springcloud;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger2 配置文件
 */
@ConfigurationProperties(prefix = "swagger")
public class Swagger2Properties {
    private String groupName;
    private String basePackage;
    private String title;
    private String host;
    private String desc;
    private String serviceUrl;
    private String version;
    private String contact;
    private String license;
    private String licenseUrl;

    //contact
    private String contactName;
    private String contactUrl;
    private String contactEmail;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return "Swagger2Properties{" +
                "groupName='" + groupName + '\'' +
                ", basePackage='" + basePackage + '\'' +
                ", title='" + title + '\'' +
                ", host='" + host + '\'' +
                ", desc='" + desc + '\'' +
                ", serviceUrl='" + serviceUrl + '\'' +
                ", version='" + version + '\'' +
                ", contact='" + contact + '\'' +
                ", license='" + license + '\'' +
                ", licenseUrl='" + licenseUrl + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactUrl='" + contactUrl + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                '}';
    }
}