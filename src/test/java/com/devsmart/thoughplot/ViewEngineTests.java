package com.devsmart.thoughplot;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViewEngineTests {

    @Test
    public void testLinkVisitor() {


        ViewEngine engine = new ViewEngine();
        Note n = engine.loadMarkdown("test", "this doc contains links to [[google]]");


    }
}
