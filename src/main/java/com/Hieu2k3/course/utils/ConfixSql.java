package com.Hieu2k3.course.utils;

public class ConfixSql {

    public interface User {
        // lấy ra tất cả user (ngoại trừ admin) với truyền admin
        String GET_ALL_USER = "SELECT o FROM User o WHERE o.active = true AND (:keyword IS NULL OR :keyword = '' " +
                "OR o.fullName LIKE %:keyword% " +
                "OR o.address LIKE %:keyword% " +
                "OR o.phoneNumber LIKE %:keyword%) " +
                "AND LOWER(o.role.name) = 'user'";
    }

}