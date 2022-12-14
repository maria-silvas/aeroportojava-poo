package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import utils.Mascara;

public class Pista {
    private int id;
    private String numero;

    public Pista(int id, String numero, Connection conn) throws Exception {
        this.id = id;
        this.numero = numero;

        if (id == 0) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO pista (numero) VALUES (?)");
            stmt.setString(1, getNumero());
            stmt.execute();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public static void CadastrarPista(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== CADASTRAR PISTA ======");
        boolean isValido = false;
        String numero = "";
        do {
            System.out.print("Numero: ");
            numero = sc.nextLine();
            isValido = Mascara.isValida(numero, "[A-Z]{1}[0-9]{3}");
            if (isValido == false) {
                System.out.println("Numero invalido! Tente novamente com o padrão A000");
            }
        } while (isValido == false);

        new Pista(0, numero, conn);
        System.out.println("Pista cadastrada com sucesso!");
    }

    public static void ListarPistas(Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pista");
        stmt.execute();

        System.out.println("====== PISTAS ======");
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            Pista pista = new Pista(rs.getInt("id"), rs.getString("numero"), conn);
            System.out.println(pista);
        }
    }

    public static void AlterarPista(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== ALTERAR PISTA ======");
        System.out.print("Informe o ID da pista que deseja alterar: ");
        int id = sc.nextInt();
        Pista pista = getPistaById(id, conn);
        if (pista == null) {
            throw new Exception("Pista nao encontrada!");
        } else {
            System.out.println("Informe o novo numero da pista: ");
            String numero = sc.nextLine();
            updatePista(id, numero, conn);
            System.out.println("Pista alterada com sucesso!");
        }
    }

    public static void DeletarPista(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== DELETAR PISTA ======");
        System.out.print("Informe o ID da pista que deseja deletar: ");
        int id = sc.nextInt();
        Pista pista = getPistaById(id, conn);
        if (pista == null) {
            throw new Exception("Pista nao encontrada!");
        } else {
            deletePista(id, conn);
            System.out.println("Pista deletada com sucesso!");
        }
    }

    public static void updatePista(int id, String input, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("UPDATE pista SET numero = ? WHERE id = ?");
        stmt.setString(1, input);
        stmt.setInt(2, id);
        stmt.execute();
    }

    public static void deletePista(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM pista WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static Pista getPistaById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pista WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Pista pista = new Pista(
                    rs.getInt("id"),
                    rs.getString("numero"),
                    conn);
            
            return pista;
        } else {
            return null;
        }

    }

    @Override
    public String toString() {
        return "\n | ID: " + getId() +
                "\n | Numero: " + getNumero();
    }
}
