package com.mktech.entity;

public class ChartConfig {
    private Integer id;
    
    private Integer lineId;
    
    private String position;

    private String type;

    private String chartid;

    private String chartname;
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


	public String getChartid() {
		return chartid;
	}

	public void setChartid(String chartid) {
		this.chartid = chartid;
	}

	public String getChartname() {
		return chartname;
	}

	public void setChartname(String chartname) {
		this.chartname = chartname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	

    
    
}