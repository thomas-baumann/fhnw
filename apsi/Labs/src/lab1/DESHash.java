package lab1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class DESHash {
	public int hash(String plainText) {
		// init Key => g0 (0x55 = 0b01010101)
		byte[] g = { 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55 };

		// get Bytes from Text
		byte[] m = plainText.getBytes();

		// save the length of the message
		int ml = m.length;

		// check if padding is necessary, do it
		if (m.length % 8 != 0) {
			int newLength = m.length >> 3; // factor 8
			newLength = (newLength + 1) << 3;
			byte[] tmp = new byte[newLength];
			System.arraycopy(m, 0, tmp, 0, m.length);
			tmp[m.length] = Byte.MIN_VALUE;
			for (int i = m.length + 1; i < tmp.length; i++) {
				tmp[i] = 0;
			}
			m = tmp;
			ml = m.length;
		}

		// add the length of the message
		byte[] tmp = new byte[m.length + 8];
		System.arraycopy(m, 0, tmp, 0, m.length);
		// mba contains the length of the message
		byte[] mba = ByteBuffer.allocate(8).putLong(ml).array();
		for (int i = 0; i < mba.length; i++) {
			tmp[tmp.length - mba.length - 1 + i] = mba[i];
		}
		m = tmp;

		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new DESEngine());
		// incrementally increase by the BlockSize on each iteration
		for (int i = 0; i < m.length; i += cipher.getBlockSize()) {
			cipher.init(true, new KeyParameter(g));

			// output of the cipher
			byte[] block = new byte[cipher.getOutputSize(cipher.getBlockSize())];

			// process the next block
			cipher.processBytes(m, i, cipher.getBlockSize(), block, 0);

			// with doFinal, the cipher produces actual output
			try {
				cipher.doFinal(block, 0);
			} catch (DataLengthException | IllegalStateException | InvalidCipherTextException e) {
				e.printStackTrace();
			}

			// calculate new key
			for (int j = 0; j < g.length; j++) {
				// xor in block because des generates works with two 64-Bit blocks
				g[j] = (byte) (g[j] ^ (block[j] ^ block[j + 8]));
			}
		}

		// build long of the byte array
		ByteBuffer buffer = ByteBuffer.wrap(g);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		long hash = buffer.getLong();

		int h1 = (int) (hash >>> 32); // first 32 bits
		int h2 = Integer.reverse((int) hash); // second 32 bits in reverse order

		return h1 ^ h2;
	}

}
