package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import utils.DefineTipoUpdate;

public class Helicoptero extends Aeromodelo {
    private int capacidade;
    private String cor;

    public Helicoptero(int id, String marca, String modelo, int capacidade, String cor, Connection conn) throws Exception {
        super(id, marca, modelo);
        this.capacidade = capacidade;
        this.cor = cor;

        if (id == 0) {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO helicoptero (modelo, marca, capacidade, cor) VALUES (?, ?, ?, ?)");
            stmt.setString(1, getModelo());
            stmt.setString(2, getMarca());
            stmt.setInt(3, getCapacidade());
            stmt.setString(4, getCor());
            stmt.execute();
        }
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public static void imprimirHelicopteros(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM helicoptero");
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            Helicoptero helicoptero = new Helicoptero(rs.getInt("id"), rs.getString("marca"), rs.getString("modelo"),
                    rs.getInt("capacidade"), rs.getString("cor"), conn);
            System.out.println(helicoptero);
        }
    }

    public static Helicoptero getHelicopteroById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM helicoptero WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Helicoptero helicoptero = new Helicoptero(
                    rs.getInt("id"),
                    rs.getString("modelo"),
                    rs.getString("marca"),
                    rs.getInt("capacidade"),
                    rs.getString("cor"),
                    conn);
            return helicoptero;
        } else {
            return null;
        }
    }

    public static void alterarHelicoptero(int id, String input, int tipoDado, Connection conn) throws Exception {

        String campo = DefineTipoUpdate.defineCampoUpdate(tipoDado, getHelicopteroById(id, conn));
try {
    PreparedStatement stmt = conn
                .prepareStatement("UPDATE helicoptero SET " + campo + " = ? WHERE id = ?");
        stmt.setString(1, input);
        stmt.setInt(2, id);
        stmt.execute();
        System.out.println("Helicoptero alterado com sucesso!");
    
} catch (Exception e) {
    throw new Exception("Alteração não realizada!");
        
    }
}

    public static void deletarHelicoptero(int id, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM helicoptero WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
        System.out.println("Helicoptero excluído com sucesso!");
        } catch (Exception e) {
            throw new Exception("Alteração não realizada!");
        }
        
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n | Cor: " + getCor() +
                "\n | Capacidade: " + getCapacidade();
    }
}
