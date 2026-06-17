package com.franRR.librarymanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories", schema = "library_manager_db")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name",unique = true, nullable = false, length = 50)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(){

    }

    public Category( String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }


}