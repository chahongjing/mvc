package com.zjy.common.common;

import com.zjy.baseframework.interfaces.IBaseEnum;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyEditorSupport;

@Slf4j
public class EnumEditor<T extends Enum<T> & IBaseEnum> extends PropertyEditorSupport {

    public Class<T> clazz;

    public EnumEditor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void setAsText(String text) {
        if (text == null || "".equals(text.trim())) {
            setValue(null);
            return;
        }
        try {
            setValue(IBaseEnum.getByValue(clazz, Integer.parseInt(text)));
        } catch (Exception e) {
            log.error("枚举值设置值异常: 枚举class:{},目标值：{}", clazz.getName(), text);
            throw e;
        }

    }
}