package org.terracottamc.taglib.nbt.io;

import io.netty.buffer.ByteBuf;
import org.terracottamc.taglib.nbt.tag.NBTTagCompound;
import org.terracottamc.taglib.util.allocation.Allocation;
import org.terracottamc.taglib.util.allocation.AllocationLimitReachedException;
import org.terracottamc.taglib.util.nbt.NBTConstants;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

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
public class NBTReader extends NBTStreamReader {

    public NBTReader(final ByteBuf buffer, final ByteOrder byteOrder) {
        super(buffer, byteOrder);
    }

    /**
     * Retrieves the {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound} which has been read by {@link NBTReader#readTagCompoundValue()}
     *
     * @return a fresh {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound}
     */
    public NBTTagCompound createCompound() {
        this.checkForExpectedInput((this.isUsingVarInt() ? 2 : 3), "The NBT data is invalid: There is not enough data available", false);

        final byte tagId = this.readByteValue();
        final String name = this.readStringValue();
        final NBTTagCompound nbtTagCompound = this.readTagCompoundValue();
        nbtTagCompound.setName(name);

        if (tagId != NBTConstants.TAG_COMPOUND) {
            try {
                throw new IOException("The NBT data is invalid: The TagCompound was not found");
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return nbtTagCompound;
    }

    /**
     * Retrieves the {@link java.util.List} of objects which has been read by {@link NBTReader#readTagListValue()}
     *
     * @return a fresh {@link java.util.List}
     */
    public List<Object> createList() {
        this.checkForExpectedInput((this.isUsingVarInt() ? 2 : 3), "The NBT data is invalid: There is not enough data available", false);

        final byte tagId = this.readByteValue();

        if (tagId != NBTConstants.TAG_LIST) {
            try {
                throw new IOException("The NBT data is invalid: The TagList was not found");
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        this.readStringValue();

        return this.readTagListValue();
    }

    /**
     * Reads a {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound}
     *
     * @return a fresh {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound}
     */
    private NBTTagCompound readTagCompoundValue() {
        try {
            this.doAlterAllocationLimit(Allocation.TAG_COMPOUND);
        } catch (final AllocationLimitReachedException e) {
            e.printStackTrace();
        }

        this.checkForExpectedInput(1, "The NBT data is invalid: A tag identifier was expected", false);

        byte tagId = this.readByteValue();
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        while (tagId != NBTConstants.TAG_END) {
            switch (tagId) {
                case NBTConstants.TAG_BYTE:
                    nbtTagCompound.setValue(this.readStringValue(), this.readByteValue());
                    break;
                case NBTConstants.TAG_SHORT:
                    nbtTagCompound.setValue(this.readStringValue(), this.readShortValue());
                    break;
                case NBTConstants.TAG_INT:
                    nbtTagCompound.setValue(this.readStringValue(), this.readIntValue());
                    break;
                case NBTConstants.TAG_LONG:
                    nbtTagCompound.setValue(this.readStringValue(), this.readLongValue());
                    break;
                case NBTConstants.TAG_FLOAT:
                    nbtTagCompound.setValue(this.readStringValue(), this.readFloatValue());
                    break;
                case NBTConstants.TAG_DOUBLE:
                    nbtTagCompound.setValue(this.readStringValue(), this.readDoubleValue());
                    break;
                case NBTConstants.TAG_BYTE_ARRAY:
                    nbtTagCompound.setValue(this.readStringValue(), this.readByteArrayValue());
                    break;
                case NBTConstants.TAG_STRING:
                    nbtTagCompound.setValue(this.readStringValue(), this.readStringValue());
                    break;
                case NBTConstants.TAG_LIST:
                    nbtTagCompound.setValue(this.readStringValue(), this.readTagListValue());
                    break;
                case NBTConstants.TAG_COMPOUND:
                    final String name = this.readStringValue();
                    final NBTTagCompound childCompound = this.readTagCompoundValue();
                    childCompound.setName(name);

                    nbtTagCompound.setChildTag(childCompound);
                    break;
                case NBTConstants.TAG_INT_ARRAY:
                    nbtTagCompound.setValue(this.readStringValue(), this.readIntArrayValue());
                    break;
            }

            tagId = this.readByteValue();
        }

        return nbtTagCompound;
    }

    /**
     * Reads a list nbt tag value
     *
     * @return a fresh {@link java.util.List}
     */
    private List<Object> readTagListValue() {
        this.checkForExpectedInput((this.isUsingVarInt() ? 2 : 5), "The NBT data is invalid: A TagList header was expected", false);

        final byte type = this.readByteValue();
        final int length = this.readIntValue();

        try {
            this.doAlterAllocationLimit(Allocation.ARRAY_LIST);
            this.doAlterAllocationLimit(Allocation.REFERENCE * length);
        } catch (final AllocationLimitReachedException e) {
            e.printStackTrace();
        }

        final List<Object> list = new ArrayList<>(length);

        switch (type) {
            case NBTConstants.TAG_END:
                break;
            case NBTConstants.TAG_BYTE:
                for (int i = 0; i < length; i++) {
                    list.add(this.readByteValue());
                }
                break;
            case NBTConstants.TAG_SHORT:
                for (int i = 0; i < length; i++) {
                    list.add(this.readShortValue());
                }
                break;
            case NBTConstants.TAG_INT:
                for (int i = 0; i < length; i++) {
                    list.add(this.readIntValue());
                }
                break;
            case NBTConstants.TAG_LONG:
                for (int i = 0; i < length; i++) {
                    list.add(this.readLongValue());
                }
                break;
            case NBTConstants.TAG_FLOAT:
                for (int i = 0; i < length; i++) {
                    list.add(this.readFloatValue());
                }
                break;
            case NBTConstants.TAG_DOUBLE:
                for (int i = 0; i < length; i++) {
                    list.add(this.readDoubleValue());
                }
                break;
            case NBTConstants.TAG_BYTE_ARRAY:
                for (int i = 0; i < length; i++) {
                    list.add(this.readByteArrayValue());
                }
                break;
            case NBTConstants.TAG_STRING:
                for (int i = 0; i < length; i++) {
                    list.add(this.readStringValue());
                }
                break;
            case NBTConstants.TAG_LIST:
                for (int i = 0; i < length; i++) {
                    list.add(this.readTagListValue());
                }
                break;
            case NBTConstants.TAG_COMPOUND:
                for (int i = 0; i < length; i++) {
                    list.add(this.readTagCompoundValue());
                }
                break;
            case NBTConstants.TAG_INT_ARRAY:
                for (int i = 0; i < length; i++) {
                    list.add(this.readIntArrayValue());
                }
                break;
            default:
                try {
                    throw new IOException("The NBT data is invalid: The tag " + type + " is unknown");
                } catch (final IOException e) {
                    e.printStackTrace();
                }
        }

        return list;
    }
}