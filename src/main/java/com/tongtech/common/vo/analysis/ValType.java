package com.tongtech.common.vo.analysis;


public enum ValType
{
    Equal(0,          "eq"),
    In(1,              "in"),
    NotIn(2,          "notin"),
    Range(3,          "range"),
    Greater(4,         ">"),
    GreaterOrEqual(5, ">="),
    Less(6,            "<"),
    LessOrEqual(7,    "<="),
    Sql(8,             "sql"),
    Contain(9,        "contain"),
    StartWith(10,      "startwith"),
    EndWith(11,        "endwith"),
    NotEqual(12,       "!="),
    All(13,            "in");

    private ValType(Integer code, String displayName){
        this.code = code;
        this.displayName = displayName;
    }

    private Integer code;
    private String displayName;

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return code.toString();
    }
}

