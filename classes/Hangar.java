package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import db.DAO;

public class Hangar {

    private int id;
    private String local;

    public Hangar(String local, Connection conn) throws Exception {
        this(0, local);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO hangar (local) VALUES (?)");
        stmt.setString(1, getLocal());
        stmt.execute();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            this.setId(rs.getInt(1));
        }
    }

    public Hangar(int id, String local) throws Exception {
        this.id = id;
        this.local = local;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public boolean isOcupado() throws Exception {
        PreparedStatement stmt = DAO.createConnection()
                .prepareStatement("SELECT * FROM hangar WHERE aviao_id is null and id = ?");
        stmt.setInt(1, getId());
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            return false;
        } else {
            return true;
        }
    }

    public static void ListarHangares(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hangar");
        stmt.execute();

        System.out.println("====== HANGARES ======");
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            Hangar hangar = new Hangar(rs.getInt("id"), rs.getString("local"));
            System.out.println(hangar);
        }
    }

    public static void AlterarHangar(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== ALTERAR HANGAR ======");
        System.out.println("Digite o ID do hangar que deseja alterar:");
        int id = sc.nextInt();
        System.out.println("Qual informação deseja alterar?");
        System.out.println(
                "1 - Local" +
                        "\n2 - Adicionar aeronave" +
                        "\n3 - Desocupar hangar");
        int opcao = sc.nextInt();
        switch (opcao) {
            case 1:
                System.out.println("Digite o novo local:");
                String local = sc.next();
                Hangar hangar = getHangarById(id, conn);
                hangar.setLocal(local);
                hangar.updateHangar(conn);
                break;

            case 2:
                if (Hangar.getHangarById(id, conn).isOcupado()) {
                    throw new Exception(
                            "Hangar já está ocupado! Remova a aeronave do Hangar antes de adicionar outra!");
                }
                System.out.println("Digite o ID da aeronave que deseja adicionar:");
                int idAviaoAdd = sc.nextInt();
                Aviao aviaoAdd = Aviao.getAviaoById(idAviaoAdd, conn);
                if (aviaoAdd == null) {
                    throw new Exception("Aeronave não encontrada!");
                }
                adicionarAviaoAoHangar(id, aviaoAdd, conn);
                System.out.println("Aeronave adicionada com sucesso!");
                break;

            case 3:
                removerAviaoDoHangar(id, conn);
                System.out.println("Aeronave removida com sucesso!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    public static void CadastrarHangar(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== CADASTRAR HANGAR ======");
        System.out.println("Digite o local do hangar:");
        String local = sc.next();
        new Hangar(local, conn);
        System.out.println("Hangar cadastrado com sucesso!");
    }

    public static void DeletarHangar(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== DELETAR HANGAR ======");

        System.out.println("Digite o ID do hangar que deseja deletar:");
        int id = sc.nextInt();
        Hangar hangar = getHangarById(id, conn);
        if (hangar == null) {
            throw new Exception("Hangar não encontrado!");
        }
        excluirHangar(id, conn);
        System.out.println("Hangar deletado com sucesso!");
    }

    public static void adicionarAviaoAoHangar(int idHangar, Aviao aviao, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("UPDATE hangar SET aviao_id = ? WHERE id = ?");
        stmt.setInt(1, aviao.getId());
        stmt.setInt(2, idHangar);
        stmt.execute();
    }

    public static void removerAviaoDoHangar(int idHangar, Connection conn) throws Exception {
        PreparedStatement stmt = conn
                .prepareStatement("UPDATE hangar SET aviao_id = NULL WHERE id = ?");
        stmt.setInt(1, idHangar);
        stmt.execute();
    }

    private void updateHangar(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("UPDATE hangar SET local = ? WHERE id = ?");
        stmt.setString(1, this.local);
        stmt.setInt(2, this.id);
        stmt.execute();
    }

    public static void excluirHangar(int id, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM hangar WHERE id = ?");
            stmt.setInt(1, id);
            stmt.execute();
            System.out.println("Hangar excluído com sucesso!");

        } catch (Exception e) {
            throw new Exception("Hangar não encontrado!");

        }

    }

    public static Hangar getHangarById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hangar WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Hangar hangar = new Hangar(
                    rs.getInt("id"),
                    rs.getString("local"));
            return hangar;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        boolean status = false;
        try {
            status = isOcupado();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "\n | ID: " + getId() +
                "\n | Local: " + getLocal() +
                "\n | Status: " + (status == true ? "Ocupado" : "Livre");
    }
}
