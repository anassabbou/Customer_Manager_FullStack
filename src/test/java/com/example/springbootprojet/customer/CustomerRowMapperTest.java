package com.example.springbootprojet.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper=new CustomerRowMapper();
        ResultSet rs= mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("anass");
        when(rs.getString("email")).thenReturn("anass@gmail.com");
        when(rs.getInt("age")).thenReturn(24);

        // When
        Customer actual= customerRowMapper.mapRow(rs,1);
        // Then
            Customer expected= new Customer(1,"anass","anass@gmail.com",24);
            assertThat(actual).isEqualTo(expected);
    }
}