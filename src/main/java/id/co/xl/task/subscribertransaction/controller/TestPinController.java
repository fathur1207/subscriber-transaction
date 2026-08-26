package id.co.xl.task.subscribertransaction.controller;

import id.co.xl.task.subscribertransaction.model.response.GetPinRs;
import id.co.xl.task.subscribertransaction.service.PinService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestPinController {

    private final PinService pinService;

    public TestPinController(PinService pinService) {
        this.pinService = pinService;
    }

    @GetMapping("/pin/{msisdn}")
    public ResponseEntity<GetPinRs> testGetPin(@PathVariable String msisdn) {
        GetPinRs result = pinService.getPin(msisdn);

        // Mirror the upstream HTTP status based on the code we got back,
        // instead of always defaulting to 200.
        HttpStatus httpStatus = "00".equals(result.getCode())
                ? HttpStatus.OK
                : "01".equals(result.getCode())
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(httpStatus).body(result);
    }
}