package org.terracottamc.taglib.nbt.io;

import io.netty.buffer.ByteBuf;
import org.terracottamc.taglib.util.VarIntUtil;
import org.terracottamc.taglib.util.allocation.AllocationLimitReachedException;
import org.terracottamc.taglib.util.deserializer.StringDeserializer;

import java.io.IOException;
import java.nio.ByteOrder;

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
public abstract class NBTStreamReader {

    private final ByteBuf buffer;
    private final ByteOrder byteOrder;

    private boolean useVarInt = false;
    private int allocationLimit = -1;

    /**
     * Creates a new {@link org.terracottamc.taglib.nbt.io.NBTStreamReader} with given
     * {@link io.netty.buffer.ByteBuf} and {@link java.nio.ByteOrder}
     *
     * @param buffer    which represents the buffer to work with
     * @param byteOrder which represents the order of the bytes to handle
     */
    protected NBTStreamReader(final ByteBuf buffer, final ByteOrder byteOrder) {
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
     * Set the used allocation limit
     *
     * @param allocationLimit which should be set
     */
    public void setAllocationLimit(final int allocationLimit) {
        this.allocationLimit = allocationLimit;
    }

    /**
     * This method is present to reduce the allocation limit
     *
     * @param remaining which represents the reduction value
     *
     * @throws org.terracottamc.taglib.util.allocation.AllocationLimitReachedException which can be thrown when the allocation limit has been reached
     */
    protected void doAlterAllocationLimit(final int remaining) throws AllocationLimitReachedException {
        if (this.allocationLimit != -1) {
            if ((this.allocationLimit - remaining) < 0) {
                throw new AllocationLimitReachedException("Could not allocate more bytes because the allocation limit has been reached");
            } else {
                this.allocationLimit -= remaining;
            }
        }
    }

    /**
     * Proofs the expected input
     *
     * @param remaining            that represents the amount remaining data
     * @param failureMessage       which is the message that will be shown when an exception occurs
     * @param alterAllocationLimit whether the allocation limit should be altered or not
     */
    protected void checkForExpectedInput(final int remaining, final String failureMessage, final boolean alterAllocationLimit) {
        if (alterAllocationLimit) {
            try {
                this.doAlterAllocationLimit(remaining);
            } catch (final AllocationLimitReachedException e) {
                e.printStackTrace();
            }
        }

        if (this.buffer.readableBytes() < remaining) {
            try {
                throw new IOException(failureMessage);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkForExpectedInput(final int remaining, final String failureMessage) {
        this.checkForExpectedInput(remaining, failureMessage, true);
    }

    public byte readByteValue() {
        this.checkForExpectedInput(1, "The NBT data is invalid: A byte value was expected");

        return this.buffer.readByte();
    }

    protected short readShortValue() {
        this.checkForExpectedInput(2, "The NBT data is invalid: A short value was expected");

        return this.byteOrder == ByteOrder.LITTLE_ENDIAN ? this.buffer.readShortLE() : this.buffer.readShort();
    }

    protected int readIntValue() {
        if (this.useVarInt) {
            return VarIntUtil.readVarInt(this);
        }

        this.checkForExpectedInput(4, "The NBT data is invalid: An int value was expected");

        return this.byteOrder == ByteOrder.LITTLE_ENDIAN ? this.buffer.readIntLE() : this.buffer.readInt();
    }

    protected long readLongValue() {
        if (this.useVarInt) {
            return VarIntUtil.readVarLong(this).longValue();
        }

        this.checkForExpectedInput(8, "The NBT data is invalid: A long value was expected");

        return this.byteOrder == ByteOrder.LITTLE_ENDIAN ? this.buffer.readLongLE() : this.buffer.readLong();
    }

    protected float readFloatValue() {
        this.checkForExpectedInput(4, "The NBT data is invalid: A float value was expected");

        return this.byteOrder == ByteOrder.LITTLE_ENDIAN ? this.buffer.readFloatLE() : this.buffer.readFloat();
    }

    protected double readDoubleValue() {
        this.checkForExpectedInput(8, "The NBT data is invalid: A double value was expected");

        return this.byteOrder == ByteOrder.LITTLE_ENDIAN ? this.buffer.readDoubleLE() : this.buffer.readDouble();
    }

    protected String readStringValue() {
        final int length = this.useVarInt ? VarIntUtil.readUnsignedVarInt(this) : this.readShortValue();

        this.checkForExpectedInput(length, "The NBT data is invalid: A String value was expected");

        final byte[] bytes = new byte[length];

        this.buffer.readBytes(bytes);

        return new StringDeserializer().deserialize(bytes, 0, length);
    }

    protected byte[] readByteArrayValue() {
        final int length = this.readIntValue();

        this.checkForExpectedInput(length, "The NBT data is invalid: A byte array value was expected");

        final byte[] bytes = new byte[length];

        this.buffer.readBytes(bytes);

        return bytes;
    }

    protected int[] readIntArrayValue() {
        final int length = this.readIntValue();

        this.checkForExpectedInput((this.useVarInt ? length : (length * 4)), "The NBT data is invalid: An int array value was expected");

        final int[] ints = new int[length];

        for (int i = 0; i < length; i++) {
            ints[i] = this.readIntValue();
        }

        return ints;
    }
}