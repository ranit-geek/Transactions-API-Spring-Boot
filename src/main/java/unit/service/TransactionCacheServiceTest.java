package unit.service;

import com.n26.dao.TransactionCache;
import com.n26.helpers.BigDecimalSummaryStatistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TransactionCacheServiceTest {

    @InjectMocks
    private
    TransactionCache transactionCache;


    @Test
    public void insertIntoCacheAndAggregate() {

        BigDecimal result = transactionCache.insertStatsInCache(5678L,
                new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP));
        BigDecimalSummaryStatistics stats = transactionCache.aggregate();
        assertThat(stats.getCount(), is(1L));
        assertThat(stats.getSum(), is(new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP)));
        assertThat(stats.getMax(), is(new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP)));
        assertThat(stats.getMin(), is(new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP)));
        assertThat(stats.getAverage(), is(new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP)));

    }

    @Test
    public void removeFromCache() {

        BigDecimal result = transactionCache.insertStatsInCache(5678L,
                new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP));
        boolean isRemoved = transactionCache.removeFromCache(5978999L);
        assertThat(transactionCache.isCacheEmpty(), is(true));
    }


}
