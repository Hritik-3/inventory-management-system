package com.boot.ordercraft.dto;

public class BackendScheduleDTO {
    private Long psId;
    private int psQuantity;
    private String psStatus;
    private String psStartDate;
    private String psEndDate;
    private String actions;

    private Long productId;
    private String productName;

    private Long productionLineId;
    private String productionLineName;

    public Long getPsId() {
		return psId;
	}

	public void setPsId(Long psId) {
		this.psId = psId;
	}

	public int getPsQuantity() {
		return psQuantity;
	}

	public void setPsQuantity(int psQuantity) {
		this.psQuantity = psQuantity;
	}

	public String getPsStatus() {
		return psStatus;
	}

	public void setPsStatus(String psStatus) {
		this.psStatus = psStatus;
	}

	public String getPsStartDate() {
		return psStartDate;
	}

	public void setPsStartDate(String psStartDate) {
		this.psStartDate = psStartDate;
	}

	public String getPsEndDate() {
		return psEndDate;
	}

	public void setPsEndDate(String psEndDate) {
		this.psEndDate = psEndDate;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductionLineId() {
		return productionLineId;
	}

	public void setProductionLineId(Long productionLineId) {
		this.productionLineId = productionLineId;
	}

	public String getProductionLineName() {
		return productionLineName;
	}

	public void setProductionLineName(String productionLineName) {
		this.productionLineName = productionLineName;
	}

	// Constructors
    public BackendScheduleDTO() {}

    public BackendScheduleDTO(Long psId, int psQuantity, String psStatus, String psStartDate, String psEndDate,
                              String actions, Long productId, String productName,
                              Long productionLineId, String productionLineName) {
        this.psId = psId;
        this.psQuantity = psQuantity;
        this.psStatus = psStatus;
        this.psStartDate = psStartDate;
        this.psEndDate = psEndDate;
        this.actions = actions;
        this.productId = productId;
        this.productName = productName;
        this.productionLineId = productionLineId;
        this.productionLineName = productionLineName;
    }

    // Getters and Setters
    // ... generate them using IDE
}
