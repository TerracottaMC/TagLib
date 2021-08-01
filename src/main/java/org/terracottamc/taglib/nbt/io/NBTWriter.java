package org.terracottamc.taglib.nbt.io;

import io.netty.buffer.ByteBuf;
import org.terracottamc.taglib.nbt.tag.NBTTagCompound;
import org.terracottamc.taglib.util.VarIntUtil;
import org.terracottamc.taglib.util.nbt.NBTConstants;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
public class NBTWriter {

    private final ByteBuf buffer;
    private final ByteOrder byteOrder;

    private boolean useVarInt;

    public NBTWriter(final ByteBuf buffer, final ByteOrder byteOrder) {
        this.buffer = buffer;
        this.byteOrder = byteOrder;
    }

    /**
     * Updates whether {@link org.terracottamc.taglib.util.VarIntUtil} methods should be used here
     *
     * @param useVarInt which should be updated
     */
    public void setUseVarInt(final boolean useVarInt) {
        this.useVarInt = useVarInt;
    }

    /**
     * Proofs whether {@link org.terracottamc.taglib.util.VarIntUtil} will be used here
     *
     * @return whether {@link org.terracottamc.taglib.util.VarIntUtil} functions will be used
     */
    public boolean isUsingVarInt() {
        return this.useVarInt;
    }

    /**
     * Writes a {@link java.util.List}
     *
     * @param list which should be written
     */
    public void writeTagList(final List<Object> list) {
        this.writeTagHeader(NBTConstants.TAG_LIST, "");
        this.writeTagListValue(list);
    }

    /**
     * Writes a fresh {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound}
     *
     * @param nbtTagCompound which should be written
     */
    public void writeTagCompound(final NBTTagCompound nbtTagCompound) {
        this.writeTagHeader(NBTConstants.TAG_COMPOUND, nbtTagCompound.getName());
        this.writeTagCompoundValue(nbtTagCompound);
    }

    /**
     * Writes the header for a {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound} or a {@link java.util.List}
     *
     * @param tagId which is representative for the identifier of this tag
     * @param name  which stands for the key name
     */
    private void writeTagHeader(final byte tagId, final String name) {
        this.writeByteValue(tagId);
        this.writeStringValue(name);
    }

    public void writeByteValue(final byte value) {
        this.guaranteeBufferCapacity(1);

        this.buffer.writeByte(value);
    }

