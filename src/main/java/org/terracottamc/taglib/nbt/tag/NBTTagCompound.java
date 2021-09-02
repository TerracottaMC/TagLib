package org.terracottamc.taglib.nbt.tag;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.terracottamc.taglib.NBTBuilder;
import org.terracottamc.taglib.nbt.io.NBTWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class NBTTagCompound implements INBTTagCompound {

    private final Map<String, Object> nbtMap = new HashMap<>();

    private String name = null;

    /**
     * Creates a new {@link NBTTagCompound}
     */
    public NBTTagCompound() {

    }

    /**
     * Creates a new {@link NBTTagCompound} with given name
     *
     * @param name that represents the name of this {@link NBTTagCompound}
     */
    public NBTTagCompound(final String name) {
        this.name = name;
    }

    @Override
    public void write(final ByteBuf buffer, final ByteOrder byteOrder) {
        final NBTWriter nbtWriter = new NBTBuilder()
                .withIOBuffer(buffer)
                .withByteOrder(byteOrder)
                .buildWriter();

        nbtWriter.writeTagCompound(this);
    }

    @Override
    public void write(final File file, final ByteOrder byteOrder) {
        try (final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            final ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer();

            this.write(buffer, byteOrder);

            final int length = buffer.readableBytes();
            final byte[] data = new byte[length];

            buffer.readBytes(data);
            buffer.release();

            fileOutputStream.write(data);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeAndFlush(final ByteBuf buffer, final ByteOrder byteOrder) {
        this.write(buffer, byteOrder);

        this.nbtMap.clear();
    }

    @Override
    public void writeAndFlush(final File file, final ByteOrder byteOrder) {
        this.write(file, byteOrder);

        this.nbtMap.clear();
    }

    @Override
    public void setValue(final String key, final byte value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final short value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final int value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final long value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final float value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final double value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final String value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final byte[] value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final int[] value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setValue(final String key, final List<Object> value) {
        this.nbtMap.put(key, value);
    }

    @Override
    public void setChildTag(final NBTTagCompound childCompound) {
        this.nbtMap.put(childCompound.getName(), childCompound);
    }

    @Override
    public byte getByte(final String key) {
        return (byte) this.nbtMap.get(key);
    }

    @Override
    public short getShort(final String key) {
        return (short) this.nbtMap.get(key);
    }

    @Override
    public int getInt(final String key) {
        return (int) this.nbtMap.get(key);
    }

    @Override
    public long getLong(final String key) {
        return (long) this.nbtMap.get(key);
    }

    @Override
    public float getFloat(final String key) {
        return (float) this.nbtMap.get(key);
    }

    @Override
    public double getDouble(final String key) {
        return (double) this.nbtMap.get(key);
    }

    @Override
    public String getString(final String key) {
        return (String) this.nbtMap.get(key);
    }

    @Override
    public byte[] getByteArray(final String key) {
        return (byte[]) this.nbtMap.get(key);
    }

    @Override
    public int[] getIntArray(final String key) {
        return (int[]) this.nbtMap.get(key);
    }

    @Override
    public List<?> getList(final String key) {
        return (List<?>) this.nbtMap.get(key);
    }

    @Override
    public NBTTagCompound getChildTag(final String name) {
        return (NBTTagCompound) this.nbtMap.get(name);
    }

    /**
     * Updates the name of this {@link NBTTagCompound}
     *
     * @param name which should be updated
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of this {@link NBTTagCompound}
     *
     * @return a fresh {@link String}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the nbt entries of this {@link org.terracottamc.taglib.nbt.tag.NBTTagCompound}
     *
     * @return a fresh {@link java.util.Set} of {@link java.util.Map} entries
     */
    public Set<Map.Entry<String, Object>> retrieveNBTEntries() {
        return this.nbtMap.entrySet();
    }
}