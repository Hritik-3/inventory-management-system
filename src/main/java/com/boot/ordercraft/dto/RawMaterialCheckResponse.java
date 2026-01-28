package com.boot.ordercraft.dto;

import java.util.List;

public class RawMaterialCheckResponse {
    private boolean sufficient;
    private List<String> sufficientMaterials;
    private List<String> insufficientMaterials;

    public RawMaterialCheckResponse(boolean sufficient, List<String> sufficientMaterials, List<String> insufficientMaterials) {
        this.sufficient = sufficient;
        this.sufficientMaterials = sufficientMaterials;
        this.insufficientMaterials = insufficientMaterials;
    }

    public boolean isSufficient() { return sufficient; }
    public void setSufficient(boolean sufficient) { this.sufficient = sufficient; }

    public List<String> getSufficientMaterials() { return sufficientMaterials; }
    public void setSufficientMaterials(List<String> sufficientMaterials) { this.sufficientMaterials = sufficientMaterials; }

    public List<String> getInsufficientMaterials() { return insufficientMaterials; }
    public void setInsufficientMaterials(List<String> insufficientMaterials) { this.insufficientMaterials = insufficientMaterials; }
}
