package wint.lang.codec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {

	/**
     * Returns a MessageDigest for the given <code>algorithm</code>.
     *
     * @param algorithm The MessageDigest algorithm name.
     * @return An MD5 digest instance.
     * @throws RuntimeException when a {@link java.security.NoSuchAlgorithmException} is caught,
     */
    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns an MD5 MessageDigest.
     *
     * @return An MD5 digest instance.
     * @throws RuntimeException when a {@link java.security.NoSuchAlgorithmException} is caught,
     */
    private static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    /**
     * Returns an SHA digest.
     *
     * @return An SHA digest instance.
     * @throws RuntimeException when a {@link java.security.NoSuchAlgorithmException} is caught,
     */
    private static MessageDigest getShaDigest() {
        return getDigest("SHA");
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element 
     * <code>byte[]</code>.
     *
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data) {
        return getMd5Digest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element 
     * <code>byte[]</code>.
     *
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data) {
        return md5(data.getBytes());
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character 
     * hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data) {
        return new String(Hex.encodeHex(md5(data)));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character 
     * hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        return new String(Hex.encodeHex(md5(data)));
    }

    /**
     * Calculates the SHA digest and returns the value as a 
     * <code>byte[]</code>.
     *
     * @param data Data to digest
     * @return SHA digest
     */
    public static byte[] sha(byte[] data) {
        return getShaDigest().digest(data);
    }

    /**
     * Calculates the SHA digest and returns the value as a 
     * <code>byte[]</code>.
     *
     * @param data Data to digest
     * @return SHA digest
     */
    public static byte[] sha(String data) {
        return sha(data.getBytes());
    }

    /**
     * Calculates the SHA digest and returns the value as a hex string.
     *
     * @param data Data to digest
     * @return SHA digest as a hex string
     */
    public static String shaHex(byte[] data) {
        return new String(Hex.encodeHex(sha(data)));
    }

    /**
     * Calculates the SHA digest and returns the value as a hex string.
     *
     * @param data Data to digest
     * @return SHA digest as a hex string
     */
    public static String shaHex(String data) {
        return new String(Hex.encodeHex(sha(data)));
    }
	
}
