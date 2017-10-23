## Bitwise Memory streams
> *This library uses some ideas from the [Multiplayer Game Programming](https://www.pearson.com/us/higher-education/program/Glazer-Multiplayer-Game-Programming-Architecting-Networked-Games/PGM317032.html) book.*

Java Bitwise Memory streams offer output and input streams for writing and reading different types of data in bit-level.
you don't need to save or send int variables with max value of 127 with traditionally 32 bits, you only need 7 bits!

Its very useful for those kind of applications which need to send and receive data on network very efficiently (for example: realtime multiplayer games).

For saving data with fewer bits like above, you can use Bitwise memory stream classes in this package.

### BitwiseMemoryOutputStream
BitwiseMemoryOutputStream class is responsible for writing data with bit precision to stream and has below methods:
* **void writeInt(int data)** writes an int value to buffer by 32 bits
* **void writeInt(int data, int bitCount)** writes an int value to buffer by specified bits count
* **void writeLong(long data)** writes a long value to buffer by 64 bits
* **void writeLong(long data, int bitCount)** writes a long value to buffer by specified bits count
* **void writeDouble(double data)** writes a double value to buffer by 64 bits
* **void writeFloat(float data)** writes a float value to buffer by 32 bits
* **void writeSigned(int data)** writes a signed int value to buffer by 32 bits
* **void writeSigned(int data, int bitCount)** writes a signed int value to buffer by specified bits count
* **void writeSignedLong(long data)** writes a signed long value to buffer by 64 bits
* **void writeSignedLong(long data, int bitCount)** writes a signed long value to buffer by specified bits count
* **void writeByte(byte data)** writes a byte value to buffer by 8 bits
* **void writeByte(byte data, int bitCount)** writes a byte value to buffer by specified bits count
* **void writeBool(bool data)** writes a bool value to buffer by 1 bit
* **void writeString(string data)** writes a string value to buffer based on string's length
* **byte[] getBuffer()** returns buffer as a byte array
* **int getBitLength()** returns buffer length in bits
* **int getByteLength()** returns buffer length in bytes

### BitwiseMemoryInputStream
BitwiseMemoryInputStream class is responsible for reading data with bit precision from stream and has below methods:
* **int readInt()** reads an int value from buffer by 32 bits
* **int readInt(int bitCount)** reads an int value from buffer by specified bits count
* **long readLong()** reads a long value from buffer by 64 bits
* **long readLong(int bitCount)** reads a long value from buffer by specified bits count
* **double readDouble()** reads a double value from buffer by 64 bits
* **float readFloat()** reads a float value from buffer by 32 bits
* **int readSignedInt()** reads a signed int value from buffer by 32 bits
* **int readSignedInt(int bitCount)** reads a signed int value from buffer by specified bits count
* **long readSignedLong()** reads a signed long value from buffer by 64 bits
* **long readSignedLong(int bitCount)** reads a signed long value from buffer by specified bits count
* **byte readByte()** reads a byte value from buffer by 8 bits
* **bool readBool()** reads a bool value from buffer by 1 bit
* **string readString()** reads a string value from buffer based on string's length
* **byte[] getBuffer()** returns buffer as a byte array
* **int getRemainingBytes()** returns count of remaining buffer bytes to read

#### Notices
* You must read data from stream in same order you wrote.
* As float/double representation in binary is different than int/long, i didn't provide methods for writing/reading them with fewer bits.
* As writeSigned methods first write number's sign as a bool and then write data's absolute value as an int/long and writeString method first writes string length as an int and then writes string's bytes, for reading this types you need to use same Stream classes or write a same implementation in other languages.

> *I have wrote a Unity/c# implementation of this bitwise memory stream classes in a different repository which you can use in your Unity/c# project: [Unity-UdpSocket-BitStream-Utilities](https://github.com/M-Aghasi/Unity-UdpSocket-BitStream-Utilities).*

## How to use
The easiest way to use this library is to add the jar file as a dependency to your project, you can find the last build in [Releases](https://github.com/M-Aghasi/Java-memory-bit-stream/releases) section.
it needs Java 8 and has no more dependencies.
