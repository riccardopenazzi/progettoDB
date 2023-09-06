package com.example.pizzapp.model.entities.delivery;

import java.time.LocalDateTime;

public class Delivery {
    private int codCostumer;
    private int codOrder;
    private LocalDateTime deliveryTime;
    private LocalDateTime effectiveTime;

    private String dest;

    public Delivery(int codCostumer, int codOrder, LocalDateTime deliveryTime, String dest) {
        this.codCostumer = codCostumer;
        this.codOrder = codOrder;
        this.deliveryTime = deliveryTime;
        this.dest = dest;
    }

    public Delivery(int codCostumer, int codOrder, LocalDateTime deliveryTime, LocalDateTime effectiveTime, String dest) {
        this.codCostumer = codCostumer;
        this.codOrder = codOrder;
        this.deliveryTime = deliveryTime;
        this.effectiveTime = effectiveTime;
        this.dest = dest;
    }

    public Delivery(int codOrder, LocalDateTime deliveryTime) {
        this.codOrder = codOrder;
        this.deliveryTime = deliveryTime;
    }

    public int getCodCostumer() {
        return codCostumer;
    }

    public void setCodCostumer(int codCostumer) {
        this.codCostumer = codCostumer;
    }

    public int getCodOrder() {
        return codOrder;
    }

    public void setCodOrder(int codOrder) {
        this.codOrder = codOrder;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public LocalDateTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(LocalDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
