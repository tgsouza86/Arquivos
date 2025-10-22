package br.com.dio.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NIO2FilePersistence  implements FilePersistence{

    private final String currentDir = System.getProperty("user.dir");

    private final String storedDir = "/managedFiles/NIO2/";

    private final String fileName;

    public NIO2FilePersistence(String fileName) throws IOException {
        this.fileName = fileName;
        var path = Paths.get(currentDir + storedDir);
        if (!Files.exists(path)) {
            Files.createDirectory(path);

        } clearFile();


    }
    private void clearFile() {

        try(OutputStream outputStream = new FileOutputStream(currentDir + storedDir + fileName)){
            System.out.printf("inicializando recursos (%s) \n", currentDir + storedDir + fileName);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public String write(String data) {
        var path = Paths.get(currentDir + storedDir + fileName);
        try {
            Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
            Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return data;
    }

    @Override
    public boolean remove(String sentence) {
        var contentList = toListString();
        if (contentList.stream().noneMatch(c ->c.contains(sentence))) return false;
        clearFile();
        contentList.stream()
                .filter(c -> !c.contains(sentence))
                .forEach(this::write);
        return true;
    }

    @Override
    public String replace(String oldContent, String newContant) {
        var contentList = toListString();
        if (contentList.stream().noneMatch(c ->c.contains(oldContent))) return "";
        clearFile();
        contentList.stream()
                .map(c -> c.contains(oldContent) ? newContant : c)
                .forEach(this::write);
        return newContant;

    }

    @Override
    public String findAll() {
        var path = Paths.get(currentDir + storedDir + fileName);
        var content = "";
        try(var lines = Files.lines(path)){
            content = lines.collect(Collectors.joining(System.lineSeparator()));
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return content;
    }

    @Override
    public String findBy(String sentence) {
        var content = findAll();
        return Stream.of(content.split(System.lineSeparator()))
                .filter(c -> c.contains(sentence))
                .findFirst()
                .orElse("");
    }

    private List<String> toListString() {
        var content = findAll();
        return new ArrayList<>(Stream.of(content.split(System.lineSeparator())).toList());
    }
}
