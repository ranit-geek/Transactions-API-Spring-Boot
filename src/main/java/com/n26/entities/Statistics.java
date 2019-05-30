package com.n26.entities;

import com.n26.helpers.BigDecimalSummaryStatistics;

import java.math.BigDecimal;
import lombok.*;

@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Statistics {

    private String sum;
    private String avg;
    private String max;
    private String min;
    private Long count;

    public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count) {
        this.sum = sum.toString();
        this.avg = avg.toString();
        this.max = max.toString();
        this.min = min.toString();
        this.count = count;
    }

    public Statistics(BigDecimalSummaryStatistics bigDecimalSummaryStatistics){
        this.avg = bigDecimalSummaryStatistics.getAverage().toString();
        this.sum = bigDecimalSummaryStatistics.getSum().toString();
        this.count = bigDecimalSummaryStatistics.getCount();
        this.max = bigDecimalSummaryStatistics.getMax().toString();
        this.min = bigDecimalSummaryStatistics.getMin().toString();
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
