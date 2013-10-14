package lab1;

import java.security.SecureRandom;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.PaddedBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;

public class Test {

	static String keyString = "asfasdff";
	static String inputString = "halloddie geht es? gi";

	public static void main(String[] args) {
		// byte[] cipher = example(inputString.getBytes(), true);
		// byte[] res = example(cipher, false);
		// System.out.println(new String(res));

		// System.out.println();
		System.out.println(inputString.getBytes().length);
		byte[] cipherText = encrypte(inputString.getBytes());
		// byte[] plainResult = decrypte(cipherText);
		// System.out.println(new String(plainResult));
	}

	private static byte[] encrypte(byte[] plainText) {
		byte[] key = new byte[8];
		for (int i = 0; i < key.length; i++) {
			key[i] = 0b01010101;
		}
		byte[] input = plainText;

		BlockCipher engine = new DESEngine();
		PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(engine, new BlockCipherPadding() {

			@Override
			public int padCount(byte[] in) throws InvalidCipherTextException {
				System.out.println("padcount");
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void init(SecureRandom random) throws IllegalArgumentException {
				// TODO Auto-generated method stub

			}

			@Override
			public String getPaddingName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int addPadding(byte[] in, int inOff) {
				System.out.println(in.length);
				System.out.println(new String(in));
				System.out.println(inOff);
				// TODO Auto-generated method stub
				return 0;
			}
		});

		pbbc.init(true, new KeyParameter(key));

		byte[] cipherText = new byte[pbbc.getOutputSize(input.length)];

		int outputLen = pbbc.processBytes(input, 0, input.length, cipherText, 0);
		try {
			pbbc.doFinal(cipherText, outputLen);
		} catch (CryptoException ce) {
			System.err.println(ce);
			System.exit(1);
		}

		for (int i = 0; i < cipherText.length; i++) {
			System.out.print(cipherText[i]);
		}
		System.out.println();

		return cipherText;
	}

	private static byte[] decrypte(byte[] cipherText) {
		byte[] key = new byte[8];
		for (int i = 0; i < key.length; i++) {
			key[i] = 0b01010101;
		}

		BlockCipher engine = new DESEngine();
		PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(engine, new ZeroBytePadding());

		pbbc.init(false, new KeyParameter(key));

		byte[] plainText = new byte[pbbc.getOutputSize(cipherText.length)];

		int outputLen = pbbc.processBytes(cipherText, 0, cipherText.length, plainText, 0);
		try {
			pbbc.doFinal(cipherText, outputLen);
		} catch (CryptoException ce) {
			System.err.println(ce);
			System.exit(1);
		}

		for (int i = 0; i < plainText.length; i++) {
			System.out.print(plainText[i]);
		}
		System.out.println();

		return plainText;
	}

	private static byte[] example(byte[] input, boolean forEncryption) {
		/*
		 * This will use a supplied key, and encrypt the data This is the
		 * equivalent of DES/CBC/PKCS5Padding
		 */
		BlockCipher engine = new DESEngine();
		BufferedBlockCipher cipher = new PaddedBlockCipher(new CBCBlockCipher(engine));

		byte[] key = keyString.getBytes();

		cipher.init(forEncryption, new KeyParameter(key));

		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

		int outputLen = cipher.processBytes(input, 0, input.length, cipherText, 0);
		try {
			cipher.doFinal(cipherText, outputLen);
		} catch (CryptoException ce) {
			System.err.println(ce);
			System.exit(1);
		}

		return cipherText;
	}

}
