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
public class StringDeserializer implements IStringDeserializer {

    @Override
    public String deserialize(final byte[] data, final int length, final int offset) {
        return new String(data, length, offset);
    }
}