package com.transsion.authentication.infrastructure.utils;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象转换配置
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
public class ModelMapperUtils {
    /**
     * 单个实体之间的转换
     *
     * @param obj
     * @param result
     * @param <T>
     * @return
     */
    public static <T> T map(Object obj, Class<T> result) {
        ModelMapper modelMapper = getModelMapper();
        T dto = modelMapper.map(obj, result);
        return dto;
    }

    /**
     * 实体列表之间的转换
     *
     * @param obj
     * @param result
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> List<T> map(List<S> obj, Class<T> result) {
        List<T> list = new ArrayList<>();
        ModelMapper modelMapper = getModelMapper();
        for (S s : obj) {
            T dto = modelMapper.map(s, result);
            list.add(dto);
        }
        return list;
    }

    /**
     * 实体转换
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     */
    public static <S, T> void map(S source, T target) {
        ModelMapper modelMapper = getModelMapper();
        modelMapper.map(source, target);
    }


    /**
     * 获取ModelMapper配置
     *
     * @return
     */
    private static ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        //设置候选属性,只复制非null的属性
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        //设置匹配策略为严格（默认为标准）
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        //开启字段匹配
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        //忽略歧义（如果出现歧义跳过）
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
