/*
 * buch.de internetstores AG (c)2012
 * All rights reserved.
 */
package de.buch.shop.bildskalierung.backend.serviceimpl;

/**
 * Bestimmt den Skalierfaktor des Bildes
 */
public class WidthAndHeight {

    private final Long width;
    private final Long height;

    public WidthAndHeight(final Long width, final Long height) {

        this.width = width;
        this.height = height;
    }

    public WidthAndHeight(final int width, final int height) {

        this.width = Long.valueOf(width);
        this.height = Long.valueOf(height);
    }

    public static WidthAndHeight determineScaleFactor(final WidthAndHeight originalWidthAndHeight,
            final WidthAndHeight requestedWidthAndHeight) {

        if ((originalWidthAndHeight == null) || (requestedWidthAndHeight == null)) {
            return null;
        }
        // pseudo fixkomma spass: 10000 entspricht 1.0
        // Damit das Ergebnis genauer ist.
        final long SCALE_ONE = 10000;

        final long heightFactor = (SCALE_ONE * requestedWidthAndHeight.height.longValue())
                / originalWidthAndHeight.height.longValue();
        final long widthFactor = (SCALE_ONE * requestedWidthAndHeight.width.longValue())
                / originalWidthAndHeight.width.longValue();

        // Orginalbild wird zurueckgegeben, da Vergroesserung
        if ((heightFactor > SCALE_ONE) && (widthFactor > SCALE_ONE)) {
            return originalWidthAndHeight;
        }

        // Der Faktor, der die groesste Verkleinerung fordert, gewinnt.
        if (heightFactor < widthFactor) {

            final long newWidth = (requestedWidthAndHeight.height * originalWidthAndHeight.width)
                    / originalWidthAndHeight.height;
            return new WidthAndHeight(newWidth, requestedWidthAndHeight.height);
        } else {

            final long newHeight = (requestedWidthAndHeight.width * originalWidthAndHeight.height)
                    / originalWidthAndHeight.width;
            return new WidthAndHeight(requestedWidthAndHeight.width, newHeight);
        }
    }

    public Long getWidth() {
        return width;
    }

    public Long getHeight() {
        return height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        super.hashCode();
        int hashCode = 11;
        int multi = 29;
        hashCode = (hashCode * multi) + width.intValue();
        hashCode = (hashCode * multi) + (int) (height & 0xFFFFFFFF);
        hashCode = (hashCode * multi) + (int) (height >>> 32);
        return hashCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return ((WidthAndHeight) obj).hashCode() == hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s [%s x %s]", getClass().getSimpleName(), getWidth(), getHeight());
    }
}