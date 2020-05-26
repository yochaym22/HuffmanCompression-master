

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Assignment 1
 * Submitted by:
 * Student 1. 	ID# 206109621 Eyal Delarea
 * Student 2. 	ID# 203000005 Yohai Mizrahi
 * Student 3.   ID# 311272173 Gabriel Noghryan
 */

/**
 * This class will preform hoffman codding and decoding in 3 steps:
 * 1.Preform analysis of the data of the file and keep amount of each data junk.
 * 2.Create a mini-heap,and insert all of the data into the heap.
 * 3.Build a hoffman-tree from the mini heap.
 * LAST STEP:
 * 4.Output the compressed file and the keymap of the values.
 */
public class HuffmanEncoderDecoder implements Compressor {

    static final int NOT_FOUND = -1;
    static final int basePathArrayIndex = 0;
    static int bitSetIndex = 0;
    static String currentFileType = null;

    public HuffmanEncoderDecoder() {

        // TODO Auto-generated constructor stub
    }


    public void Compress(String[] input_names, String[] output_names) {

        Scanner reader = new Scanner(System.in);
        System.out.println("choose file by array index");
        int inputStringIndex = reader.nextInt();
        currentFileType = getFileSuffix(input_names[inputStringIndex]);
        System.out.println("File suffix : "+currentFileType);

        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(Paths.get(input_names[inputStringIndex]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayListObject> inputVariables = new ArrayList<ArrayListObject>();

        /**
         * First step - mapping the chars of the files.
         * building array list of different values of ascii chars,
         * and keep counting values for each char.
         *
         */
        //read from file and remove char
        Byte temp1 = 0;
        //loop until file ends == NOT_FOUND
        assert fileContent != null;
        for (int i = 0; i < fileContent.length; i++) {
            temp1 = fileContent[i];
            int ans = find(inputVariables, temp1);
            if (ans == NOT_FOUND) {
                inputVariables.add(new ArrayListObject(temp1));

            } else {
                inputVariables.get(ans).incAmount();
            }
        }

        MinHeap heap = new MinHeap(inputVariables.size(),input_names[basePathArrayIndex]);
        //insert all elements
        for (ArrayListObject inputVariable : inputVariables) {
            heap.insert(inputVariable);
        }

        //Generate Huffman map
        HashMap<Byte, mapObject> map = heap.generateHoffmanMap();

        //Crate BitSet object

        BitSet bitset = new BitSet();
        BitSet tempByte ;

        //write Coding to Bitset to compressed file

        for (int i = 0; i < fileContent.length; i++) {

            mapObject tempObject = map.get(fileContent[i]);
            int length = tempObject.getLength();
            tempByte = tempObject.getHoffmanCode();

            for (int j = 0; j < length; j++) {
                bitset.set(bitSetIndex++, tempByte.get(j));
            }

        }


        try {
            File compressedFile = new File(output_names[0]);
            if (compressedFile.createNewFile()) {
                System.out.println("File created: " + compressedFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fileOut =
                    new FileOutputStream(output_names[0]);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(bitset);

            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + output_names[0]);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void decompress(String[] input_names, String[] output_names) {

        BitSet set = null;
        ArrayListObject key = null;

        try {
            FileInputStream fileIn = new FileInputStream(output_names[0]);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            set = (BitSet) in.readObject();
            fileIn = new FileInputStream(output_names[1]);
            in = new ObjectInputStream(fileIn);
            key = (ArrayListObject) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        assert set != null;
        assert key != null;

        //wrtire to char array from bitset

        ArrayList<Byte> deCompressedArray = decompressCharArray(key, set);
        File decompressFile = new File(output_names[2]);


        try {

            // Initialize a pointer
            // in file using OutputStream
            OutputStream os = new FileOutputStream(decompressFile + currentFileType);

            // Starts writing the bytes in it
            for (int i = 0; i < deCompressedArray.size(); i++) {
                os.write(deCompressedArray.get(i));
            }

            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        //clear after,delete key
        try {
            File f = new File(output_names[1]);           //file to be delete
            if (f.delete())                      //returns Boolean value
            {
                System.out.println(f.getName() + " deleted");   //getting and printing the file name
            } else {
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Byte> decompressCharArray(ArrayListObject tree, BitSet compressedBits) {

        ArrayList<Byte> result = new ArrayList<>();
        boolean temp;
        ArrayListObject current = tree;

        for (int i = 0; i < bitSetIndex; i++) {

            while (current.isFather()) {
                temp = compressedBits.get(i); // bool value

                if (temp) {
                    current = current.getRight();
                } else {
                    current = current.getLeft();
                }
                if (current.isFather()) {
                    i++;
                }


            }
            result.add(current.getValue());
            current = tree;

        }  //end for loop i++
        return result;
    }


    /**
     * Check if a char exits in the array list.
     *
     * @param arrayList   given arrayList
     * @param currentChar given char
     * @return index value;
     */
    public static int find(ArrayList<ArrayListObject> arrayList, Byte currentChar) {

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getValue() == currentChar) {
                return i;
            }
        }
        return NOT_FOUND;
    }


    /**
     * read file to char array
     *
     * @param filePath file path
     * @return char array
     * @throws IOException e
     */
    public static char[] ReadFileToCharArray(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return fileData.toString().toCharArray();
    }

    public String getFileSuffix(String path) {
        char temp;
        for (int i = path.length() - 1; i > 0; i--) {
            temp = path.charAt(i);
            if (temp == '.') {
                return path.substring(i, path.length());
            }
        }
        return null;
    }
}




