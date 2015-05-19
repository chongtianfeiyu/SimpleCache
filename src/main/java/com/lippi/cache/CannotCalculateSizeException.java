
package com.lippi.cache;

/**
 * <p>Flags an exception when we cannot determine size of the object to be cached.</p>
 *
 */
public class CannotCalculateSizeException extends Exception {

    public static final long serialVersionUID = 1754096832201752388L;

    public CannotCalculateSizeException() {
    }

    public CannotCalculateSizeException(Object obj) {
        super("Unable to determine size of " + obj.getClass() + " instance");
    }
}
