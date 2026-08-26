package id.co.xl.task.subscribertransaction.model.entity;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TransactionDetail {
    private Integer id;
    private String msisdn;
    private String transactionType; // maps from column transaction_type
    private String productName; // maps from column product_name
    private int amount;
    private LocalDateTime transactionDate; // maps from column transaction_date (datetime)
}
