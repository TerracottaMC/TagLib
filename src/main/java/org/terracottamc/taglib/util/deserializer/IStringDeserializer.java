package org.terracottamc.taglib.util.deserializer;

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
public interface IStringDeserializer {

    /**
     * Deserializes the given data, its length and with given offset
     *
     * @param data   which should be decoded
     * @param length which represents the length of the data to deserialize
     * @param offset which is the index of the first byte of the data to decode
     *
     * @return a fresh {@link java.lang.String}
     */
    String deserialize(final byte[] data, final int length, final int offset);
}