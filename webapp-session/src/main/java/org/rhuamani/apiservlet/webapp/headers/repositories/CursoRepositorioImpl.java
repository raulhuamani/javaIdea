package org.rhuamani.apiservlet.webapp.headers.repositories;

import org.rhuamani.apiservlet.webapp.headers.models.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoRepositorioImpl implements Repository<Curso> {

    private Connection conn;

    public CursoRepositorioImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Curso> listar() throws SQLException {
        List<Curso> cursos = new ArrayList<>();

        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT * FROM cursos")) {
            while (rs.next()) {
                Curso p = getCurso(rs);
                cursos.add(p);
            }
        }
        return cursos;
    }

    @Override
    public Curso porId(Long id) throws SQLException {
        Curso curso = null;
        try(PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cursos c WHERE c.id = ?")) {
            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    curso = getCurso(rs);
                }
            }
        }
        return curso;
    }

    @Override
    public List<Curso> porNombre(String nombre) throws SQLException {
        List<Curso> cursos = new ArrayList<>();

        try ( PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cursos c WHERE c.nombre like ?")) {
            stmt.setString(1, "%" + nombre + "%");

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cursos.add(getCurso(rs));
                }
            }
        }

        return cursos;
    }

    @Override
    public void guardar(Curso curso) throws SQLException {

    }

    @Override
    public void eliminar(Long id) throws SQLException {

    }

    private Curso getCurso(ResultSet rs) throws SQLException {
        Curso c = new Curso();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setInstructor(rs.getString("instructor"));
        c.setDuracion(rs.getDouble("duracion"));
        return c;
    }
}