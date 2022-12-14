package classes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import utils.DefineTipoUpdate;
import utils.GetOption;
import utils.Mascara;

public class Voo {
    private int id;
    private String numero;
    private String observacao;
    private String piloto;
    private String copiloto;
    private String origem;
    private String destino;
    private String data;
    private String hora;
    private Aeromodelo aeromodelo;
    private Pista pista;

    public Voo(int id, String numero, String observacao, String piloto, String copiloto, 
    String origem, String destino, String data, String hora, Aeromodelo aeromodelo, Pista pista, Connection conn) throws Exception {
        this.id = id;
        this.numero = numero;
        this.observacao = observacao;
        this.piloto = piloto;
        this.copiloto = copiloto;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.hora = hora;
        this.aeromodelo = aeromodelo;
        this.pista = pista;

        String campo = getAeromodelo(aeromodelo);

        if (id == 0) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO voo (numero, observacao, piloto, copiloto, origem, destino, data, hora, pista_id, "+campo+") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, getNumero());
            stmt.setString(2, getObservacao());
            stmt.setString(3, getPiloto());
            stmt.setString(4, getCopiloto());
            stmt.setString(5, getOrigem());
            stmt.setString(6, getDestino());
            stmt.setString(7, getData());
            stmt.setString(8, getHora());
            stmt.setInt(9, getPista().getId());
            stmt.setInt(10, aeromodelo.getId());
            stmt.execute();
        }
    }

    public static <T> String getAeromodelo(T aeromodelo) throws Exception {
        if (aeromodelo instanceof Aviao) {
            return "aviao_id";
        } else if (aeromodelo instanceof Helicoptero) {
            return "helicoptero_id";
        } else if (aeromodelo instanceof Jato) {
            return "jato_id";
        } else {
            throw new Exception("Aeromodelo não definido");
        }
    }

    public static void CadastrarVoo(Scanner sc, Connection conn) throws Exception {
        String numero, origem, destino, data, hora;

        System.out.println("====== CADASTRAR VOO ======");
        do {
            System.out.println("Digite o numero do voo: ");
            numero = sc.next();
            if (!Mascara.isValida(numero, "[A-Z]{3}[0-9]{6}")) {
                System.out.println("Numero de voo inválido. Tente novamente com o padrão XXX000000");
            }
        } while (!Mascara.isValida(numero, "[A-Z]{3}[0-9]{6}"));

        System.out.println("Digite a observação do voo: ");
        String observacao = sc.next();
        System.out.println("Digite o nome do piloto: ");
        String piloto = sc.next();
        System.out.println("Digite o nome do copiloto: ");
        String copiloto = sc.next();
        do {
            System.out.println("Digite a origem do voo: ");
            origem = sc.next();
            if (Mascara.isValida(origem, "[A-Z]{3}") == false) {
                System.out.println("Origem inválida. Tente novamente com o padrão XXX");
            }
        } while (Mascara.isValida(origem, "[A-Z]{3}") == false);
        do {
            System.out.println("Digite o destino do voo: ");
            destino = sc.next();
            if (Mascara.isValida(destino, "[A-Z]{3}") == false) {
                System.out.println("Destino inválido. Tente novamente com o padrão XXX");
            }
        } while (Mascara.isValida(destino, "[A-Z]{3}") == false);
        do {
            System.out.println("Digite a data do voo: ");
            data = sc.next();
            if (Mascara.isValida(data, "[0-9]{4}-[0-9]{2}-[0-9]{2}") == false) {
                System.out.println("Data inválida. Tente novamente com o padrão YYYY-MM-DD");
            }
        } while (Mascara.isValida(data, "[0-9]{4}-[0-9]{2}-[0-9]{2}") == false);
        do {
            System.out.println("Digite a hora do voo: ");
            hora = sc.next();
            if (Mascara.isValida(hora, "[0-9]{2}:[0-9]{2}") == false) {
                System.out.println("Hora inválida. Tente novamente com o padrão HH:MM");
            }
        } while (Mascara.isValida(hora, "[0-9]{2}:[0-9]{2}") == false);
        System.out.println("Digite o id da pista: ");
        int pistaId = sc.nextInt();
        Pista pista = Pista.getPistaById(pistaId, conn);
        if (pista == null) {
            throw new Exception("Pista não encontrada");
        }
        System.out.println("Qual o tipo de aeronave do voo?");
        System.out.println(
            "\n1 - Avião" +
            "\n2 - Helicóptero" +
            "\n3 - Jato");
        int tipoAeronave = sc.nextInt();
        switch (tipoAeronave) {
            case 1:
                System.out.println("Digite o id do avião: ");
                Aviao aviao = Aviao.getAviaoById(sc.nextInt(), conn);
                new Voo(0,numero, observacao, piloto, copiloto, origem, destino, data, hora, aviao, pista, conn);
                break;
            case 2:
                System.out.println("Digite o id do helicóptero: ");
                Helicoptero helicoptero = Helicoptero.getHelicopteroById(sc.nextInt(), conn);
                new Voo(0,numero, observacao, piloto, copiloto, origem, destino, data, hora, helicoptero, pista, conn);
                break;
            case 3:
                System.out.println("Digite o id do jato: ");
                Jato jato = Jato.getJatoById(sc.nextInt(), conn);
                new Voo(0,numero, observacao, piloto, copiloto, origem, destino, data, hora, jato, pista, conn);
                break;
        
            default:
                System.out.println("Opção inválida");
                break;
        }   

        System.out.println("Voo cadastrado com sucesso!");
    }

    public static void AlterarVoo(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== ALTERAR VOO ======");
        System.out.println("Digite o id do voo: ");
        int id = sc.nextInt();
        Voo voo = Voo.getVooById(id, conn);
        if (voo == null) {
            throw new Exception("Voo não encontrado");
        } 
        System.out.println("Qual informação deseja alterar?");
        System.out.println(
            "1 - Numero" +
            "\n2 - Observação" + 
            "\n3 - Piloto" +
            "\n4 - Copiloto" +
            "\n5 - Origem" +
            "\n6 - Destino" +
            "\n7 - Data" +
            "\n8 - Hora" +
            "\n9 - Pista" +
            "\n10 - Avião" );
        int opcao = GetOption.get(1, 10, sc);
        System.out.println("Digite o novo valor: ");
        String novoValor = sc.nextLine();

        updateVoo(id, novoValor, opcao, conn);
        System.out.println("Voo alterado com sucesso!");
    }

    public static void DeletarVoo(Scanner sc, Connection conn) throws Exception {
        System.out.println("====== DELETAR VOO ======");
        System.out.println("Digite o id do voo: ");
        int id = sc.nextInt();
        Voo voo = Voo.getVooById(id, conn);
        if (voo == null) {
            throw new Exception("Voo não encontrado");
        } 
        deleteVoo(id, conn);
        System.out.println("Voo deletado com sucesso!");
    }

    public static void ListarVoo(Scanner sc, Connection conn) throws Exception {

        System.out.println("====== LISTAR VOOS ======");
        System.out.println("Deseja exportar o resultado desta listagem para um arquivo txt? (S/N)");
        char opcao = 0;
        do {
            opcao = sc.next().charAt(0);
            if (opcao != 'S' && opcao != 'N') {
                System.out.println("Opção inválida. Tente novamente. (S/N)");
                opcao = 0;
            }
        } while (opcao == 0);

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM voo");
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            int idAviao = rs.getInt("aviao_id");
            int idJato = rs.getInt("jato_id");
            int idHelicoptero = rs.getInt("helicoptero_id");

            if (idAviao != 0) {
                Aviao aviao = Aviao.getAviaoById(idAviao, conn);
                Voo voo = new Voo(
                    rs.getInt("id"),
                    rs.getString("numero"),
                    rs.getString("observacao"),
                    rs.getString("piloto"),
                    rs.getString("copiloto"),
                    rs.getString("origem"),
                    rs.getString("destino"),
                    rs.getString("data"),
                    rs.getString("hora"),
                    aviao,
                    Pista.getPistaById(rs.getInt("pista_id"), conn),
                    conn
                );
                System.out.println(voo);

                try {
                    if (opcao == 'S') {
                        BufferedReader br = new BufferedReader(new FileReader("RegistroVoos.txt"));
                        String linha = br.readLine();
                        PrintWriter criaArquivo;
                        BufferedWriter bw;

                        if (linha == null) {
                            criaArquivo = new PrintWriter("RegistroVoos.txt");
                            criaArquivo.println(voo);
                            criaArquivo.close();
                        } else {
                            bw = new BufferedWriter(new FileWriter("RegistroVoos.txt", true));
                            bw.append(voo.toString());
                            bw.newLine();
                            bw.close();
                        }

                        br.close();
                    }
                } catch (Exception e) {
                    System.out.println("\n Erro ao exportar para txt!" + e.getMessage());
                }

            } else if (idJato != 0) {
                Jato jato = Jato.getJatoById(idJato, conn);
                Voo voo = new Voo(
                    rs.getInt("id"),
                    rs.getString("numero"),
                    rs.getString("observacao"),
                    rs.getString("piloto"),
                    rs.getString("copiloto"),
                    rs.getString("origem"),
                    rs.getString("destino"),
                    rs.getString("data"),
                    rs.getString("hora"),
                    jato,
                    Pista.getPistaById(rs.getInt("pista_id"), conn),
                    conn
                );
                System.out.println(voo);

                try {
                    if (opcao == 'S') {
                        PrintWriter criaArquivo = new PrintWriter("RegistroVoos.txt");
                        criaArquivo.println(voo);
                        criaArquivo.close();
                    }
                } catch (Exception e) {
                    System.out.println("\n Erro ao exportar para txt!" + e.getMessage());
                }

            } else if (idHelicoptero != 0) {
                Helicoptero helicoptero = Helicoptero.getHelicopteroById(idHelicoptero, conn);
                Voo voo = new Voo(
                    rs.getInt("id"),
                    rs.getString("numero"),
                    rs.getString("observacao"),
                    rs.getString("piloto"),
                    rs.getString("copiloto"),
                    rs.getString("origem"),
                    rs.getString("destino"),
                    rs.getString("data"),
                    rs.getString("hora"),
                    helicoptero,
                    Pista.getPistaById(rs.getInt("pista_id"), conn),
                    conn
                );
                System.out.println(voo);

                try {
                    if (opcao == 'S') {
                        PrintWriter criaArquivo = new PrintWriter("RegistroVoos.txt");
                        criaArquivo.println(voo);
                        criaArquivo.close();
                    }
                } catch (Exception e) {
                    System.out.println("\n Erro ao exportar para txt!" + e.getMessage());
                }

            }
        }
    }

    public static Voo getVooById(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM voo WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {

            Aeromodelo aero = null;

            if (rs.getInt("aviao_id") != 0) {
                aero = Aviao.getAviaoById(rs.getInt("aviao_id"), conn);
            } else if (rs.getInt("helicoptero_id") != 0) {
                aero = Helicoptero.getHelicopteroById(rs.getInt("helicoptero_id"), conn);
            } else if (rs.getInt("jato_id") != 0) {
                aero = Jato.getJatoById(rs.getInt("jato_id"), conn);
            } 

            Voo voo = new Voo(
                rs.getInt("id"), 
                rs.getString("numero"), 
                rs.getString("observacao"), 
                rs.getString("piloto"), 
                rs.getString("copiloto"),
                rs.getString("origem"),
                rs.getString("destino"),
                rs.getString("data"),
                rs.getString("hora"),
                aero,
                Pista.getPistaById(rs.getInt("pista_id"), conn),
                conn);
            return voo;
        } else {
            return null;
        }
    }

    public static void updateVoo(int id, String input, int tipoDado, Connection conn) throws Exception {

        String campo = DefineTipoUpdate.defineCampoUpdate(tipoDado, getVooById(id, conn));

        PreparedStatement stmt = conn.prepareStatement("UPDATE voo SET "+campo+" = ? WHERE id = ?");
        stmt.setString(1, input);
        stmt.setInt(2, id);
        stmt.execute();
    }

    public static void deleteVoo(int id, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM voo WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getPiloto() {
        return piloto;
    }

    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }

    public String getCopiloto() {
        return copiloto;
    }

    public void setCopiloto(String copiloto) {
        this.copiloto = copiloto;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Pista getPista() {
        return pista;
    }

    public void setPista(Pista pista) {
        this.pista = pista;
    }

    @Override
    public String toString() {
        return "\n | ID: " + id + 
                "\n | Numero: " + numero + 
                "\n | Observacao: " + observacao + 
                "\n | Piloto: " + piloto + 
                "\n | Copiloto: " + copiloto + 
                "\n | Origem: " + origem + 
                "\n | Destino: " + destino + 
                "\n | Data: " + data + 
                "\n | Hora: " + hora + 
                "\n | Pista: " + pista.getNumero() + 
                "\n | Aeromodelo: " + aeromodelo.getId();
    }

}