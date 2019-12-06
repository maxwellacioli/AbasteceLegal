package com.maxwell.AbasteceLegal.util;

public enum City {
    MACEIO("Maceió"),
    PALMEIRA("Palmeira dos Índios"),
    RECIFE("Recife"),
    TEOTONIO("Teotônio Vilela"),
    GARANHUNS("Garanhuns");

    private String name;

    City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
