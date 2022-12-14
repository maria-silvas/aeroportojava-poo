package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import utils.DefineTipoUpdate;

public class Jato extends Aeromodelo {
    private String cor;
    private int velocidade;

    public Jato(int id, String marca, String modelo, String cor, int velocidade, Connection conn) throws Exception {
        super(id, marca, modelo);
        this.cor = cor;
        this.velocidade = velocidade;

        if (id == 0) {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO jato (modelo, marca, cor, velocidade) VALUES (?, ?, ?, ?)");
            stmt.setString(1, getModelo());
            stmt.setString(2, getMarca());
            stmt.setString(3, getCor());
            stmt.setInt(4, getVelocidade());
            stmt.execute();
        }
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public static void imprimirJatos(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jato");
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            Jato jato = new Jato(rs.getInt("id"), rs.getString("marca"), rs.getString("modelo"), rs.getString("cor"),
                    rs.getInt("velocidade"), conn);
            System.out.println(jato);
        }
    }

    public static Jato getJatoById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jato WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Jato jato = new Jato(
                    rs.getInt("id"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("cor"),
                    rs.getInt("velocidade"),
                    conn);
            return jato;
        } else {
            return null;
        }
    }

    public static void alterarJato(int id, String input, int tipoDado, Connection conn) throws Exception {

        String campo = DefineTipoUpdate.defineCampoUpdate(tipoDado, getJatoById(id, conn));

        PreparedStatement stmt = conn
                .prepareStatement("UPDATE jato SET " + campo + " = ? WHERE id = ?");
        stmt.setString(1, input);
        stmt.setInt(2, id);
        stmt.execute();
    }

    public static void deletarJato(int id, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM jato WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();
            System.out.println("Jato excluído com sucesso!");
            
        } catch (Exception e) {
            throw new Exception("Alteração não realizada!");
        }
       
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n | Cor: " + getCor() +
                "\n | Velocidade: " + getVelocidade();
    }
}
