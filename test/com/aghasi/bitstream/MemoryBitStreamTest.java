package com.aghasi.bitstream;

import junit.framework.TestCase;
import java.io.UnsupportedEncodingException;


public class MemoryBitStreamTest extends TestCase {

    private OutputMemoryBitStream _outStream;
    private InputMemoryBitStream _inStream;

    @Override
    protected void setUp() throws Exception {
                
        _outStream = new OutputMemoryBitStream();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        _outStream = null;
        super.tearDown();
    }

    public void testReadAndWriteInt() {

        for (int i = 0; i < 10000; i++) {
            int bitCount = getRequiredBitsCount(i);
            _outStream.writeInt(i, bitCount);
        }
        int bitCount = getRequiredBitsCount(Integer.MAX_VALUE);
        _outStream.writeInt(Integer.MAX_VALUE, bitCount);

        _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);

        for (int i = 0; i < 10000; i++) {
            assertEquals(i, _inStream.readInt(getRequiredBitsCount(i)));
        }
        assertEquals(Integer.MAX_VALUE, _inStream.readInt(getRequiredBitsCount(Integer.MAX_VALUE)));
    }

    public void testReadAndWriteFloat() {

        for (float i = 0.123f; i < 1000; i += 1.1) {
            int bitCount = getRequiredBitsCount(Float.floatToIntBits(i));
            _outStream.writeFloat(i, bitCount);
        }
        int bitCount = getRequiredBitsCount(Float.floatToIntBits(Float.MAX_VALUE));
        _outStream.writeFloat(Float.MAX_VALUE, bitCount);

        _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);

        for (float i = 0.123f; i < 1000; i += 1.1) {
            assertEquals(i, _inStream.readFloat(getRequiredBitsCount(Float.floatToIntBits(i))));
        }
        assertEquals(Float.MAX_VALUE, _inStream.readFloat(getRequiredBitsCount(Float.floatToIntBits(Float.MAX_VALUE))));
    }

    public void testReadAndWriteLong() {

        for (long i = 0; i < 10000; i++) {
            int bitCount = getLongRequiredBitsCount(i);
            _outStream.writeLong(i, bitCount);
        }
        int bitCount = getLongRequiredBitsCount(Long.MAX_VALUE);
        _outStream.writeLong(Long.MAX_VALUE, bitCount);

        _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);

        for (long i = 0; i < 10000; i++) {
            assertEquals(i, _inStream.readLong(getLongRequiredBitsCount(i)));
        }
        assertEquals(Long.MAX_VALUE, _inStream.readLong(getLongRequiredBitsCount(Long.MAX_VALUE)));
    }

    public void testReadAndWriteDouble() {

        for (double i = 321.123f; i < 1000; i += 1.1) {
            int bitCount = getLongRequiredBitsCount(Double.doubleToLongBits(i));
            _outStream.writeDouble(i, bitCount);
        }
        int bitCount = getLongRequiredBitsCount(Double.doubleToLongBits(Double.MAX_VALUE));
        _outStream.writeDouble(Double.MAX_VALUE, bitCount);

        _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);

        for (double i = 321.123f; i < 1000; i += 1.1) {
            assertEquals(i, _inStream.readDouble(getLongRequiredBitsCount(Double.doubleToLongBits(i))));
        }
        assertEquals(Double.MAX_VALUE, _inStream.readDouble(getLongRequiredBitsCount(Double.doubleToLongBits(Double.MAX_VALUE))));
    }
    
    public void testReadAndWriteSignedInt() {

        for (int i = -5000; i < 5000; i++) {
            int bitCount = getRequiredBitsCount(i);
            _outStream.writeSigned(i, bitCount);
        }
        int bitCount = getRequiredBitsCount(Integer.MAX_VALUE);
        _outStream.writeSigned(Integer.MAX_VALUE, bitCount);

        _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);

        for (int i = -5000; i < 5000; i++) {
            assertEquals(i, _inStream.readSignedInt(getRequiredBitsCount(i)));
        }
        assertEquals(Integer.MAX_VALUE, _inStream.readSignedInt(getRequiredBitsCount(Integer.MAX_VALUE)));
    }


    public void testReadAndWriteString() {

        try {
            _outStream.writeString("test text, hello world!");
            _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);
            assertEquals("test text, hello world!", _inStream.readString());
        } catch (UnsupportedEncodingException e) {
            fail("UnsupportedEncodingException for string");
        }
    }

    public void testCombinationReadAndWrite() {

        try {
            _outStream.writeBool(true);
            _outStream.writeInt(3249);
            _outStream.writeString("test case, sefjsefjosejfoffiof43095=6-0697979-07_)+(**&&");
            _outStream.writeString("مسعود تست");        // test for utf-8 chars
            _outStream.writeString("مسعود تست e-hfu");
            _outStream.writeSigned(-48, 32);
            _outStream.writeByte((byte)0xff);
            _outStream.writeFloat(1.23f, 32);

            _inStream = new InputMemoryBitStream(_outStream.getBuffer(), _outStream.getBuffer().length * 8);

            assertEquals(true, _inStream.readBool());
            assertEquals(3249, _inStream.readInt(32));
            assertEquals("test case, sefjsefjosejfoffiof43095=6-0697979-07_)+(**&&", _inStream.readString());
            assertEquals("مسعود تست", _inStream.readString());
            assertEquals("مسعود تست e-hfu", _inStream.readString());
            assertEquals(-48, _inStream.readSignedInt(32));
            assertEquals((byte)0xff, _inStream.readByte());
            assertEquals(1.23f, _inStream.readFloat(32));
        } catch (UnsupportedEncodingException e) {
            fail("UnsupportedEncodingException for string");
        }
    }

    private int getRequiredBitsCount(int num) {

        if(num < 0) {
            num *= -1;
        }
        int bitCount = 1;
        while (num / 2 > 0) {
            bitCount++;
            num /= 2;
        }
        return bitCount;
    }

    private int getLongRequiredBitsCount(long num) {

        if(num < 0) {
            num *= -1;
        }
        int bitCount = 1;
        while (num / 2 > 0) {
            bitCount++;
            num /= 2;
        }
        return bitCount;
    }
}
