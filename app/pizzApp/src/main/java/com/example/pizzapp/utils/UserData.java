package com.example.pizzapp.utils;
public class UserData {
    private static int codUtente;
    private static String nome;
    private static String cognome;
    private static String email;
    private static int telefono;
    private static String tipo;

    public UserData(final int codUtente, final String nome, final String cognome, final String email, final int telefono, final String tipo) {
        UserData.codUtente = codUtente;
        UserData.nome = nome;
        UserData.cognome = cognome;
        UserData.email = email;
        UserData.telefono = telefono;
        UserData.tipo = tipo;
    }

    public int getCodUtente() {
        return codUtente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public static void setCodUtente(int codUtente) {
        UserData.codUtente = codUtente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
