package com.devsmart.thoughplot;

public class Note implements Comparable<Note> {

    public final String name;
    public String markdown;
    public String html;

    public Note(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass()) {
            return false;
        }

        return compareTo((Note) obj) == 0;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Note o) {
        return name.compareTo(o.name);
    }
}
