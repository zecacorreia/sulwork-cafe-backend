package com.mv.sulworkcafe.util;

public final class CpfValidator {
    private CpfValidator() {}
    public static String onlyDigits(String s){ return s == null ? null : s.replaceAll("\\D", ""); }
    public static boolean isValid(String raw){
        String cpf = onlyDigits(raw);
        if (cpf == null || cpf.length() != 11) return false;
        if (cpf.chars().distinct().count() == 1) return false;
        int d1 = calc(cpf, 10); int d2 = calc(cpf, 11);
        return d1 == Character.getNumericValue(cpf.charAt(9)) && d2 == Character.getNumericValue(cpf.charAt(10));
    }
    private static int calc(String s, int f){ int sum=0; for(int i=0;i<f-1;i++) sum += Character.getNumericValue(s.charAt(i))*(f-i); int mod=(sum*10)%11; return mod==10?0:mod; }
}