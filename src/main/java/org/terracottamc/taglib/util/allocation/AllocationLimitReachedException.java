package org.terracottamc.taglib.util.allocation;

/**
 * Copyright (c) 2021, TerracottaMC
 * All rights reserved.
 *
 * <p>
 * This project is licensed under the BSD 3-Clause License which
 * can be found in the root directory of this source tree
 *
 * @author Kaooot
 * @version 1.0
 */
public class AllocationLimitReachedException extends Exception {

    /**
     * Creates a new {@link AllocationLimitReachedException} with given message
     *
     * @param message which describes the cause why this {@link AllocationLimitReachedException} was fired
     */
    public AllocationLimitReachedException(final String message) {
        super(message);
    }
}