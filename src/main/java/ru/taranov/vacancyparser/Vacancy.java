package ru.taranov.vacancyparser;

import java.time.LocalDate;

public class Vacancy {

    private int id;
    private String name;
    private String text;
    private String link;
    private LocalDate localDate;

    public Vacancy(String name, String text, String link, LocalDate date) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.localDate = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public LocalDate getDate() {
        return localDate;
    }

    @Override
    public String toString() {
        return String.format("* %s,\n  %s,\n  %s\n  %s", getName(), getText(), getLink(), getDate());
    }
}