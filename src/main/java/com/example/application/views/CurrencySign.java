package com.example.application.views;

enum CurrencySign {
    inr("₹"), usd("$"), eur("€"), gbp("£");
    private final String text;

    CurrencySign(String text) {
        this.text = text;
    }

    public String getSymbol() {
        return this.text;
    }

    public static String getSymbol(String s) {
        return CurrencySign.valueOf(s).getSymbol();
    }
}
