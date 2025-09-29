package com.mv.sulworkcafe.util;

public final class CpfValidator {

    private CpfValidator() {}

    public static String normalize(String cpf) {
        if (cpf == null) return "";
        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValid(String cpf) {
        String normalized = normalize(cpf);
        return normalized.length() == 11;
    }
}
