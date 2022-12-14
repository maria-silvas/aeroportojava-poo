package classes;

import java.sql.Connection;
import java.util.Scanner;

import utils.Mascara;

// Classe abstrata que representa um aeromodelo - classe pai de Aviao, Helicoptero e Jato
public abstract class Aeromodelo {

    private int id;
    private String marca;
    private String modelo;

    protected Aeromodelo(int id, String marca, String modelo) throws Exception {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    // Metodo Realiza cadastramento dos filhos - Aviao, Helicoptero e Jato
    public static void CadastrarAeromodelo(Scanner sc, Connection conn) throws Exception {
        String marca, modelo, cor;
        int capacidade;

        System.out.println("Qual tipo de Aeromodelo deseja cadastrar?");
        System.out.println(
                "1 - Avião \n" +
                        "2 - Helicóptero \n" +
                        "3 - Jato \n");
        int opcao = sc.nextInt();

        switch (opcao) {
            case 1:
                System.out.println("Digite o modelo do avião: ");
                modelo = sc.next();
                System.out.println("Digite a marca do avião: ");
                marca = sc.next();
                boolean isValido = false;
                String prefixo = "";
                do {
                    System.out.println("Digite o prefixo do avião: ");
                    prefixo = sc.next();
                    isValido = Mascara.isValida(prefixo, "[A-Z]{3}[0-9]{4}");
                    if (isValido == false) {
                        System.out.println("Prefixo inválido. Tente novamente com o padrão XXX0000");
                    }
                } while (isValido == false);
                System.out.println("Digite a capacidade do avião: ");
                capacidade = sc.nextInt();
                if (capacidade < 0) {
                    capacidade = capacidade * (-1);
                    System.out.println("Capacidade inválida. Valor ajustado para " + capacidade);
                }
                System.out.println("Digite o id da companhia do avião: ");
                int idCompanhia = sc.nextInt();
                Companhia companhia = Companhia.getCompanhiaById(idCompanhia, conn);
                if (companhia == null) {
                    System.out.println("Companhia não encontrada");
                } else {
                    new Aviao(0, modelo, marca, prefixo, capacidade, companhia, conn);
                    System.out.println("Avião cadastrado com sucesso!");
                }
                break;
            case 2:
                System.out.println("Digite a marca do helicóptero: ");
                marca = sc.next();
                System.out.println("Digite o modelo do helicóptero: ");
                modelo = sc.next();
                System.out.println("Digite a capacidade do helicóptero: ");
                capacidade = sc.nextInt();
                if (capacidade < 0) {
                    capacidade = capacidade * (-1);
                    System.out.println("Capacidade inválida. Valor ajustado para " + capacidade);
                }
                System.out.println("Digite a cor do helicóptero: ");
                cor = sc.next();

                new Helicoptero(0, marca, modelo, capacidade, cor, conn);

                System.out.println("Helicóptero cadastrado com sucesso!");
                break;
            case 3:
                System.out.println("Digite a marca do jato: ");
                marca = sc.next();
                System.out.println("Digite o modelo do jato: ");
                modelo = sc.next();
                System.out.println("Digite a cor do jato: ");
                cor = sc.next();
                System.out.println("Digite a velocidade do jato: ");
                int velocidade = sc.nextInt();
                if (velocidade < 0) {
                    velocidade = velocidade * (-1);
                    System.out.println("Velocidade inválida. Valor ajustado para " + velocidade);
                }

                new Jato(0, marca, modelo, cor, velocidade, conn);

                System.out.println("Jato cadastrado com sucesso!");
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    // Metodo Realiza alteração dos filhos - Aviao, Helicoptero e Jato
    public static void AlterarAeromodelo(Scanner sc, Connection conn) throws Exception {
        System.out.println("Qual tipo de Aeromodelo deseja alterar?");
        System.out.println(
                "1 - Avião \n" +
                        "2 - Helicóptero \n" +
                        "3 - Jato \n");
        int opcao = sc.nextInt();

        switch (opcao) {
            case 1:
                System.out.println("Digite o id do avião: ");
                int idAviao = sc.nextInt();
                Aviao aviao = Aviao.getAviaoById(idAviao, conn);
                if (aviao == null) {
                    throw new Exception("Avião não encontrado!");
                } else {
                    System.out.println("Qual informação deseja alterar?");
                    System.out.println(
                            "1 - Modelo \n" +
                                    "2 - Marca \n" +
                                    "3 - Prefixo \n" +
                                    "4 - Capacidade \n" +
                                    "5 - Companhia \n");
                    int opcaoAviao = sc.nextInt();
                    System.out.println("Digite a nova informação: ");
                    String novaInfo = sc.next();

                    Aviao.alterarAviao(idAviao, novaInfo, opcaoAviao, conn);

                    System.out.println("Avião " + aviao.getId() + " alterado com sucesso!");
                }
                break;
            case 2:
                System.out.println("Digite o id do helicóptero: ");
                int idHelicoptero = sc.nextInt();
                Helicoptero helicoptero = Helicoptero.getHelicopteroById(idHelicoptero, conn);
                if (helicoptero == null) {
                    throw new Exception("Helicóptero não encontrado!");
                } else {
                    System.out.println("Qual informação deseja alterar?");
                    System.out.println(
                            "1 - Modelo \n" +
                                    "2 - Marca \n" +
                                    "3 - Capacidade \n" +
                                    "4 - Cor \n");
                    int opcaoHelicoptero = sc.nextInt();
                    System.out.println("Digite a nova informação: ");
                    String novaInfo = sc.next();

                    Helicoptero.alterarHelicoptero(idHelicoptero, novaInfo, opcaoHelicoptero, conn);

                    System.out.println("Helicóptero " + helicoptero.getId() + " alterado com sucesso!");
                }
                break;
            case 3:
                System.out.println("Digite o id do jato: ");
                int idJato = sc.nextInt();
                Jato jato = Jato.getJatoById(idJato, conn);
                if (jato == null) {
                    throw new Exception("Jato não encontrado!");
                } else {
                    System.out.println("Qual informação deseja alterar?");
                    System.out.println(
                            "1 - Modelo \n" +
                                    "2 - Marca \n" +
                                    "3 - Cor \n" +
                                    "4 - Velocidade \n");
                    int opcaoJato = sc.nextInt();
                    System.out.println("Digite a nova informação: ");
                    String novaInfo = sc.next();

                    Jato.alterarJato(idJato, novaInfo, opcaoJato, conn);

                    System.out.println("Jato " + jato.getId() + " alterado com sucesso!");
                }
                break;

            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    // Metodo Realiza exclusão dos filhos - Aviao, Helicoptero e Jato
    public static void DeletarAeromodelo(Scanner sc, Connection conn) throws Exception {
        System.out.println("Qual tipo de Aeromodelo deseja excluir?");
        System.out.println(
                "1 - Avião \n" +
                        "2 - Helicóptero \n" +
                        "3 - Jato \n");
        int opcao = sc.nextInt();

        switch (opcao) {
            case 1:
                System.out.println("Digite o id do avião: ");
                int idAviao = sc.nextInt();
                Aviao aviao = Aviao.getAviaoById(idAviao, conn);
                if (aviao == null) {
                    throw new Exception("Avião não encontrado!");
                } else {
                    Aviao.deletarAviao(idAviao, conn);
                    System.out.println("Avião " + aviao.getId() + " excluído com sucesso!");
                }
                break;
            case 2:
                System.out.println("Digite o id do helicóptero: ");
                int idHelicoptero = sc.nextInt();
                Helicoptero helicoptero = Helicoptero.getHelicopteroById(idHelicoptero, conn);
                if (helicoptero == null) {
                    throw new Exception("Helicóptero não encontrado!");
                } else {
                    Helicoptero.deletarHelicoptero(idHelicoptero, conn);
                    System.out.println("Helicóptero " + helicoptero.getId() + " excluído com sucesso!");
                }
                break;

            case 3:
                System.out.println("Digite o id do jato: ");
                int idJato = sc.nextInt();
                Jato jato = Jato.getJatoById(idJato, conn);
                if (jato == null) {
                    throw new Exception("Jato não encontrado!");
                } else {
                    Jato.deletarJato(idJato, conn);
                    System.out.println("Jato " + jato.getId() + " excluído com sucesso!");
                }
                break;

            default:
                System.out.println("Opção inválida!");
                break;
        }
    }

    // Metodo Realiza listagem dos filhos - Aviao, Helicoptero e Jato
    public static void ListarAeromodelos(Connection conn) throws Exception {
        System.out.println("\n====== AEROMODELOS ======");
        System.out.println("====== Aviões: ");
        Aviao.imprimirAvioes(conn);
        System.out.println("\n====== Helicópteros: ");
        Helicoptero.imprimirHelicopteros(conn);
        System.out.println("\n====== Jatos: ");
        Jato.imprimirJatos(conn);
    }

    @Override
    public String toString() {
        return "\n | ID: " + getId() +
                "\n | Modelo: " + getModelo() +
                "\n | Marca: " + getMarca();
    }
}
