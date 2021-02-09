package com.gopavajhalagayatri.seniorresearch;

public class Task {
    int day;
    int month;
    int year;
    boolean state;
    String name;
    int time;

    public Task(int a, int b, int c, String d, int e, boolean f){
        day = a;
        month = b;
        year = c;
        name = d;
        time = e;
        state = f;
    }

    @Override
    public String toString() {
        return "DateSelected{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", name=" + name +
                ", time=" + time +
                ", state=" + state +
                '}';
    }
}
