package utils;

public enum UpdateAviao {
    MODELO,
    MARCA,
    PREFIXO,
    CAPACIDADE,
    COMPANHIA_ID;

    public static String getValue(int value) {
        int ordinalValue = value - 1;
        return UpdateAviao.values()[ordinalValue].toString().toLowerCase();
    }
}
