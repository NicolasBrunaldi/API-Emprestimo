    package com.minsait.apiEmprestimo.enums;

    import java.math.BigDecimal;

    public enum Relacionamento {

        BRONZE() {
            @Override
            public BigDecimal calculoDoRelacionamento(BigDecimal...params) {

                BigDecimal valorInicial = params[0];
                BigDecimal valorFinal = valorInicial.multiply(BigDecimal.valueOf(1.80));

                return valorFinal;
            }
        },

        PRATA() {
            @Override
            public BigDecimal calculoDoRelacionamento(BigDecimal...params) {

                BigDecimal valorInicial = params[0];

                BigDecimal valorBase = new BigDecimal(5000);
                int resultadoDaComparacao = valorInicial.compareTo(valorBase);

                BigDecimal valorFinal = resultadoDaComparacao > 0
                        ? valorInicial.multiply(BigDecimal.valueOf(1.40))
                        : valorInicial.multiply(BigDecimal.valueOf(1.60));

                return valorFinal;
            }
        },

        OURO() {
            @Override
            public BigDecimal calculoDoRelacionamento(BigDecimal...params) {

                BigDecimal valorInicial = params[0];
                BigDecimal quantidadeDeEmprestimos =params[1];

                BigDecimal valorFinal =quantidadeDeEmprestimos.intValue() > 2
                        ? valorInicial.multiply(BigDecimal.valueOf(1.3))
                        : valorInicial.multiply(BigDecimal.valueOf(1.2));
                return valorFinal;

            }
        };

            public abstract BigDecimal calculoDoRelacionamento(BigDecimal...params);
    }