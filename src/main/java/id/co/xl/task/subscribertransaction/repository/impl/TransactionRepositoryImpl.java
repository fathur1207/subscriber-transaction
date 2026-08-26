package id.co.xl.task.subscribertransaction.repository.impl;

import id.co.xl.task.subscribertransaction.model.entity.TransactionDetail;
import id.co.xl.task.subscribertransaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${query.transaction.fetchbymsisdn}")
    private String queryFetchByMSISDN;

    @Override
    public List<TransactionDetail> fetchByMSISDN(String msisdn) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("msisdn", msisdn);
        return namedParameterJdbcTemplate.query(queryFetchByMSISDN, params, BeanPropertyRowMapper.newInstance(TransactionDetail.class));
    }
}
