package com.tmqt.dao;

import com.tmqt.model.Monthcheck;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(value = "MonthcheckMapper")
public interface MonthcheckMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table monthcheck
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table monthcheck
     *
     * @mbg.generated
     */
    int insert(Monthcheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table monthcheck
     *
     * @mbg.generated
     */
    int insertSelective(Monthcheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table monthcheck
     *
     * @mbg.generated
     */
    Monthcheck selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table monthcheck
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Monthcheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table monthcheck
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Monthcheck record);

    List<Monthcheck> selectByIf(Map conditions);

    List<Monthcheck> selectAll();

    int countMonthcheck();
}