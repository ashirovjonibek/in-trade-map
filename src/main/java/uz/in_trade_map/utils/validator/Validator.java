package uz.in_trade_map.utils.validator;

import org.springframework.stereotype.Component;
import uz.in_trade_map.utils.validator.annotations.Email;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class Validator<T> {
    public Map<String, Object> valid(T t) {
        Map<String, Object> errors = new HashMap<>();
        Class<?> tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        Arrays.asList(fields).forEach(field -> {
            List<String> err = new ArrayList<>();
            NotNull notNull = field.getAnnotation(NotNull.class);
            Email email = field.getAnnotation(Email.class);
            field.setAccessible(true);
            try {
                Object o = field.get(t);
                if (notNull != null) {
                    if (o == null) {
                        err.add(notNull.message());
                    } else if (notNull.minLength() != -1) {
                        String val = (String) o;
                        if (val.length() < notNull.minLength()) {
                            err.add(field.getName() + " value must be at least " + notNull.minLength() + " characters");
                        }
                    } else if (notNull.maxLength() != -1) {
                        String val = (String) o;
                        if (val.length() > notNull.maxLength()) {
                            err.add("The " + field.getName() + " value should not exceed " + notNull.maxLength() + " characters");
                        }
                    }
                }
                if (email != null) {
                    String val = (String) o;
                    if (val != null) {
                        if (!Objects.requireNonNull(val).contains("@")) {
                            err.add(email.message());
                        }
                    }
                }
                if (err.size() > 0) {
                    errors.put(field.getName(), err);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return errors;
    }
}
