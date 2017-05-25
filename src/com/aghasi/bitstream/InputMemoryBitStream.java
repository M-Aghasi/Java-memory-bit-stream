package com.aghasi.bitstream;

import java.io.UnsupportedEncodingException;

public class InputMemoryBitStream {
    
    final private int _capacity;
    private int _head;
    final private byte[] _buffer;
    
    public InputMemoryBitStream(byte[] buffer, int bitCount) {
        
        _buffer = buffer;
        _capacity = buffer.length;
        _head = 0;
    }
    
    public int readInt(int bitCount) {
        
        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        byte[] resBytes = new byte[byteCount];
        
        int bitsToRead = (bitCount % 8 == 0) ? 8:bitCount % 8;
        resBytes[0] = readBits(bitsToRead);
        
        int i = 1;
        while( i < byteCount ){
                resBytes[i] = readBits(8);
                ++i;
        }
        return bytesToInt(resBytes);
    }

    public float readFloat(int bitCount) {

        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        byte[] resBytes = new byte[byteCount];

        int bitsToRead = (bitCount % 8 == 0) ? 8:bitCount % 8;
        resBytes[0] = readBits(bitsToRead);

        int i = 1;
        while( i < byteCount ){
            resBytes[i] = readBits(8);
            ++i;
        }
        return Float.intBitsToFloat(bytesToInt(resBytes));
    }


    public long readLong(int bitCount) {

        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        byte[] resBytes = new byte[byteCount];

        int bitsToRead = (bitCount % 8 == 0) ? 8:bitCount % 8;
        resBytes[0] = readBits(bitsToRead);

        int i = 1;
        while( i < byteCount ){
            resBytes[i] = readBits(8);
            ++i;
        }
        return bytesToLong(resBytes);
    }

    public double readDouble(int bitCount) {

        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        byte[] resBytes = new byte[byteCount];

        int bitsToRead = (bitCount % 8 == 0) ? 8:bitCount % 8;
        resBytes[0] = readBits(bitsToRead);

        int i = 1;
        while( i < byteCount ){
            resBytes[i] = readBits(8);
            ++i;
        }
        return Double.longBitsToDouble(bytesToLong(resBytes));
    }
    
    public int readSignedInt(int bitCount) {
        boolean isNegative = readBool();
        int res = readInt(bitCount);
        if(isNegative) {
            res *= -1;
        }
        return res;
    }

    public long readSignedLong(int bitCount) {
        boolean isNegative = readBool();
        long res = readLong(bitCount);
        if(isNegative) {
            res *= -1;
        }
        return res;
    }
    
    public boolean readBool() {
        return (readBits(1) == 1);
    }

    public byte readByte() {
        return readBits(8);
    }

    public String readString() throws UnsupportedEncodingException {
        int bytesCount = readInt(32);
        byte[] resArr = new byte[bytesCount];
        for (int i=0 ; i<bytesCount ; i++) {
            resArr[i] = readBits(8);
        }
        return new String(resArr, "UTF-8");
    }
    
    private byte readBits(int bitCount) {
                
        int byteOffset = _head >> 3;
        int bitOffset = _head & 0x7;
        byte result = (byte) (Byte.toUnsignedInt(_buffer[byteOffset]) << bitOffset);
        
        int bitsFreeThisByte = 8 - bitOffset;
        if( bitsFreeThisByte < bitCount ) {
            int temp2 = (Byte.toUnsignedInt(_buffer[ byteOffset + 1 ]) >>> bitsFreeThisByte);
            result = (byte) (Byte.toUnsignedInt(result) | temp2);
	}
        result = (byte) (Byte.toUnsignedInt(result) >>> (8-bitCount));
        _head += bitCount;
        return result;
    }
    
    private int bytesToInt(byte[] num) {
        
        int result = 0;
        for(int i=0 ; i < num.length; i++) {
            result |= (Byte.toUnsignedInt(num[i]) << ((num.length-i-1)*8));
        }
        return result;
    }

    private long bytesToLong(byte[] num) {

        long result = 0;
        for(int i=0 ; i < num.length; i++) {
            result |= (Byte.toUnsignedLong(num[i]) << ((num.length-i-1)*8));
        }
        return result;
    }
    
    public byte[] getBuffer() {
        return _buffer;
    }
    
    public int getRemainingBitCount() {
        return _capacity - _head;
    }
}
