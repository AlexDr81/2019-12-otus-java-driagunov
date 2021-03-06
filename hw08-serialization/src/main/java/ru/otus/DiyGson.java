package ru.otus;

import java.lang.reflect.Field;
import java.util.Collection;

public class DiyGson {
    public String toJson(Object src) {
        return getParseObject(src);
    }

    private String getParseObject(Object object){
        if (object == null) return null;

        if ( isPrimitive(object) ){
            return object.toString();
        }

        if ( isCharacterOrString(object) ){
            return "\"" +
                    object.toString() +
                    "\"";
        }

        if(isCollection(object)){
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            Object[] arrayObject= ((Collection) object).toArray();
            for (int j = 0; j < arrayObject.length; j++){
                jsonBuilder.append(getParseObject(arrayObject[j]));
                if (j < arrayObject.length-1){
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
            return jsonBuilder.toString();
        }

        return getBasicParseObject(object);
    }

    private String getBasicParseObject(Object object) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            jsonBuilder.append("\"");
            jsonBuilder.append(fields[i].getName());
            jsonBuilder.append("\":");
            fields[i].setAccessible(true);
            Object objField = new Object();
            try {
                objField = fields[i].get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            jsonBuilder.append(getParseObject(objField));
            if (i < fields.length-1){
                jsonBuilder.append(",");
            }
            fields[i].setAccessible(false);
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    boolean isPrimitive(Object o) {
        Class<?> aClass = o.getClass();
        return  aClass.equals(Double.class) ||
                aClass.equals(Float.class) ||
                aClass.equals(Long.class) ||
                aClass.equals(Integer.class) ||
                aClass.equals(Short.class) ||
                aClass.equals(Byte.class) ||
                aClass.equals(Boolean.class);
    }

    boolean isCharacterOrString(Object o) {
        Class<?> aClass = o.getClass();
        return aClass.equals(Character.class) ||
                aClass.equals(String.class);
    }

    boolean isCollection(Object o) {
        Class<?> aClass = o.getClass();
        return Collection.class.isAssignableFrom(aClass);
    }
}
