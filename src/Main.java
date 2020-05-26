
public class Main {
    public static void main(String[] args) {

        String basePath = "C:\\Users\\owner\\Desktop\\HuffmanCompression-master\\src";
        String filePath = basePath+"\\opp.txt";
        String filePath1 = basePath+"\\OnTheOrigin.txt";
        String filepathPhoto = basePath+"\\Smiley.bmp";
        String filepathPhoto1 = basePath+"\\Red_Flowers.bmp";
        String outputFile = basePath+"\\MyCompressedFile";
        String outputKey = basePath+"\\MyKey";
        String decompressedFile = basePath+"\\decompressed";

        String[] paths = {basePath,filePath, filePath1,filepathPhoto,filepathPhoto1};
        String[] output = {outputFile,outputKey,decompressedFile};

        HuffmanEncoderDecoder hf = new HuffmanEncoderDecoder();
        hf.Compress(paths,output);
        hf.decompress(paths,output);


    }
}
