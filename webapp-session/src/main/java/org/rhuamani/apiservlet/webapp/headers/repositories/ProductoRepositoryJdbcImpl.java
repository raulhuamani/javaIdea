package org.rhuamani.apiservlet.webapp.headers.repositories;

import org.rhuamani.apiservlet.webapp.headers.models.Categoria;
import org.rhuamani.apiservlet.webapp.headers.models.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImpl implements Repository<Producto> {
    private Connection conn;

    public ProductoRepositoryJdbcImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.*, c.nombre as categoria FROM productos p " +
                    "INNER JOIN categorias c ON (p.categoria_id = c.id) ORDER BY p.id ASC")) {
            while (rs.next()) {
                Producto p = getProducto(rs);
                productos.add(p);
            }
        }
        return productos;
    }

    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;
        try(PreparedStatement stmt = conn.prepareStatement("SELECT p.*, c.nombre as categoria FROM productos p " +
                "INNER JOIN categorias c ON (p.categoria_id = c.id) WHERE p.id = ?")) {
            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    producto = getProducto(rs);
                }
            }
        }
        return producto;
    }

    @Override
    public void guardar(Producto producto) throws SQLException {

        String sql;
        if (producto.getId() != null && producto.getId() >0) {
            sql = "update productos set nombre=?, precio=?, sku=?, categoria_id=?, fecha_registro=? where id=?";
        } else {
            sql = "Insert into productos (nombre, precio, sku, categoria_id, fecha_registro) values (?, ?, ?, ?, ?)";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getPrecio());
            stmt.setString(3, producto.getSku());
            stmt.setLong(4, producto.getCategoria().getId());

            if(producto.getId() != null && producto.getId() > 0) {
                stmt.setDate(5,Date.valueOf(producto.getFechaRegistro()) );
                stmt.setLong(6, producto.getId());
            } else {
                stmt.setDate(5,Date.valueOf(producto.getFechaRegistro()) );
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "Delete from productos where id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Producto getProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getInt("precio"));
        p.setSku(rs.getString("sku"));
        p.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        Categoria c = new Categoria();
        c.setId(rs.getLong("categoria_id"));
        c.setNombre(rs.getString("categoria"));
        p.setCategoria(c);

        return p;
    }

    @Override
    public List<Producto> porNombre(String nombre) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try ( PreparedStatement stmt = conn.prepareStatement("SELECT p.*, c.nombre as categoria FROM productos p " +
                " INNER JOIN categorias c ON (p.categoria_id = c.id) WHERE p.nombre like ? ")) {
            stmt.setString(1, "%" + nombre + "%");

            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(getProducto(rs));
                }
            }
        }
        return productos;
    }
}