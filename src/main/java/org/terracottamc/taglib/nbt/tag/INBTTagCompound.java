package org.terracottamc.taglib.nbt.tag;

import io.netty.buffer.ByteBuf;

import java.io.File;
import java.nio.ByteOrder;
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
public interface INBTTagCompound {

    /**
     * Writes this {@link INBTTagCompound} with the given buffer and {@link ByteOrder}
     *
     * @param buffer    which is used to write the {@link INBTTagCompound}
     * @param byteOrder which is used to set the order the bytes are written
     */
    void write(final ByteBuf buffer, final ByteOrder byteOrder);

    /**
     * Writes this {@link INBTTagCompound} to the given file with given {@link ByteOrder}
     *
     * @param file      which represents the target the {@link INBTTagCompound} should be written to
     * @param byteOrder which is used to set the order the bytes are written
     */
    void write(final File file, final ByteOrder byteOrder);

    /**
     * Writes this {@link INBTTagCompound} and flushes all data from this
     * {@link INBTTagCompound} with the given buffer and {@link ByteOrder}
     *
     * @param buffer    which is used to write the {@link INBTTagCompound}
     * @param byteOrder which is used to set the order the bytes are written
     */
    void writeAndFlush(final ByteBuf buffer, final ByteOrder byteOrder);

    /**
     * Writes this {@link INBTTagCompound} and flushes all data from this
     * {@link INBTTagCompound} to the given file with given {@link ByteOrder}
     *
     * @param file      which represents the target the {@link INBTTagCompound} should be written to
     * @param byteOrder which is used to set the order the bytes are written
     */
    void writeAndFlush(final File file, final ByteOrder byteOrder);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the byte value that should be added
     */
    void setValue(final String key, final byte value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the short value that should be added
     */
    void setValue(final String key, final short value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the int value that should be added
     */
    void setValue(final String key, final int value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the long value that should be added
     */
    void setValue(final String key, final long value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the float value that should be added
     */
    void setValue(final String key, final float value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the double value that should be added
     */
    void setValue(final String key, final double value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the String value that should be added
     */
    void setValue(final String key, final String value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the byte array value that should be added
     */
    void setValue(final String key, final byte[] value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the int array value that should be added
     */
    void setValue(final String key, final int[] value);

    /**
     * Adds a new value with given key to this {@link INBTTagCompound}
     *
     * @param key   which stands for the holder of the value
     * @param value which represents the list value that should be added
     */
    void setValue(final String key, final List<Object> value);

    /**
     * Adds a new child tag to this {@link INBTTagCompound}
     *
     * @param childCompound which should be added to this {@link INBTTagCompound}
     */
    void setChildTag(final NBTTagCompound childCompound);

    /**
     * Retrieves the byte value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh byte value
     */
    byte getByte(final String key);

    /**
     * Retrieves the short value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh short value
     */
    short getShort(final String key);

    /**
     * Retrieves the int value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh int value
     */
    int getInt(final String key);

    /**
     * Retrieves the long value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh long value
     */
    long getLong(final String key);

    /**
     * Retrieves the float value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh float value
     */
    float getFloat(final String key);

    /**
     * Retrieves the double value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh double value
     */
    double getDouble(final String key);

    /**
     * Retrieves the {@link String} value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh {@link String} value
     */
    String getString(final String key);

    /**
     * Retrieves the byte array value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh byte array value
     */
    byte[] getByteArray(final String key);

    /**
     * Retrieves the int array value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh int array value
     */
    int[] getIntArray(final String key);

    /**
     * Retrieves the {@link java.util.List} value by its given key
     *
     * @param key which is needed to find the value
     *
     * @return a fresh {@link java.util.List} value
     */
    List<?> getList(final String key);

    /**
     * Retrieves the {@link NBTTagCompound} child tag by its name
     *
     * @param name which is needed to find the child tag
     *
     * @return a fresh {@link NBTTagCompound}
     */
    NBTTagCompound getChildTag(final String name);
}