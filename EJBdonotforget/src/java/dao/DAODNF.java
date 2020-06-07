package dao;

import dbconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ModelDNF;

/**
 *
 * @author Matheus M. Burin
 */
public class DAODNF {

    Connection con;

    public DAODNF() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public void adiconar(ModelDNF dnf) throws SQLException {
        String sql = " INSERT INTO TODO (STATUS, CONTEUDO, DATA, HORA) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dnf.getStatus());
            ps.setString(2, dnf.getConteudo());
            ps.setDate(3, dnf.getData());
            ps.setTime(4, dnf.getHora());
            ps.execute();
        }
    }

    public void editar(ModelDNF dnf) throws SQLException {
        String sql = " UPDATE TODO "
                + "SET STATUS = ? "
                + "WHERE ID = ? ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dnf.getStatus());
            ps.setInt(2, dnf.getId());
            ps.execute();
        }
    }

    public void deletar(int dnf) throws SQLException {
        String sql = "DELETE FROM TODO "
                + "WHERE ID = ? ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dnf);
            ps.execute();
        }
    }

    public List<ModelDNF> buscarTodos() throws SQLException {
        List<ModelDNF> todos = new ArrayList<>();

        String sql = "SELECT TODO.ID, TODO.status, TODO.CONTEUDO, TODO.HORA, TODO.DATA "
                + "FROM TODO "
                + "ORDER BY TODO.DATA, TODO.HORA ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ModelDNF todo = new ModelDNF();

            todo.setId(rs.getInt("ID"));
            todo.setStatus(rs.getInt("STATUS"));
            todo.setConteudo(rs.getString("CONTEUDO"));
            todo.setData(rs.getDate("DATA"));
            todo.setHora(rs.getTime("HORA"));

            todos.add(todo);
        }
        rs.close();
        ps.close();

        return todos;
    }
}
