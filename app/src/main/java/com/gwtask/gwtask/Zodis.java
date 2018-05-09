package com.gwtask.gwtask;

public class Zodis {
    private String reiksme;
    private String partOfSpeech;
    private String definition;
    private String paieskosLaikas;

    public Zodis(String reiksme, String partOfSpeech, String definition, String paieskosLaikas) {
        this.reiksme = reiksme;
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.paieskosLaikas = paieskosLaikas;
    }

    public String getReiksme() {
        return reiksme;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }

    public String getPaieskosLaikas() {
        return paieskosLaikas;
    }
}
