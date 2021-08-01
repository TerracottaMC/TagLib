package org.terracottamc.taglib.util;

import org.terracottamc.taglib.nbt.io.NBTStreamReader;
import org.terracottamc.taglib.nbt.io.NBTWriter;

import java.math.BigInteger;

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
public class VarIntUtil {

    public static int readVarInt(final NBTStreamReader nbtStreamReader) {
        final long value = VarIntUtil.readUnsignedVarLong(nbtStreamReader);

        return VarIntUtil.deserializeZigZag32(value);
    }

    public static void writeVarInt(final NBTWriter nbtWriter, final int value) {
        final long l = VarIntUtil.serializeZigZag32(value);

        VarIntUtil.writeUnsignedVarLong(nbtWriter, l);
    }

    public static BigInteger readVarLong(final NBTStreamReader nbtStreamReader) {
        final BigInteger value = VarIntUtil.readVarNumber(nbtStreamReader);

        return VarIntUtil.deserializeZigZag64(value);
    }

    public static void writeVarLong(final NBTWriter nbtWriter, final long value) {
        final BigInteger bigInteger = VarIntUtil.serializeZigZag64(value);

        VarIntUtil.writeVarNumber(nbtWriter, bigInteger);
    }

    public static int readUnsignedVarInt(final NBTStreamReader nbtStreamReader) {
        int readInt = 0;
        int amountOfBytes = 0;
        byte byteValue;

        do {
            byteValue = nbtStreamReader.readByteValue();
            readInt |= (byteValue & 0x7f) << (amountOfBytes++ * 7);

            if (amountOfBytes > 6) {
                throw new RuntimeException("read VarInt is too big");
            }
        } while ((byteValue & 0x80) == 0x80);

        return readInt;
    }

    public static void writeUnsignedVarInt(final NBTWriter nbtWriter, int value) {
        while ((value & 0xffffff80) != 0) {
            nbtWriter.writeByteValue((byte) (value & 0x7f | 0x80));

            value >>>= 7;
        }

        nbtWriter.writeByteValue((byte) value);
    }

    private static int deserializeZigZag32(final long value) {
        return (int) (value >> 1) ^ -((int) (value & 1));
    }

    private static long serializeZigZag32(final int value) {
        return ((long) value << 1 ^ value >> 31);
    }

    private static BigInteger deserializeZigZag64(final BigInteger value) {
        final BigInteger leftShifted = value.shiftRight(1);
        final BigInteger rightShifted = value.and(BigInteger.ONE).negate();

        return leftShifted.xor(rightShifted);
    }

    private static BigInteger serializeZigZag64(final long value) {
        final BigInteger origin = BigInteger.valueOf(value);
        final BigInteger leftShifted = origin.shiftLeft(1);
        final BigInteger rightShifted = origin.shiftRight(63);

        return leftShifted.xor(rightShifted);
    }

    private static BigInteger readVarNumber(final NBTStreamReader nbtStreamReader) {
        BigInteger result = BigInteger.ZERO;
        int offset = 0;
        int byteValue;

        do {
            if (offset >= 10) {
                throw new RuntimeException("read VarNumber is too big");
            }

            byteValue = nbtStreamReader.readByteValue();
            result = result.or(BigInteger.valueOf((long) (byteValue & 0x7f) << (offset * 7)));

            offset++;
        } while ((byteValue & 0x80) > 0);

        return result;
    }

    private static void writeVarNumber(final NBTWriter nbtWriter, BigInteger value) {
        final BigInteger unsigned_long_max_value = new BigInteger("FFFFFFFFFFFFFFFF", 16);

        if (value.compareTo(unsigned_long_max_value) > 0) {
            throw new IllegalArgumentException("The value is too big");
        }

        value = value.and(unsigned_long_max_value);

        final BigInteger i = BigInteger.valueOf(0xffffff80);
        final BigInteger x7f = BigInteger.valueOf(0x7f);
        final BigInteger x80 = BigInteger.valueOf(0x80);

        while (!value.and(i).equals(BigInteger.ZERO)) {
            nbtWriter.writeByteValue(value.and(x7f).or(x80).byteValue());

            value = value.shiftRight(7);
        }

        nbtWriter.writeByteValue(value.byteValue());
    }

    private static long readUnsignedVarLong(final NBTStreamReader nbtStreamReader) {
        long readLong = 0L;
        int amountOfBytes = 0;
        byte byteValue;

        do {
            byteValue = nbtStreamReader.readByteValue();
            readLong |= (long) (byteValue & 0x7f) << (amountOfBytes++ * 7);

            if (amountOfBytes > 7) {
                throw new RuntimeException("read VarInt is too big");
            }
        } while ((byteValue & 0x80) == 0x80);

        return readLong;
    }

    private static void writeUnsignedVarLong(final NBTWriter nbtWriter, long value) {
        while ((value & 0xffffffffffffff80L) != 0) {
            nbtWriter.writeByteValue((byte) ((int) (value & 0x7f | 0x80)));

            value >>>= 7;
        }

        nbtWriter.writeByteValue((byte) ((int) value));
    }
}