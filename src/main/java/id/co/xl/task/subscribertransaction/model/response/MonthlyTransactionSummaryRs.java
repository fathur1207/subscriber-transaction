package id.co.xl.task.subscribertransaction.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MonthlyTransactionSummaryRs {
    private String month;
    private long totalAmount;
    private long totalTransaction;
}
