/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.serviceimpl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.buch.shop.bildskalierung.backend.serviceimpl.WidthAndHeight;

/**
 * Testet die Klasse WidthAndHeightTest.
 */
public class WidthAndHeightTest {

    @Test
    public void testEqualsIfItIsTrue() {
        WidthAndHeight first = new WidthAndHeight(15, 30);
        WidthAndHeight second = new WidthAndHeight(15, 30);

        assertTrue(first.equals(second));
        assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsIfItIsFalse() {
        WidthAndHeight first = new WidthAndHeight(30, 15);
        WidthAndHeight second = new WidthAndHeight(15, 30);

        assertTrue(!first.equals(second));
        assertTrue(!second.equals(first));
    }

    @Test
    public void testEqualsIfItIsFalse2() {
        WidthAndHeight first = new WidthAndHeight(15, 30);
        WidthAndHeight second = new WidthAndHeight(16, 30);

        assertTrue(!first.equals(second));
        assertTrue(!second.equals(first));
    }

    @Test
    public void testEqualsIfItIsTrueBigNumbers() {
        WidthAndHeight first = new WidthAndHeight(15555, 15556);
        WidthAndHeight second = new WidthAndHeight(15555, 15556);

        assertTrue(first.equals(second));
        assertTrue(second.equals(first));
    }
}
