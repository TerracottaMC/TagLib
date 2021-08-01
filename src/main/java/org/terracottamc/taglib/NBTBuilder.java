package org.terracottamc.taglib;

import io.netty.buffer.ByteBuf;
import org.terracottamc.taglib.nbt.io.NBTReader;
import org.terracottamc.taglib.nbt.io.NBTStream;
import org.terracottamc.taglib.nbt.io.NBTWriter;
import org.terracottamc.taglib.util.nbt.NBTBuilderException;

import java.nio.ByteOrder;
import java.util.Objects;

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
public class NBTBuilder {

    private ByteBuf buffer = null;
    private ByteOrder byteOrder = null;

    /**
     * Sets the {@link ByteBuf} which is used to handle IO
     *
     * @param buffer which is used to work with NBT data
     *
     * @return a fresh {@link NBTBuilder}
     */
    public NBTBuilder withIOBuffer(final ByteBuf buffer) {
        this.buffer = buffer;

        return this;
    }

    /**
     * Sets the {@link ByteOrder} which is used to define the order the bytes were handled in IO
     *
     * @param byteOrder which is used to work with NBT data
     *
     * @return a fresh {@link NBTBuilder}
     */
    public NBTBuilder withByteOrder(final ByteOrder byteOrder) {
        this.byteOrder = byteOrder;

        return this;
    }

    /**
     * Builds a new {@link org.terracottamc.taglib.nbt.io.NBTReader} when the given data is valid
     *
     * @return a fresh {@link org.terracottamc.taglib.nbt.io.NBTReader}
     */
    public NBTReader buildReader() {
        if (this.isDataInvalid()) {
            throw new NBTBuilderException(NBTReader.class.getSimpleName());
        }

        return new NBTReader(this.buffer, this.byteOrder);
    }

    public NBTWriter buildWriter() {
        if (this.isDataInvalid()) {
            throw new NBTBuilderException(NBTWriter.class.getSimpleName());
        }

        return new NBTWriter(this.buffer, this.byteOrder);
    }

    public NBTStream buildStream() {
        if (this.isDataInvalid()) {
            throw new NBTBuilderException(NBTStream.class.getSimpleName());
        }

        // TODO

        return null;
    }

    /**
     * Proofs whether the given data is invalid or if it cannot be used for the selected build operation
     *
     * @return whether attributes are valid
     */
    private boolean isDataInvalid() {
        return Objects.isNull(this.buffer) || Objects.isNull(this.byteOrder);
    }
}