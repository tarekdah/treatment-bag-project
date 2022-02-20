package com.smallacademy.smart_bag;

public class Player {
    private String Name;
    private String Date;
    private String Score,Initialdiagnosis,Id;
Player(){}

    @Override
    public String toString() {
        return "Player{" +
                "Name='" + Name + '\'' +
                ", Date='" + Date + '\'' +
                ", Score='" + Score + '\'' +
                ", Initial_diag='" + Initialdiagnosis + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setScore(String score) {
        Score = score;
    }

    public void setInitialdiagnosis(String initial_diag) {
        Initialdiagnosis = initial_diag;
    }

    public void setId(String id) {
        Id = id;
    }

    public Player(String name, String date, String score, String initial_diag, String id) {
        Name = name;
        Date = date;
        Score = score;
        Initialdiagnosis = initial_diag;
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }

    public String getScore() {
        return Score;
    }

    public String getInitialdiagnosis() {
        return Initialdiagnosis;
    }

    public String getId() {
        return Id;
    }
}
