package com.kakaopay.coupon.repository.coupon;

import com.kakaopay.coupon.domain.coupon.dto.CouponDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class CouponJdbcRepository {
  private final int BATCH_SIZE = 10000;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public int createCoupon(List<CouponDto> couponDtoList) {
        int batchCount = 0;
        List<CouponDto> subItems = new ArrayList<>();
        for (int i = 0; i < couponDtoList.size(); i++) {
          subItems.add(couponDtoList.get(i));
          if ((i + 1) % BATCH_SIZE == 0) {
            batchCount = batchInsert(BATCH_SIZE, batchCount, subItems);
          }
        }
        if (!subItems.isEmpty()) {
          batchCount = batchInsert(BATCH_SIZE, batchCount, subItems);
        }
        return batchCount;
  }

  private int batchInsert(int batchSize, int batchCount, List<CouponDto> subItems) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO COUPON (`COUPON_NUM`, `STATUS`, `EXPIRATION_AT`, `CREATED_AT`, `UPDATED_AT`, `ENABLED`, `ISSUED`) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                  @Override
                  public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, subItems.get(i).getCouponNum());
                        ps.setString(2, subItems.get(i).getStatus().toString());
                        ps.setString(3, subItems.get(i).getExpirationAt().toString());
                        ps.setString(4, LocalDateTime.now().toString());
                        ps.setString(5, LocalDateTime.now().toString());
                        ps.setBoolean(6, true);
                        ps.setBoolean(7, false);
                  }

                  @Override
                  public int getBatchSize() {
                    return subItems.size();
                  }
                });
        subItems.clear();
        batchCount++;
        return batchCount;
  }

}
