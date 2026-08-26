package id.co.xl.task.subscribertransaction.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetMonthlyTransactionRq {
    private String msisdn;
    private String pin;
}
