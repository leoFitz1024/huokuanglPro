package com.tmqt.model;

import java.util.Date;

public class User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.username
     *
     * @mbg.generated
     */
    private String username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.password
     *
     * @mbg.generated
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.competence
     *
     * @mbg.generated
     */
    private Integer competence;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.addtime
     *
     * @mbg.generated
     */

    private Date addtime;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.remarks
     *
     * @mbg.remarks
     */

    private String remarks;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.username
     *
     * @return the value of user.username
     *
     * @mbg.generated
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.username
     *
     * @param username the value for user.username
     *
     * @mbg.generated
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.password
     *
     * @return the value of user.password
     *
     * @mbg.generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.password
     *
     * @param password the value for user.password
     *
     * @mbg.generated
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.competence
     *
     * @return the value of user.competence
     *
     * @mbg.generated
     */
    public Integer getCompetence() {
        return competence;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.competence
     *
     * @param competence the value for user.competence
     *
     * @mbg.generated
     */
    public void setCompetence(Integer competence) {
        this.competence = competence;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.addtime
     *
     * @return the value of user.addtime
     *
     * @mbg.generated
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.addtime
     *
     * @param addtime the value for user.addtime
     *
     * @mbg.generated
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}