    private void writeShortValue(final short value) {
        this.guaranteeBufferCapacity(2);

        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.buffer.writeShortLE(value);
        } else {
            this.buffer.writeShort(value);
        }
    }

    private void writeIntValue(final int value) {
        if (this.useVarInt) {
            VarIntUtil.writeVarInt(this, value);
        } else {
            this.guaranteeBufferCapacity(4);

            if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.buffer.writeIntLE(value);
            } else {
                this.buffer.writeInt(value);
            }
        }
    }

    private void writeLongValue(final long value) {
        if (this.useVarInt) {
            VarIntUtil.writeVarLong(this, value);
        } else {
            this.guaranteeBufferCapacity(8);

            if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.buffer.writeLongLE(value);
            } else {
                this.buffer.writeLong(value);
            }
        }
    }

    private void writeFloatValue(final float value) {
        this.guaranteeBufferCapacity(4);

        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.buffer.writeFloatLE(value);
        } else {
            this.buffer.writeFloat(value);
        }
    }

    private void writeDoubleValue(final double value) {
        this.guaranteeBufferCapacity(8);

        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.buffer.writeDoubleLE(value);
        } else {
            this.buffer.writeDouble(value);
        }
    }

    private void writeStringValue(final String value) {
        if (value == null) {
            if (this.useVarInt) {
                this.writeByteValue((byte) 0);
            } else {
                this.writeShortValue((short) 0);
            }

            return;
        }

        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        final int length = bytes.length;

        if (this.useVarInt) {
            VarIntUtil.writeUnsignedVarInt(this, length);
        } else {
            this.writeShortValue((short) length);
        }

        this.guaranteeBufferCapacity(length);

        this.buffer.writeBytes(bytes);
    }

    private void writeByteArrayValue(final byte[] value) {
        final int length = value.length;

        this.guaranteeBufferCapacity(length);

        this.writeIntValue(length);

        this.buffer.writeBytes(value);
    }

    private void writeIntArrayValue(final int[] value) {
        final int length = value.length;

        this.guaranteeBufferCapacity(length);

        this.writeIntValue(length);

        for (final int i : value) {
            this.writeIntValue(i);
        }
    }

    private void writeTagListValue(final List<Object> value) {
        this.guaranteeBufferCapacity(5);

        if (value.size() <= 0) {
            this.writeByteValue(NBTConstants.TAG_BYTE);
            this.writeIntValue(0);

            return;
        }

        final byte tagId = this.retrieveTagIdFromValue(value.get(0));

        this.writeByteValue(tagId);
        this.writeIntValue(value.size());

        for (final Object rawValue : value) {
            this.writeValuesByTagId(tagId, rawValue);
        }
    }

    private void writeTagCompoundValue(final NBTTagCompound nbtTagCompound) {
        for (final Map.Entry<String, Object> nbtEntry : nbtTagCompound.retrieveNBTEntries()) {
            final String key = nbtEntry.getKey();
            final Object rawValue = nbtEntry.getValue();

            final byte tagId = this.retrieveTagIdFromValue(rawValue);

            this.writeTagHeader(tagId, key);
            this.writeValuesByTagId(tagId, rawValue);
        }
    }

    /**
     * Writes the values identified by their tagId
     *
     * @param tagId    which is needed to write the values
     * @param rawValue the value which should be written
     */
    private void writeValuesByTagId(final byte tagId, final Object rawValue) {
        switch (tagId) {
            case NBTConstants.TAG_BYTE:
                this.writeByteValue((Byte) rawValue);
                break;
            case NBTConstants.TAG_SHORT:
                this.writeShortValue((Short) rawValue);
                break;
            case NBTConstants.TAG_INT:
                this.writeIntValue((Integer) rawValue);
                break;
            case NBTConstants.TAG_LONG:
                this.writeLongValue((Long) rawValue);
                break;
            case NBTConstants.TAG_FLOAT:
                this.writeFloatValue((Float) rawValue);
                break;
            case NBTConstants.TAG_DOUBLE:
                this.writeDoubleValue((Double) rawValue);
                break;
            case NBTConstants.TAG_STRING:
                this.writeStringValue((String) rawValue);
                break;
            case NBTConstants.TAG_BYTE_ARRAY:
                this.writeByteArrayValue((byte[]) rawValue);
                break;
            case NBTConstants.TAG_LIST:
                this.writeTagListValue((List<Object>) rawValue);
                break;
            case NBTConstants.TAG_COMPOUND:
                this.writeTagCompoundValue((NBTTagCompound) rawValue);
                break;
            case NBTConstants.TAG_INT_ARRAY:
                this.writeIntArrayValue((int[]) rawValue);
                break;
        }
    }

    /**
     * Retrieves the tag identifier from the given value
     *
     * @param value which is needed to retrieve the id
     *
     * @return a fresh byte
     */
    private byte retrieveTagIdFromValue(final Object value) {
        if (value.getClass().equals(Byte.class)) {
            return NBTConstants.TAG_BYTE;
        } else if (value.getClass().equals(Short.class)) {
            return NBTConstants.TAG_SHORT;
        } else if (value.getClass().equals(Integer.class)) {
            return NBTConstants.TAG_INT;
        } else if (value.getClass().equals(Long.class)) {
            return NBTConstants.TAG_LONG;
        } else if (value.getClass().equals(Float.class)) {
            return NBTConstants.TAG_FLOAT;
        } else if (value.getClass().equals(Double.class)) {
            return NBTConstants.TAG_DOUBLE;
        } else if (value.getClass().equals(String.class)) {
            return NBTConstants.TAG_STRING;
        } else if (value instanceof byte[]) {
            return NBTConstants.TAG_BYTE_ARRAY;
        } else if (value instanceof List) {
            return NBTConstants.TAG_LIST;
        } else if (value.getClass().equals(NBTTagCompound.class)) {
            return NBTConstants.TAG_COMPOUND;
        } else if (value instanceof int[]) {
            return NBTConstants.TAG_INT_ARRAY;
        } else {
            try {
                throw new IOException("The NBT data is invalid: Could not create a tagId from the given value: " +
                        value.getClass().getSimpleName());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return NBTConstants.TAG_END;
    }

    /**
     * Guarantees the capacity of this {@link io.netty.buffer.ByteBuf} buffer
     *
     * @param bufferCapacity which has to be validated
     */
    private void guaranteeBufferCapacity(final int bufferCapacity) {
        final int targetCapacity = this.buffer.writerIndex() + bufferCapacity;

        // capacity guaranteed
        if (targetCapacity <= this.buffer.capacity()) {
            return;
        }

        final int maxFastWritableBytes = this.buffer.maxFastWritableBytes();
        final int maxSize = 10 * 1024 * 1024;

        final int newCapacity = maxFastWritableBytes >= bufferCapacity ?
                (this.buffer.writerIndex() + maxFastWritableBytes) :
                this.buffer.alloc().calculateNewCapacity(targetCapacity, maxSize);

        this.buffer.capacity(newCapacity);
    }
}