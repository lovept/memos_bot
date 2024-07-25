package com.github.lovept.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author lovept
 * @date 2024/7/23 14:30
 * @description memos uuid 生成
 */
public class UUIDGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int BASE = ALPHABET.length();

    public static String newEncodedUUID() {
        UUID uuid = newUUID();
        return encodeUUID(uuid);
    }

    public static UUID newUUID() {
        try {
            return newRandomUUID();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UUID newRandomUUID() throws IOException {
        return newRandomFromReader(new ByteArrayInputStream(SECURE_RANDOM.generateSeed(16)));
    }

    public static UUID newRandomFromReader(InputStream inputStream) throws IOException {
        byte[] uuidBytes = new byte[16];
        int bytesRead = inputStream.read(uuidBytes);
        if (bytesRead != 16) {
            throw new IOException("Could not read enough bytes for UUID");
        }

        // Set the version to 4
        uuidBytes[6] = (byte) ((uuidBytes[6] & 0x0f) | 0x40);

        // Set the variant to 10
        uuidBytes[8] = (byte) ((uuidBytes[8] & 0x3f) | 0x80);

        long mostSigBits = 0;
        long leastSigBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (uuidBytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (uuidBytes[i] & 0xff);
        }

        return new UUID(mostSigBits, leastSigBits);
    }

    public static String encodeUUID(UUID uuid) {
        String hexString = uuid.toString().replace("-", "");
        BigInteger num = new BigInteger(hexString, 16);
        return numToString(num);
    }

    private static String numToString(BigInteger num) {
        StringBuilder sb = new StringBuilder();
        BigInteger base = BigInteger.valueOf(BASE);
        while (num.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = num.divideAndRemainder(base);
            sb.append(ALPHABET.charAt(divmod[1].intValue()));
            num = divmod[0];
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        String encodedUUID = newEncodedUUID();
        System.out.println("Generated Encoded UUID: " + encodedUUID);
    }
}


