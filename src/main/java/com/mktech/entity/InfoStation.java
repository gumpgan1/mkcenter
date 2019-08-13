package com.mktech.entity;

public class InfoStation {
    private Integer id;

    private Integer lineId;

    private String statId;

    private String name;
    
    private String nickname;

    private String unit;

    private String instruction;

    private Double maxValue;

    private Double minValue;

    private Double maxThreshold;

    private Double minThreshold;

    private String relate1;

    private String relate2;

    private String relate3;
    
    private Integer abnormal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getStatId() {
        return statId;
    }

    public void setStatId(String statId) {
        this.statId = statId == null ? null : statId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction == null ? null : instruction.trim();
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(Double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public Double getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(Double minThreshold) {
        this.minThreshold = minThreshold;
    }

    public String getRelate1() {
        return relate1;
    }

    public void setRelate1(String relate1) {
        this.relate1 = relate1 == null ? null : relate1.trim();
    }

    public String getRelate2() {
        return relate2;
    }

    public void setRelate2(String relate2) {
        this.relate2 = relate2 == null ? null : relate2.trim();
    }

    public String getRelate3() {
        return relate3;
    }

    public void setRelate3(String relate3) {
        this.relate3 = relate3 == null ? null : relate3.trim();
    }

	public Integer getAbnormal() {
		return abnormal;
	}

	public void setAbnormal(Integer abnormal) {
		this.abnormal = abnormal;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
    
	
    
}