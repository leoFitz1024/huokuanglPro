package com.tmqt.dao;

import com.tmqt.model.Inbill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "InbillMapper")
public interface InbillMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table inbill
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table inbill
     *
     * @mbg.generated
     */
    int insert(Inbill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table inbill
     *
     * @mbg.generated
     */
    int insertSelective(Inbill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table inbill
     *
     * @mbg.generated
     */
    Inbill selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table inbill
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Inbill record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table inbill
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Inbill record);

    List<Inbill> selectAll();

    List<Inbill> selectByIf(Map conditions);

    int countInbill();
}