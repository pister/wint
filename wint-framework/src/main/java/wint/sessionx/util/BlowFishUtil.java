package wint.sessionx.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wint.lang.codec.DigestUtils;
import wint.lang.utils.MapUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;

public class BlowFishUtil {

	private static final Logger log = LoggerFactory.getLogger(BlowFishUtil.class);

	private static final String CIPHER_NAME = "Blowfish/CFB8/NoPadding";
	private static final String KEY_SPEC_NAME = "Blowfish";

	private static final ThreadLocal<HashMap<String, BlowFishUtil>> pool = new ThreadLocal<HashMap<String, BlowFishUtil>>();

	private Cipher enCipher;

	private Cipher deCipher;

	private String key;

	private BlowFishUtil(String key) {
		try {
			this.key = key;
			String iv = StringUtils.substring(DigestUtils.md5Hex(key), 0, 8);
			String secret =  StringUtils.substring(DigestUtils.md5Hex(key), 0, 16);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
			SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), KEY_SPEC_NAME);
			enCipher = Cipher.getInstance(CIPHER_NAME);
			deCipher = Cipher.getInstance(CIPHER_NAME);
			enCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			deCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (Exception e) {
			log.error("初始化BlowfishUtils失败", e);
			throw new RuntimeException(e);
		}
	}

	public static String encryptBlowfish(String s, String key) {
		return getInstance(key).encrypt(s);
	}

	public static String decryptBlowfish(String s, String key) {
		return getInstance(key).decrypt(s);
	}

	private static BlowFishUtil getInstance(String key) {
		HashMap<String, BlowFishUtil> keyMap = pool.get();
		if (MapUtil.isEmpty(keyMap)) {
			keyMap = new HashMap<String, BlowFishUtil>();
			pool.set(keyMap);
		}
		BlowFishUtil instance = (BlowFishUtil) keyMap.get(key);
		if (instance == null || !StringUtils.equals(instance.key, key)) {
			instance = new BlowFishUtil(key);
			keyMap.put(key, instance);
		}
		return instance;
	}

	private void resetInstance() {
		pool.set(null);
	}

	/**
	 * 加密的方法
	 * 
	 * @param s
	 * @return
	 */
	private String encrypt(String s) {
		String result = null;

		if (StringUtils.isNotBlank(s)) {
			try {
				byte[] encrypted = enCipher.doFinal(s.getBytes());
				result = new String(BlowFishBase64.encodeBase64(encrypted));
			} catch (Exception e) {
				resetInstance(); // 抛弃当前对象，防止enCipher出现中间状态
				log.warn("加密失败", e);
			}
		}

		return result;
	}

	/**
	 * 解密的方法
	 * 
	 * @param s
	 * @return
	 */
	private String decrypt(String s) {
		String result = null;

		if (StringUtils.isNotBlank(s)) {
			try {
				byte[] decrypted = BlowFishBase64.decodeBase64(s.getBytes());
				result = new String(deCipher.doFinal(decrypted));
			} catch (Exception e) {
				resetInstance(); // 抛弃当前对象，防止deCipher出现中间状态
				log.warn("解密失败", e);
			}
		}

		return result;
	}

}
