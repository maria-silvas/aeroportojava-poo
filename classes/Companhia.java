package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import utils.DefineTipoUpdate;

public class Companhia {
    private int id;
    private String nome;
    private String cnpj;

    public Companhia(int id, String nome, String cnpj, Connection conn) throws Exception {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;

        if (id == 0) {
            PreparedStatement stmt = conn
                    .prepareStatement("INSERT INTO companhia (nome, cnpj) VALUES (?, ?)");
            stmt.setString(1, getNome());
            stmt.setString(2, getCnpj());
            stmt.execute();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public static void ListarCompanhias(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM companhia");
        stmt.execute();

        System.out.println("====== COMPANHIAS ======");
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            Companhia companhia = new Companhia(rs.getInt("id"), rs.getString("nome"), rs.getString("cnpj"), conn);
            System.out.println(companhia);
        }
    }

    public static void CadastrarCompanhia(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== CADASTRAR COMPANHIA ======");
        System.out.print("Nome: ");
        String nome = sc.next();
        String cnpj = "";
        do {
            System.out.print("CNPJ: ");
            cnpj = sc.next();
            if (!cnpj.matches("[0-9]{14}")) {
                System.out.println("CNPJ inválido. Tente novamente.");
            }
        } while (!cnpj.matches("[0-9]{14}"));

        Companhia companhia = new Companhia(0, nome, cnpj, conn);
        System.out.println("Companhia " + companhia.getNome() + " cadastrada com sucesso!");
    }

    public static void AlterarCompanhia(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== ALTERAR COMPANHIA ======");
        System.out.println("Digite o ID da companhia que deseja alterar: ");
        int id = sc.nextInt();
        Companhia companhia = getCompanhiaById(id, conn);
        if (companhia == null) {
            throw new Exception("Companhia não encontrada!");
        }

        System.out.println("Qual informação deseja alterar?");
        System.out.println("1 - Nome" +
                "\n2 - CNPJ");
        int opcaoCompanhia = sc.nextInt();
        System.out.println("Digite o novo valor: ");
        String novoValor = sc.next();

        updateCompanhia(id, novoValor, opcaoCompanhia, conn);
    }

    public static void DeletarCompanhia(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== DELETAR COMPANHIA ======");
        System.out.println("Digite o ID da companhia que deseja deletar: ");
        int id = sc.nextInt();
        Companhia companhia = getCompanhiaById(id, conn);
        if (companhia == null) {
            throw new Exception("Companhia não encontrada!");
        } else {
            deleteCompanhia(id, conn);
            System.out.println("Companhia deletada com sucesso!");
        }
    }

    public static Companhia getCompanhiaById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM companhia WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Companhia companhia = new Companhia(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cnpj"),
                    conn);
            return companhia;
        } else {
            return null;
        }
    }

    public static void updateCompanhia(int id, String input, int tipoDado, Connection conn) throws Exception {

        String campo = DefineTipoUpdate.defineCampoUpdate(tipoDado, getCompanhiaById(id, conn));
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("UPDATE companhia SET " + campo + " = ? WHERE id = ?");
            stmt.setString(1, input);
            stmt.setInt(2, id);
            stmt.execute();
            System.out.println("Companhia alterada com sucesso!");

        } catch (Exception e) {
            throw new Exception("Alteração não realizada!");
        }
    }

    public static void deleteCompanhia(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM companhia WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

    }

    @Override
    public String toString() {
        return "\n | ID: " + getId() +
                "\n | Nome: " + getNome() +
                "\n | CNPJ: " + getCnpj();
    }
}
