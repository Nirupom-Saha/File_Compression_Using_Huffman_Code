# File_Compression_Using_Huffman_Code
This repository contains a Java implementation of Huffman Compression, a widely used algorithm for lossless data compression. The code includes classes for building a Huffman Tree, generating Huffman Codes, and encoding and decoding files using these codes.

#HuffmanCompression
This class contains methods for compressing and decompressing files:

compressFile(String inputFilePath, String outputFilePath): Compresses the input file and writes the compressed data to the output file.
decompressFile(String inputFilePath, String outputFilePath): Decompresses the input file and writes the decompressed data to the output file.

#BitInputStream
This class provides methods to read bits from a file, which is essential for reading the compressed binary data.

#BitOutputStream
This class provides methods to write bits to a file, which is essential for writing the compressed binary data.

#HuffmanCompressionExample
This is the main class that demonstrates how to use the HuffmanCompression class to compress and decompress files. Modify the inputFile, compressedFile, and decompressedFile variables to point to your desired files.
