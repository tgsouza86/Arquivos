package br.com.dio.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class IOFilePersistence implements FilePersistence{

    private final String currentDir = System.getProperty("user.dir");

    private final String storedDir = "/managedFiles/IO/";

    private final String fileName;

    public IOFilePersistence(String fileName) throws IOException {
        this.fileName = fileName;
        var file = new File(currentDir + storedDir);
        if (!file.exists() && !file.mkdirs()) throw new IOException("Erro ao criar arquivo");
        clearFile();
    }

    @Override
    public String write(String data) {
        try(
                var fileWriter = new FileWriter(currentDir + storedDir + fileName, true);
                var bufferredWrite = new BufferedWriter(fileWriter);
                var printWriter = new PrintWriter(bufferredWrite)
        ){
            printWriter.println(data);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return data;
    }

    @Override
    public boolean remove(String sentence) {
        var content = findAll();
        var contentList = new ArrayList<>(Stream.of(content.split(System.lineSeparator())).toList());

        if (contentList.stream().noneMatch(c -> c.contains(sentence))) return false;
        clearFile();
        contentList.stream()
                .filter(c -> !c.contains(sentence))
                .forEach(this::write);


        return true;
    }

    @Override
    public String replace(String oldContent, String newContant) {
        var content = findAll();
        var contentList = new ArrayList<>(Stream.of(content.split(System.lineSeparator())).toList());
        if (contentList.stream().noneMatch(c -> c.contains(oldContent))) return "";

        clearFile();
        contentList.stream()
                .map(c -> !c.contains(oldContent) ? newContant : c)
                .forEach(this::write);
        return newContant;
    }

    @Override
    public String findAll() {
        var content = new StringBuilder();
        try(var reader = new BufferedReader (new FileReader(currentDir + storedDir + fileName))){
            String line;
            do{
                line = reader.readLine();
                if ((line != null)) content.append(line).append(System.lineSeparator());
            }while (line != null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return content.toString();
    }

    @Override
    public String findBy(String sentence) {
        var found = "";
        try (var reader = new BufferedReader(new FileReader(currentDir + storedDir + fileName))){
            String line = reader.readLine();
            while (line != null){
                if ((line.contains(sentence))){
                    found = line;
                    break;
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return found;
    }

    private void clearFile() {

        try(OutputStream outputStream = new FileOutputStream(currentDir + storedDir + fileName)){
            System.out.printf("inicializando recursos (%s) \n", currentDir + storedDir + fileName);
              }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void createFile(){

    }
}
