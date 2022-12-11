package com.example.ApachePOIExcelExample.util;

import java.math.BigDecimal;

public class RuMoneyFormat {

    public int trillion;
    public int billion;
    public int million;
    public int thousand;
    public int units;
    public int cents;
    public char sign = '+';

    public static RuMoneyFormat valueOf(BigDecimal value) {
        var result = new RuMoneyFormat();
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            value = value.abs();
            result.sign = '-';
        }
        var div = value.divideAndRemainder(BigDecimal.valueOf(1e12));
        result.trillion = div[0].intValue();

        div = div[1].divideAndRemainder(BigDecimal.valueOf(1e9));
        result.billion = div[0].intValue();

        div = div[1].divideAndRemainder(BigDecimal.valueOf(1e6));
        result.million = div[0].intValue();

        div = div[1].divideAndRemainder(BigDecimal.valueOf(1e3));
        result.thousand = div[0].intValue();

        result.units = div[1].intValue();
        result.cents = div[1].subtract(BigDecimal.valueOf(result.units)).movePointRight(2).intValue();
        return result;
    }

    @Override
    public String toString() {
        var result = new StringBuilder();
        if (this.trillion > 0) {
            result.append(this.trillion).append(" трл.");
        }
        if (this.billion > 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(this.billion).append(" млрд.");
        }
        if (this.million > 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(this.million).append(" млн.");
        }
        if (this.thousand > 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(this.thousand).append(" тыс.");
        }
        if (this.units > 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(this.units).append(" руб.");
        } else {
            result.append(" руб.");
        }
        if (this.cents > 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(this.cents).append(" коп.");
        }
        if (this.sign == '-') {
            result.insert(0, this.sign);
        }
        return result.toString();
    }

}