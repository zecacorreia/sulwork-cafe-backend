package com.mv.sulworkcafe.util;

public final class CpfValidator {

    private CpfValidator() {}

    public static String normalize(String cpf) {
        if (cpf == null) return "";
        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValid(String cpf) {
        String n = normalize(cpf);
        if (n.length() != 11) return false;
        return !n.matches("(\\d)\\1{10}");
    }
}