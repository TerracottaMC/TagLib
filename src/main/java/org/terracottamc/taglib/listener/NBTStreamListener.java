package org.terracottamc.taglib.listener;

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
public interface NBTStreamListener {

    /**
     * This method gets invoked after the reading of an NBT Node has processed
     *
     * @param nbtPath  which represents the nbt path that has been read
     * @param nbtValue which is representative for the nbt value that has been read
     */
    void listenOnNBTNode(final String nbtPath, final Object nbtValue);
}