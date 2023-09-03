package com.example.pizzapp.model.entities.user;

public class User {
    private static int codUtente;
    private static String nome;
    private static String cognome;
    private static String email;
    private static String telefono;
    private static String tipo;

    public static void createUser (final int codUtente, final String nome, final String cognome, final String email, final String telefono, final String tipo) {
        User.codUtente = codUtente;
        User.nome = nome;
        User.cognome = cognome;
        User.email = email;
        User.telefono = telefono;
        User.tipo = tipo;
    }

    public static int getCodUtente() {
        return codUtente;
    }

    public static String getNome() {
        return nome;
    }

    public static String getCognome() {
        return cognome;
    }

    public static String getEmail() {
        return email;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static String getTipo() {
        return tipo;
    }

    public static void setCodUtente(int codUtente) {
        User.codUtente = codUtente;
    }

    public static void setNome(String nome) {
        User.nome = nome;
    }

    public static void setCognome(String cognome) {
        User.cognome = cognome;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static void setTelefono(String telefono) {
        User.telefono = telefono;
    }

    public static void setTipo(String tipo) {
        User.tipo = tipo;
    }
}
