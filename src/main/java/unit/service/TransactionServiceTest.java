package unit.service;

//
//@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

//    @Mock
//    private DateTimeConverter dateTimeConverter;
//
//    @Mock
//    private StatisticsService statisticsService;
//    @Mock
//    private TransactionCache transactionCache;
//    @Mock
//    private Transactions transactions;
//
//
//    @InjectMocks
//    private
//    TransactionService transactionService;

//    @Test
//    public void shouldSaveAValidTransactionSuccessfully() throws Exception {
//
//        Transactions validTransaction = new Transactions("44.00", "2019-05-30T07:14:34.755Z");
//        when(transactions.isTxnTimeInLastSixtySeconds(1458251153L)).thenReturn(Option.of(validTransaction));
//        when(statisticsService.saveStats(validTransaction)).thenReturn(new Statistics());
//        Option<Statistics> maybeStatSummary = transactionService.saveTransaction(validTransaction,1458251153L);
//        assertTrue(maybeStatSummary.isDefined());
//    }
//
//    @Test
//    public void shouldReturnNoneMonadAndNotSaveTheTransactionIfTransactionIsOlder() throws Exception {
//
//        Transactions inValidTransaction = new Transactions("44.00", "2019-05-30T07:14:34.755Z");
//        verify(statisticsService, times(0)).saveStats(inValidTransaction);
//        Option<Statistics> maybeStatSummary = transactionService.saveTransaction(inValidTransaction,1559251153L);
//        assertTrue(maybeStatSummary.isDefined());
//    }
}
