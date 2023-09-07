package com.example.pizzapp.model.entities.coupon;

public class Coupon {
    private double sconto;
    private int costo;
    private Boolean attivo;

    private int codCoupon;

    public Coupon(double sconto, int costo, Boolean attivo, int codCoupon) {
        this.sconto = sconto;
        this.costo = costo;
        this.attivo = attivo;
        this.codCoupon = codCoupon;
    }

    public double getSconto() {
        return sconto;
    }

    public void setSconto(double sconto) {
        this.sconto = sconto;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public int getCodCoupon() {
        return codCoupon;
    }

    public void setCodCoupon(int codCoupon) {
        this.codCoupon = codCoupon;
    }
}
