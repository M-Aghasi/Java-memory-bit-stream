package com.aghasi.bitstream;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class OutputMemoryBitStream {

    private int _capacity;
    private int _head;
    private byte[] _buffer;

    private static final int INITIAL_BUFFER_SIZE = 600;

    public OutputMemoryBitStream() {
        _head = 0;
        reallocateBuffer(INITIAL_BUFFER_SIZE * 8);
    }
    
    public void writeInt(int data) {
        writeInt(data, 32);
    }

    public void writeInt(int data, int bitCount) {
        
        if(bitCount < 1) {
            return;
        }
        if(bitCount > 32) {
            bitCount = 32;
        }
        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        final byte[] srcByte = intToBytes(data, byteCount);
        int bitsToWrite = (bitCount % 8 == 0) ? 8:bitCount % 8;
        writeBits(srcByte[0], bitsToWrite);
        
        int i = 1;
        //write all the bytes
        while( i < byteCount ){
                writeBits(srcByte[i], 8 );
                ++i;
        }
    }

    public void writeLong(long data, int bitCount) {

        if(bitCount < 1) {
            return;
        }
        if(bitCount > 64) {
            bitCount = 64;
        }
        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        final byte[] srcByte = longToBytes(data, byteCount);
        int bitsToWrite = (bitCount % 8 == 0) ? 8:bitCount % 8;
        writeBits(srcByte[0], bitsToWrite);

        int i = 1;
        //write all the bytes
        while( i < byteCount ){
            writeBits(srcByte[i], 8 );
            ++i;
        }
    }

    public void writeDouble(double data, int bitCount) {

        if(bitCount < 1) {
            return;
        }
        if(bitCount > 64) {
            bitCount = 64;
        }
        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        final byte[] srcByte = longToBytes(Double.doubleToLongBits(data), byteCount);
        int bitsToWrite = (bitCount % 8 == 0) ? 8:bitCount % 8;
        writeBits(srcByte[0], bitsToWrite);

        int i = 1;
        //write all the bytes
        while( i < byteCount ){
            writeBits(srcByte[i], 8 );
            ++i;
        }
    }

    public void writeFloat(float data, int bitCount) {

        if(bitCount < 1) {
            return;
        }
        if(bitCount > 32) {
            bitCount = 32;
        }
        int byteCount = bitCount / 8;
        if(bitCount % 8 > 0) {
            byteCount++;
        }
        final byte[] srcByte = intToBytes(Float.floatToIntBits(data), byteCount);
        int bitsToWrite = (bitCount % 8 == 0) ? 8:bitCount % 8;
        writeBits(srcByte[0], bitsToWrite);

        int i = 1;
        //write all the bytes
        while( i < byteCount ){
            writeBits(srcByte[i], 8 );
            ++i;
        }
    }
    
    public void writeSigned(int data, int bitCount) {
        
        boolean isNegative = (data<0);
        if(isNegative) {
            data *= -1;
        }
        writeBool(isNegative);
        writeInt(data, bitCount);
    }

    public void writeSignedLong(long data, int bitCount) {

        boolean isNegative = (data<0);
        if(isNegative) {
            data *= -1;
        }
        writeBool(isNegative);
        writeLong(data, bitCount);
    }
    
    public void writeByte(byte data, int bitCount) {
        writeBits(data, bitCount);
    }

    public void writeByte(byte data) {
        writeBits(data, 8);
    }

    public void writeBool(boolean data) {
        writeBits(data? (byte)1:(byte)0, 1);
    }

    public void writeString(String data) throws UnsupportedEncodingException {
        byte[] bytes = data.getBytes("UTF-8");
        writeInt(bytes.length);
        for (byte element : bytes) {
            writeByte(element);
        }
    }
    
    private void writeBits(byte Data, int bitCount) {

        int nextBitHead = _head + bitCount;
        if (nextBitHead > _capacity) {
            reallocateBuffer(Math.max(_capacity * 2, nextBitHead));
        }
        int byteOffset = _head >> 3;
        int bitOffset = _head & 0x7;

        byte currentMask = (byte) ~(0xff >>> bitOffset);
        _buffer[byteOffset] = (byte) ((Byte.toUnsignedInt(_buffer[byteOffset]) & currentMask) | ((Byte.toUnsignedInt(Data) << 8 - bitCount) >>> bitOffset));

        int bitsFreeThisByte = 8 - bitOffset;
        
        if (bitsFreeThisByte < bitCount) {
            _buffer[byteOffset + 1] = (byte) ((Byte.toUnsignedInt(Data) << 8 - bitCount) << bitsFreeThisByte);
        }
        _head = nextBitHead;
    }
    
    private byte[] intToBytes(int num, int byteCount) {
        
        byte[] result = new byte[byteCount];
        for(int i=0 ; byteCount > 0; i++, byteCount-- ) {
            result[i] = (byte) (num >> ((byteCount-1)*8));
        }
        return result;
    }

    private byte[] longToBytes(Long num, int byteCount) {

        byte[] result = new byte[byteCount];

        for(int i=0 ; byteCount > 0; i++, byteCount-- ) {
            result[i] = (byte) (num >> ((byteCount-1)*8));
        }
        return result;
    }

    private void reallocateBuffer(int newCapacity) {

        if (_buffer == null) {
            _buffer = new byte[newCapacity >> 3];
        } else {
            byte[] tempBuffer = new byte[newCapacity >> 3];
            System.arraycopy(_buffer, 0, tempBuffer, 0, _buffer.length);
            _buffer = null;
            _buffer = tempBuffer;
        }
        _capacity = newCapacity;
    }
    
    public byte[] getBuffer() {
        return Arrays.copyOf(_buffer, getByteLength());
    }

    public int getBitLength() {
        return _head;
    }

    public int getByteLength() {
        return (_head + 7) >> 3;
    }
}
