package org.rhuamani.optional.ejemplo;

import org.rhuamani.optional.ejemplo.models.Computador;
import org.rhuamani.optional.ejemplo.repositorio.ComputadorRepositorio;
import org.rhuamani.optional.ejemplo.repositorio.Repositorio;

import java.util.Optional;

public class EjemploRepositorioMetodosOrElseThrow {
    public static void main(String[] args) {

        Repositorio<Computador> repositorio = new ComputadorRepositorio();

        Computador pc = repositorio.filtrar("rog").orElseThrow(IllegalStateException::new);
        System.out.println("pc = " + pc);

        //obtiene extension
        String archivo = "documento.pdf";
        String extension = Optional.ofNullable(archivo)
                .filter(a -> a.contains("."))
                .map( a -> a.substring(archivo.lastIndexOf(".") + 1))
                .orElseThrow();
        System.out.println(extension);
    }

}
