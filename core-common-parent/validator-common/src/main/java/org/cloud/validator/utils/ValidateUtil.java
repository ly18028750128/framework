package org.cloud.validator.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;
import org.cloud.vo.ValidateResultVO;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public final class ValidateUtil {

    private ValidateUtil() {
    }

    private final static ValidateUtil instance = new ValidateUtil();

    public static ValidateUtil single() {
        return instance;
    }

    private Validator validator;

    public void setValidator(Validator validator) {
        Assert.isNull(this.validator, "已经设置过不能重复设置");
        this.validator = validator;
    }

    /**
     * 校验数据是否通过校验，通过vo注解，参考网上的hibernate-validator的相关注解，相关示例请参考FrameDataDimension,参考代码请看FrameDataDimensionController.insertOrUpdateBatch
     *
     * @param value
     * @param groupCls
     * @param <T>
     * @return
     */
    public <T> List<ValidateResultVO> validate(T value, Class<?> groupCls) {
        Set<ConstraintViolation<T>> validateResult = validator.validate(value, groupCls);
        if (CollectionUtils.isEmpty(validateResult)) {
            return null;
        }
        List<ValidateResultVO> resultVOS = new ArrayList<>();
        Iterator<ConstraintViolation<T>> resultIter = validateResult.iterator();
        while (resultIter.hasNext()) {
            ConstraintViolation<T> result = resultIter.next();
            resultVOS.add(new ValidateResultVO(result.getPropertyPath().toString(), result.getMessage()));
        }
        return resultVOS;
    }

    public <T> List<ValidateResultVO> validate(T value) {
        return validate(value, Default.class);
    }

    /**
     * 校验List的数据是否通过校验，通过vo注解，参考网上的hibernate-validator的相关注解，相关示例请参考FrameDataDimension,参考代码请看FrameDataDimensionController.insertOrUpdateBatch
     *
     * @param values
     * @param groupCls
     * @param <T>
     * @return
     */
    public <T> Map<Integer, List<ValidateResultVO>> validate(@NotEmpty List<T> values, Class<?> groupCls) {
        Map<Integer, List<ValidateResultVO>> result = new LinkedHashMap<>(10);
        for (int idx = 0; idx < values.size(); idx++) {
            List<ValidateResultVO> singleValidateResult = validate(values.get(idx), groupCls);
            if (!CollectionUtils.isEmpty(singleValidateResult)) {
                result.put(idx, singleValidateResult);
            }
        }
        return result;
    }

    public <T> Map<Integer, List<ValidateResultVO>> validate(@NotEmpty List<T> values) {
        return validate(values, Default.class);
    }
}
