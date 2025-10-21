package br.com.dio.persistence;

import java.io.*;
import java.nio.ByteBuffer;

public class NIOFilePersistence implements FilePersistence{

    private final String currentDir = System.getProperty("user.dir");

    private final String storedDir = "/managedFiles/NIO/";

    private final String fileName;

    public NIOFilePersistence(String fileName) throws IOException {
        this.fileName = fileName;
        var file = new File(currentDir + storedDir);
        if (!file.exists() && !file.mkdirs()) throw new IOException("Erro ao criar arquivo");
        clearFile();
    }

    @Override
    public String write(String data) {
        try(var file = new RandomAccessFile(new File(currentDir + storedDir + fileName),"rw");

        ){
            file.seek(file.length());
            file.writeBytes(data);
            file.writeBytes(System.lineSeparator());
        }catch (IOException ex) {
            ex.printStackTrace();
        }

        return data;
    }

    @Override
    public boolean remove(String sentence) {
        return false;
    }

    @Override
    public String replace(String oldContent, String newContant) {
        return "";
    }

    @Override
    public String findAll() {
        var content = new StringBuilder();
        try(var file = new RandomAccessFile(new File(currentDir + storedDir + fileName), "r");
            var channel = file.getChannel();
        ){
            var buffer = ByteBuffer.allocate(256);
            var bytesReader = channel.read(buffer);
            while (bytesReader != -1){
                buffer.flip();
                while (buffer.hasRemaining()){
                    content.append((char) buffer.get());
                }
                buffer.clear();
                bytesReader = channel.read(buffer);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return content.toString();
    }

    @Override
    public String findBy(String sentence) {
        return "";
    }
    private void clearFile() {

        try(OutputStream outputStream = new FileOutputStream(currentDir + storedDir + fileName)){
            System.out.printf("inicializando recursos (%s) \n", currentDir + storedDir + fileName);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
