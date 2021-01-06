package com.tongtech.common.enums;

/**
 * 此Form的共享范围
 * @author 1
 */
public enum FormScope {
    User("User","属于某个用户"),
    Sys("Sys", "整个系统共享");

    private String code;
    private String displayName;

    private FormScope(String code, String displayName){
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
