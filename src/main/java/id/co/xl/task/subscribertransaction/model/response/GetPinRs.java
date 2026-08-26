package id.co.xl.task.subscribertransaction.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetPinRs {
    private String status;
    private String code;
    private String message;
    private String data;
}
