package com.devsmart.thoughplot;

import java.util.HashSet;
import java.util.Set;

public class Note {

    public String name;
    public String markdown;
    public String html;
    public final Set<String> linkRefs = new HashSet<>();

}
