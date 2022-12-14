package utils;

import classes.Aviao;
import classes.Companhia;
import classes.Helicoptero;
import classes.Jato;
import classes.Voo;

public class DefineTipoUpdate {

    // Define o campo que será atualizado no banco de dados - por cla
    public static <T> String defineCampoUpdate(int tipo, T obj) {

        String campoUpdate = "";

        if (obj instanceof Aviao) {
            campoUpdate = UpdateAviao.getValue(tipo);
        }

        if (obj instanceof Voo) {
            switch (tipo) {
                case 1: campoUpdate = "numero"; break;
                case 2: campoUpdate = "observacao"; break;
                case 3: campoUpdate = "piloto"; break;
                case 4: campoUpdate = "copiloto"; break;
                case 5: campoUpdate = "origem"; break;
                case 6: campoUpdate = "destino"; break;
                case 7: campoUpdate = "data"; break;
                case 8: campoUpdate = "hora"; break;
                case 9: campoUpdate = "pista_id"; break;
                case 10: campoUpdate = "aviao_id"; break;
                case 11: campoUpdate = "helicoptero_id"; break;
                case 12: campoUpdate = "jato_id"; break;
                default: break;
            }
        }

        if (obj instanceof Companhia) {
            switch (tipo) {
                case 1: campoUpdate = "nome"; break;
                case 2: campoUpdate = "cnpj"; break;
                default: break;
            }
        }

        if (obj instanceof Helicoptero) {
            switch (tipo) {
                case 1: campoUpdate = "modelo"; break;
                case 2: campoUpdate = "marca"; break;
                case 4: campoUpdate = "capacidade"; break;
                case 5: campoUpdate = "cor"; break;
                default: break;
            }
        }

        if (obj instanceof Jato) {
            switch (tipo) {
                case 1: campoUpdate = "modelo"; break;
                case 2: campoUpdate = "marca"; break;
                case 4: campoUpdate = "cor"; break;
                case 5: campoUpdate = "velocidade"; break;
                default: break;
            }
        }

        return campoUpdate;
    }
}
