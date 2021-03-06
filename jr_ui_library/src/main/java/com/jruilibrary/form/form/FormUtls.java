package com.jruilibrary.form.form;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.jruilibarary.widget.lineformview.LineFromView;
import com.jruilibrary.form.check.FormCheckInterface;
import com.jruilibrary.form.check.RoutineVerification;
import com.jruilibrary.form.check.ViewAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by zhush on 2016/10/8.
 * E-mail zhush@jerei.com
 */
public class FormUtls {
    /**
     * 设置表单初始数据
     *
     * @param object
     */
    public static boolean objectToForm(Object page, Object object) {
        HashMap<String, ViewAttribute> map = FormInit.allLineFormViewMap.get(page.getClass().getName());
        if (map == null) {
            return false;
        }


        Field[] field = object.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            ViewAttribute va = map.get(name);
            try {
                arrangementData(va.getView(), field[i], object, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    /**
     * 获取表单数据并且檢查
     * 返回空 这映射失败
     * @param classz
     */
    public static <T> T formToObjectAndCheck(FormCheckInterface page, Class<T> classz) {
        Object object = null;
        try {
            object = classz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        }
        if(object==null){
            Log.e("FormUtls","该对象必须要有 public 的 空构造函数");
            return null;
        }
        HashMap<String, ViewAttribute> map = FormInit.allLineFormViewMap.get(page.getClass().getName());
        if (map == null) {
            return null;
        }
        if (!checkParam(page)) {
            return null;
        }
        Field[] field = object.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String name = field[i].getName();
            ViewAttribute va = map.get(name);
            if (va != null) {
                try {
                    arrangementData(va.getView(), field[i], object, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("表单映射失败");
                }
            }

        }
        return (T)object;
    }

    public static void arrangementData(View view, Field field, Object object, boolean isSet) throws Exception {

        String name = field.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set
        String type = field.getGenericType().toString(); // 获取属性的类型
        if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = null;
            if (isSet) {
                m = object.getClass().getMethod("set" + name, String.class);
                m.invoke(object, getContent(view));
            } else {
                m = object.getClass().getMethod("get" + name);
                String strValue = m.invoke(object) + "";
                if (!TextUtils.isEmpty(strValue)) {

                }
                setContent(view, m.invoke(object) + "");

            }
        }

        if (type.equals("class java.lang.Integer")) {

            Method m = null;
            if (isSet) {
                m = object.getClass().getMethod("set" + name, Integer.class);
                try {
                    m.invoke(object, Integer.parseInt(getContent(view)));
                }catch (Exception e){}

            } else {
                m = object.getClass().getMethod("get" + name);
                Integer integer = (Integer) m.invoke(object);
                if (integer != null) {
                    setContent(view, m.invoke(object) + "");
                }

            }

        }

        if (type.equals("class java.lang.Boolean")) {

            Method m = null;
            if (isSet) {
                m = object.getClass().getMethod("set" + name, Boolean.class);
                String value = getContent(view);
                if (!value.equals("true") && !value.equals("false")) {
                    throw new RuntimeException("switchButton必须是boolean值");
                }
                m.invoke(object, Boolean.parseBoolean(value));
            } else {
                m = object.getClass().getMethod("get" + name);
                setContent(view, m.invoke(object) + "");
            }

        }

        if (type.equals("class java.lang.Long")) {

            Method m = null;
            if (isSet) {
                m = object.getClass().getMethod("set" + name, Long.class);
                try {
                m.invoke(object, Long.parseLong(getContent(view)));
                }catch (Exception e){}
            } else {
                m = object.getClass().getMethod("get" + name);
                setContent(view, m.invoke(object) + "");
            }

        }
        if (type.equals("class java.lang.Double")) {

            Method m = null;
            if (isSet) {
                m = object.getClass().getMethod("set" + name, Double.class);
                try {
                m.invoke(object, Double.parseDouble(getContent(view)));
                }catch (Exception e){}
            } else {
                m = object.getClass().getMethod("get" + name);
                setContent(view, m.invoke(object) + "");
            }

        }

    }

    static String getContent(View v) {
        if (v instanceof LineFromView) {
           return  ((LineFromView)v).getContent();
        }
        if (v instanceof CheckBox) {

            boolean selected =((CheckBox) v).isChecked();
            return selected+"";
        }
        if (v instanceof EditText) {
            return ((EditText) v).getText() + "";
        }
        if (v instanceof TextView) {
            return ((TextView) v).getText() + "";
        }

        if (v instanceof Spinner) {
//            return ((Spinner) v).getSelectedItemPosition() + "";

            return  ((Spinner) v).getSelectedItem().toString();
        }


        return "";
    }

    static void setContent(View v, String value) {


        if (v instanceof LineFromView) {
            ((LineFromView)v).setContent(value);
            return;
        }
        if (v instanceof CheckBox) {
            boolean selected = false;
            try {
                selected = Boolean.parseBoolean(value);
            } catch (Exception e) {
                throw new RuntimeException("选择器 value必须是布尔值");
            }
            ((CheckBox) v).setChecked(selected);
            return;
        }

        if (v instanceof EditText) {
            ((EditText) v).setText(value);
            return;
        }
        if (v instanceof TextView) {
            ((TextView) v).setText(value);
            return;
        }
//        if (v instanceof Spinner) {
//            int i = 0;
//            try {
//                i = Integer.parseInt(value);
//            } catch (Exception e) {
//                throw new RuntimeException("选择器 value必须是数字");
//            }
//            ((Spinner) v).setSelection(i,true);
//            return;
//        }


    }


    /**
     * 检查表单
     *
     * @return
     */
    public static boolean checkParam(FormCheckInterface page) {
        HashMap<String, ViewAttribute> map = FormInit.allLineFormViewMap.get(page.getClass().getName());
        if (map != null) {
            Set<String> keys1 = map.keySet();
            for (String key : keys1) {
                ViewAttribute va = map.get(key);
                if (TextUtils.isEmpty(getContent(va.getView())) && !va.isNull()) {
                    page.formCheckNullCall(va.getView(), "请正确输入" + va.getMessage());
                    return false;
                }

                if (va.getCheckType() != null) {
                    if (!check(page, va)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * 检查参数是否合法
     * @param page
     * @param va
     * @return
     */
    public static boolean check(FormCheckInterface page, ViewAttribute va) {
        //自定义检查信息
        if (!page.formCheck(va.getView())) {
//            page.formCheckParamCall(va.getView(), "");
            return false;
        }

        switch (va.getCheckType()) {
            case PHONE:
                if (!RoutineVerification.isPhoneNum(getContent(va.getView()))) {

                    page.formCheckParamCall(va.getView(), "请正确输入手机号");
                    return false;
                }
                break;
            case PASSWORD:
                if (!RoutineVerification.is6To12(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "请正确输入密码");
                    return false;
                }
                break;
            case EMAIL:
                if (!RoutineVerification.isEmail(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "请正确输入邮箱");
                    return false;
                }
            case CHINESE:
                if (!RoutineVerification.isChinese(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "只能输入中文");
                    return false;
                }
            case IDCARD:
                if (!RoutineVerification.isIdCard(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "身份证格式不正确");
                    return false;
                }
            case ISDATA:
                if (!RoutineVerification.isData(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "日期格式不正确");
                    return false;
                }
            case AMOUNT_MONEY:
                if (!RoutineVerification.isAmountMoney(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "金额不正确，最多两位小数");
                    return false;
                }
            case URL:
                if (!RoutineVerification.isURL(getContent(va.getView()))) {
                    page.formCheckParamCall(va.getView(), "请输入正确的url地址");
                    return false;
                }

                break;
        }
        return true;
    }
}
