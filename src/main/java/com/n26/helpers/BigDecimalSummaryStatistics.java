package com.n26.helpers;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.Objects;
@Service
public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

    public static Collector<BigDecimal,?,BigDecimalSummaryStatistics> statistics() {
        return Collector.of(BigDecimalSummaryStatistics::new,
                BigDecimalSummaryStatistics::accept, BigDecimalSummaryStatistics::merge);
    }
    private BigDecimal sum = BigDecimal.ZERO,min,max;
    private long count;

    public BigDecimalSummaryStatistics() {
        count=0;
        sum=new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
        min=new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
        max=new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void clear()
    {
        count=0;
        sum=new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
        min=new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);
        max=new BigDecimal(0.00).setScale(2,BigDecimal.ROUND_HALF_UP);

    }

    public synchronized void  accept(BigDecimal t) {
        if(count == 0) {
            Objects.requireNonNull(t);
            count = 1;
            sum = t;
            min = t;
            max = t;
        }
        else {
            sum = sum.add(t);
            if(min.compareTo(t) > 0) min = t;
            if(max.compareTo(t) < 0) max = t;
            count++;
        }
    }

    public void combine(BigDecimalSummaryStatistics other) {
        count += other.count;
        sum = sum.add(other.sum);
        min = min.min(other.min);
        max = max.max(other.max);
    }

    public  BigDecimalSummaryStatistics merge(BigDecimalSummaryStatistics s) {
        if(s.count > 0) {
            if(count == 0) {
                count = s.count;
                sum = s.sum;
                min = s.min;
                max = s.max;
            }
            else {
                sum = sum.add(s.sum);
                if(min.compareTo(s.min) > 0) min = s.min;
                if(max.compareTo(s.max) < 0) max = s.max;
                count += s.count;
            }
        }
        return this;
    }

    public long getCount() {
        return count;
    }

    public BigDecimal getSum()
    {
        return sum;
    }

    public  BigDecimal getAverage()
    {
        return count < 2? sum: sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{count=%d, sum=%f, min=%f, average=%f, max=%f}",
                this.getClass().getSimpleName(),
                getCount(),
                getSum(),
                min,
                getAverage(),
                max);
    }

}
