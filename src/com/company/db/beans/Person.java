package com.company.db.beans;


public class Person {
    private int personId;
    private String name;
    private String family;
    private String nationalId;

    public Person(String name, String family, String nationalId) {
//        this.personId = personId;
        this.name = name.trim();
        this.family = family.trim();
        this.nationalId = nationalId.trim();
    }

    public Person() {
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family.trim();
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId.trim();
    }
}
