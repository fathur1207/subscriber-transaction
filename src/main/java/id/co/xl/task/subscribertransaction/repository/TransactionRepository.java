package id.co.xl.task.subscribertransaction.repository;

import id.co.xl.task.subscribertransaction.model.entity.TransactionDetail;

import java.util.List;

public interface TransactionRepository {
    List<TransactionDetail> fetchByMSISDN(String msisdn);
}
