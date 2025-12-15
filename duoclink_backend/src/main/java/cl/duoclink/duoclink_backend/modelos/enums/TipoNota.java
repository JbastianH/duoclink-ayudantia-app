package cl.duoclink.duoclink_backend.modelos.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoNota {
    TEXTO("texto"),
    MULTIMEDIA("multimedia"),
    ENLACE("enlace"),
    DOCUMENTO("documento");

    private final String codigo;

    TipoNota(String codigo) {
        this.codigo = codigo;
    }

    @JsonValue
    public String getCodigo() {
        return codigo;
    }

    @JsonCreator
    public static TipoNota desde(String valor) {
        if (valor == null) return null;
        String v = valor.trim().toLowerCase();
        switch (v) {
            case "texto":
            case "text":
                return TEXTO;
            case "multimedia":
            case "media":
                return MULTIMEDIA;
            case "enlace":
            case "link":
                return ENLACE;
            case "documento":
            case "document":
                return DOCUMENTO;
            default:
                throw new IllegalArgumentException("Tipo de nota inválido: " + valor);
        }
    }
}