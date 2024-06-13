package com.transsion.daconsole.infrastructure.constants;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/5/24
 */
public enum ModelEnum {
    FACTORY(1, "工厂"),
    RESEARCH(2, "研发版本"),
    FANS(3, "粉丝版本"),
    AFTET_SAVE(4, "售后版本");

    /**
     * 算法传输 code
     */
    private final Integer code;
    /**
     * 算法描述
     */
    private final String modelDescribe;

    ModelEnum(Integer code, String modelDescribe) {
        this.code = code;
        this.modelDescribe = modelDescribe;
    }

    public Integer getCode() {
        return code;
    }

    public String getModelDescribe() {
        return modelDescribe;
    }

    /**
     * 通过 Code获取算法枚举
     *
     * @param code
     * @return
     */
    public ModelEnum getModelDescribe(Integer code) {
        for (ModelEnum value : ModelEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
