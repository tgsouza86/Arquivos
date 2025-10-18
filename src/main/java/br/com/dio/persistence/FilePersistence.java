package br.com.dio.persistence;

public interface FilePersistence {

    String write(final String data);
    boolean remove(final String sentence);

    String replace(final String oldContent, final String newContant);

    String findAll();

    String findBy(final String sentence);
}
