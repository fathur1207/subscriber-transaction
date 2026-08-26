package id.co.xl.task.subscribertransaction.usecase;

import id.co.xl.task.subscribertransaction.model.entity.TransactionDetail;
import id.co.xl.task.subscribertransaction.model.response.GenericResponse;
import id.co.xl.task.subscribertransaction.model.response.GetPinRs;
import id.co.xl.task.subscribertransaction.model.response.MonthlyTransactionSummaryRs;
import id.co.xl.task.subscribertransaction.repository.TransactionRepository;
import id.co.xl.task.subscribertransaction.service.PinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TransactionUsecase {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PinService pinService;

    public ResponseEntity<Object> getTransactionSummary(String msisdn, String pin) {

        // 1. Validate PIN
        GetPinRs getPinRs = pinService.getPin(msisdn);

        if (!"00".equals(getPinRs.getCode())) {
            // Propagate upstream failure as-is (e.g. "01" subscriber not found)
            GenericResponse<Object> failedResponse = new GenericResponse<>()
                    .setStatus("failed")
                    .setCode(getPinRs.getCode())
                    .setMessage(getPinRs.getMessage());
            HttpStatus status = "01".equals(getPinRs.getCode()) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_GATEWAY;
            return new ResponseEntity<>(failedResponse, status);
        }

        if (getPinRs.getData() == null || !getPinRs.getData().equals(pin)) {
            GenericResponse<Object> invalidPinResponse = new GenericResponse<>()
                    .setStatus("failed")
                    .setCode("04")
                    .setMessage("invalid pin");
            return new ResponseEntity<>(invalidPinResponse, HttpStatus.UNAUTHORIZED);
        }

        // 2. PIN verified — fetch and aggregate transactions by month
        List<TransactionDetail> transactionDetailList = transactionRepository.fetchByMSISDN(msisdn);

        Map<String, long[]> grouped = new LinkedHashMap<>(); // [month, totalAmount, totalTransaction]
        for (TransactionDetail tx : transactionDetailList) {
            String month = tx.getTransactionDate().format(MONTH_FORMATTER);
            long[] agg = grouped.computeIfAbsent(month, m -> new long[2]);
            agg[0] += tx.getAmount();
            agg[1] += 1;
        }

        List<MonthlyTransactionSummaryRs> summaryList = grouped.entrySet().stream()
                .map(e -> new MonthlyTransactionSummaryRs()
                        .setMonth(e.getKey())
                        .setTotalAmount(e.getValue()[0])
                        .setTotalTransaction(e.getValue()[1]))
                .sorted(Comparator.comparing(MonthlyTransactionSummaryRs::getMonth).reversed())
                .toList();

        GenericResponse<List<MonthlyTransactionSummaryRs>> response = new GenericResponse<List<MonthlyTransactionSummaryRs>>()
                .setStatus("ok")
                .setCode("00")
                .setMessage("success")
                .setData(summaryList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
