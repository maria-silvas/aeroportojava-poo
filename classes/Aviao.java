package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import utils.DefineTipoUpdate;

public class Aviao extends Aeromodelo {
    private String prefixo;
    private int capacidade;
    private Companhia companhia;

    public Aviao(int id, String modelo, String marca, String prefixo, int capacidade, Companhia companhia, Connection conn)
            throws Exception {
        super(id, marca, modelo);
        this.prefixo = prefixo;
        this.capacidade = capacidade;
        this.companhia = companhia;

        if (id == 0) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO aviao (modelo, marca, prefixo, capacidade, companhia_id) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, getModelo());
            stmt.setString(2, getMarca());
            stmt.setString(3, getPrefixo());
            stmt.setInt(4, getCapacidade());
            stmt.setInt(5, getCompanhia().getId());
            stmt.execute();
        }
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Companhia getCompanhia() {
        return companhia;
    }

    public void setCompanhia(Companhia companhia) {
        this.companhia = companhia;
    }

    public static void imprimirAvioes(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM aviao");
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            Aviao aviao = new Aviao(rs.getInt("id"), rs.getString("modelo"), rs.getString("marca"),
                    rs.getString("prefixo"), rs.getInt("capacidade"),
                    Companhia.getCompanhiaById(rs.getInt("companhia_id"), conn),conn);
            System.out.println(aviao);
        }
    }

    public static Aviao getAviaoById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM aviao WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Aviao aviao = new Aviao(
                    rs.getInt("id"),
                    rs.getString("modelo"),
                    rs.getString("marca"),
                    rs.getString("prefixo"),
                    rs.getInt("capacidade"),
                    Companhia.getCompanhiaById(rs.getInt("companhia_id"), conn), conn);
            return aviao;
        } else {
            return null;
        }
    }

    public static void alterarAviao(int id, String input, int tipoDado, Connection conn) throws Exception {

        String campo = DefineTipoUpdate.defineCampoUpdate(tipoDado, getAviaoById(id, conn));

        PreparedStatement stmt = conn
                .prepareStatement("UPDATE aviao SET " + campo + " = ? WHERE id = ?");
        stmt.setString(1, input);
        stmt.setInt(2, id);
        stmt.execute();
    }

    public static void deletarAviao(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM aviao WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n | Prefixo: " + getPrefixo() +
                "\n | Capacidade: " + getCapacidade() +
                "\n | Companhia: " + getCompanhia().getNome();
    }

}
