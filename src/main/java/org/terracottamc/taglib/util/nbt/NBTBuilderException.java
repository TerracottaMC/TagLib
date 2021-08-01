package org.terracottamc.taglib.util.nbt;

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
public class NBTBuilderException extends RuntimeException {

    /**
     * Creates a new {@link org.terracottamc.taglib.util.nbt.NBTBuilderException} with given message
     *
     * @param failedOnBuildObject which represents the name of the object which is involved in a conflict
     */
    public NBTBuilderException(final String failedOnBuildObject) {
        super("The building attempt of " + failedOnBuildObject + " failed due to the provision of invalid data");
    }
}