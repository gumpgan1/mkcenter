package com.mktech.entity;

public class InfoLine {
    private Integer id;

    private Integer companyId;

    private Integer status;

    private String deviceCode;

    private String name;

    private String instruction;

    private String lastUpdate;

    private Double numIn;

    private Double numOut;

    private String lastBegin;

    private String href;

    private String info;

    private String shaks;

    private String eners;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction == null ? null : instruction.trim();
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate == null ? null : lastUpdate.trim();
    }

    public Double getNumIn() {
        return numIn;
    }

    public void setNumIn(Double numIn) {
        this.numIn = numIn;
    }

    public Double getNumOut() {
        return numOut;
    }

    public void setNumOut(Double numOut) {
        this.numOut = numOut;
    }

    public String getLastBegin() {
        return lastBegin;
    }

    public void setLastBegin(String lastBegin) {
        this.lastBegin = lastBegin == null ? null : lastBegin.trim();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public String  getShaks() {
        return shaks;
    }

    public void setShaks(String  shaks) {
        this.shaks = shaks;
    }

    public String  getEners() {
        return eners;
    }

    public void setEners(String  eners) {
        this.eners = eners;
    }
}