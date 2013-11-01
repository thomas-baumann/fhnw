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
		// init Key => g0
		byte[] g = {0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55};
		
		// get Bytes from Text
		byte[] m = plainText.getBytes();
		
		// save the length of the message
		int ml = m.length;
		
		// check if padding is necessary, do it
		if (m.length % 8 != 0) {
			int newLength = m.length >> 3; // factor 8
			newLength = (newLength+1) << 3;
			byte[] tmp = new byte[newLength];
			System.arraycopy(m, 0, tmp, 0, m.length);
			tmp[m.length] = Byte.MIN_VALUE;
			for (int i = m.length+1; i < tmp.length; i++) {
				tmp[i] = 0;
			}
			m = tmp;
		}
		
		// add the length of the message, do it
		byte[] tmp = new byte[m.length + 8];
		System.arraycopy(m, 0, tmp, 0, m.length);
		byte[] mba = ByteBuffer.allocate(8).putLong(ml).array();
		for (int i = 0; i < mba.length; i++) {
			tmp[tmp.length-mba.length-1+i] = mba[i];
		}
		m = tmp;
		
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new DESEngine());
		for (int i = 0; i < m.length; i+=cipher.getBlockSize()) {	// incrementally increase by the BlockSize
			cipher.init(true, new KeyParameter(g));
			byte[] block = new byte[cipher.getOutputSize(cipher.getBlockSize())];	// output of the cipher
			cipher.processBytes(m, i, cipher.getBlockSize(), block, 0);	// process the next block
			
			// with doFinal, the cipher produces actual output
			try {
				cipher.doFinal(block, 0);
			} catch (DataLengthException | IllegalStateException
					| InvalidCipherTextException e) {
				e.printStackTrace();
			}
			
			// calculate new key
			for (int j = 0; j < g.length; j++) {
				// xor in block because des generates works with two 64-Bit blocks
				g[j] = (byte) (g[j] ^ (block[j] ^ block[j+8]));
			}
		}
		
		// last process step
		byte[] H = new byte[4];
		for (int i = 0; i < H.length; i++) {
			H[i] = (byte) (g[i] ^ g[g.length-1-i]);
		}
		
		// wrap the array in a buffer and magically get an int out of the buffer
		ByteBuffer buffer = ByteBuffer.wrap(H);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
	}
	
}
