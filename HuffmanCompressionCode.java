import java.io.*;
import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    int data;
    char c;
    HuffmanNode left;
    HuffmanNode right;

    @Override
    public int compareTo(HuffmanNode o) {
        return data - o.data;
    }
}

class HuffmanCompression {
    private static final int BYTE_SIZE = 8;

    public void compressFile(String inputFilePath, String outputFilePath) {
        try {
            FileInputStream inputFile = new FileInputStream(inputFilePath);
            int[] frequencies = getFrequencies(inputFile);
            inputFile.close();

            HuffmanNode root = buildHuffmanTree(frequencies);

            HashMap<Character, String> huffmanCodes = new HashMap<>();
            generateHuffmanCodes(root, "", huffmanCodes);

            FileInputStream inputFile2 = new FileInputStream(inputFilePath);
            FileOutputStream outputFile = new FileOutputStream(outputFilePath);
            writeHeader(outputFile, frequencies);

            BitOutputStream bitOutput = new BitOutputStream(outputFile);
            encodeFile(inputFile2, bitOutput, huffmanCodes);
            bitOutput.close();

            inputFile2.close();
            outputFile.close();

            System.out.println("File compressed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decompressFile(String inputFilePath, String outputFilePath) {
        try {
            FileInputStream inputFile = new FileInputStream(inputFilePath);
            FileOutputStream outputFile = new FileOutputStream(outputFilePath);
            BitInputStream bitInput = new BitInputStream(inputFile);

            int[] frequencies = readHeader(bitInput);
            HuffmanNode root = buildHuffmanTree(frequencies);

            decodeFile(bitInput, outputFile, root);

            bitInput.close();
            outputFile.close();
            inputFile.close();

            System.out.println("File decompressed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] getFrequencies(FileInputStream inputFile) throws IOException {
        int[] frequencies = new int[256];

        int value;
        while ((value = inputFile.read()) != -1) {
            frequencies[value]++;
        }

        return frequencies;
    }

    private HuffmanNode buildHuffmanTree(int[] frequencies) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

        for (char c = 0; c < 256; c++) {
            if (frequencies[c] > 0) {
                HuffmanNode node = new HuffmanNode();
                node.c = c;
                node.data = frequencies[c];
                node.left = null;
                node.right = null;
                pq.add(node);
            }
        }

        while (pq.size() > 1) {
            HuffmanNode x = pq.poll();
            HuffmanNode y = pq.poll();
            HuffmanNode sum = new HuffmanNode();
            sum.data = x.data + y.data;
            sum.c = '-';
            sum.left = x;
            sum.right = y;
            pq.add(sum);
        }

        return pq.poll();
    }

    private void generateHuffmanCodes(HuffmanNode root, String code, HashMap<Character, String> huffmanCodes) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.c, code);
        }

        generateHuffmanCodes(root.left, code + "0", huffmanCodes);
        generateHuffmanCodes(root.right, code + "1", huffmanCodes);
    }

    private void writeHeader(FileOutputStream outputFile, int[] frequencies) throws IOException {
        for (int i = 0; i < frequencies.length; i++) {
            outputFile.write(frequencies[i]);
        }
    }

    private int[] readHeader(BitInputStream bitInput) throws IOException {
        int[] frequencies = new int[256];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = bitInput.readBit();
        }
        return frequencies;
    }

    private void encodeFile(FileInputStream inputFile, BitOutputStream bitOutput, HashMap<Character, String> huffmanCodes)
            throws IOException {
        int value;
        while ((value = inputFile.read()) != -1) {
            String code = huffmanCodes.get((char) value);
            for (char c : code.toCharArray()) {
                bitOutput.writeBit(c == '1');
            }
        }
    }

    private void decodeFile(BitInputStream bitInput, FileOutputStream outputFile, HuffmanNode root) throws IOException {
        HuffmanNode current = root;
        int bit;
        while ((bit = bitInput.readBit()) != -1) {
            if (bit == 0) {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                outputFile.write(current.c);
                current = root;
            }
        }
    }
}

class BitInputStream {
    private FileInputStream input;
    private int currentByte;
    private int currentBit;
    private static final int BYTE_SIZE = 8;

    public BitInputStream(FileInputStream input) {
        this.input = input;
        currentByte = 0;
        currentBit = BYTE_SIZE;
    }

    public int readBit() throws IOException {
        if (currentBit == BYTE_SIZE) {
            currentByte = input.read();
            if (currentByte == -1) {
                return -1;
            }
            currentBit = 0;
        }

        int bit = (currentByte >> (BYTE_SIZE - 1 - currentBit)) & 1;
        currentBit++;
        return bit;
    }

    public void close() throws IOException {
        input.close();
    }
}

class BitOutputStream {
    private FileOutputStream output;
    private int currentByte;
    private int currentBit;
    private static final int BYTE_SIZE = 8;

    public BitOutputStream(FileOutputStream output) {
        this.output = output;
        currentByte = 0;
        currentBit = 0;
    }

    public void writeBit(boolean bit) throws IOException {
        currentByte = (currentByte << 1) | (bit ? 1 : 0);
        currentBit++;

        if (currentBit == BYTE_SIZE) {
            output.write(currentByte);
            currentByte = 0;
            currentBit = 0;
        }
    }

    public void close() throws IOException {
        if (currentBit > 0) {
            currentByte <<= (BYTE_SIZE - currentBit);
            output.write(currentByte);
        }
        output.close();
    }
}
public class HuffmanCompressionCode {
	public static void main(String[] args) {
        String inputFile = "input.txt";
        String compressedFile = "compressed.bin";
        String decompressedFile = "decompressed.txt";

        HuffmanCompression compressor = new HuffmanCompression();
        compressor.compressFile(inputFile, compressedFile);
        compressor.decompressFile(compressedFile, decompressedFile);
    }

}
