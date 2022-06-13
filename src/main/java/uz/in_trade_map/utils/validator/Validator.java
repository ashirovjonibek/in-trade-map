package uz.in_trade_map.utils.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uz.in_trade_map.utils.validator.annotations.Email;
import uz.in_trade_map.utils.validator.annotations.FieldTypeArray;
import uz.in_trade_map.utils.validator.annotations.FieldTypeFile;
import uz.in_trade_map.utils.validator.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
            FieldTypeFile fieldTypeFile = field.getAnnotation(FieldTypeFile.class);
            FieldTypeArray fieldTypeArray = field.getAnnotation(FieldTypeArray.class);
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
                if (fieldTypeFile != null && fieldTypeArray == null) {
                    if (o != null) {
                        MultipartFile o1 = null;
                        if (o instanceof MultipartFile) {
                            o1 = (MultipartFile) o;
                            String filename = o1.getOriginalFilename();
                            if (filename != null&&!filename.equals("")) {
                                String ext = filename.substring(filename.lastIndexOf("."));
                                if (!fieldTypeFile.extension().equals("*")
                                        && ext.length() > 0
                                        && !fieldTypeFile.extension().contains(ext.toLowerCase())) {
                                    err.add("The file type must be " + fieldTypeFile.extension());
                                }
                                if (fieldTypeFile.size() != -1 && fieldTypeFile.size() < o1.getSize()) {
                                    err.add("The file size must be less than " + fieldTypeFile.size() + " bytes");
                                }
                            }
                        } else
                            err.add("Field type not containing file!");
                    }
                }
                if (fieldTypeFile == null && fieldTypeArray != null) {
                    if (o != null) {
                        if (o instanceof Arrays) {
                            List<Object> objects = Arrays.asList(o);
                            if (fieldTypeArray.maxLength() != -1 && objects.size() > fieldTypeArray.maxLength()) {
                                err.add("The length of the list should not exceed " + fieldTypeArray.maxLength());
                            }
                            if (fieldTypeArray.minLength() != -1 && objects.size() < fieldTypeArray.minLength()) {
                                err.add("The length of the list should not be less than " + fieldTypeArray.minLength());
                            }
                        }
                    }
                }

                if (fieldTypeFile != null && fieldTypeArray != null) {
                    if (o != null) {
                        if (o instanceof MultipartFile[]) {
                            MultipartFile[] multipartFiles = (MultipartFile[]) o;
                            if (fieldTypeArray.maxLength() != -1 && multipartFiles.length > fieldTypeArray.maxLength()) {
                                err.add("The length of the list should not exceed " + fieldTypeArray.maxLength());
                            } else if (fieldTypeArray.maxLength() != -1 && multipartFiles.length <= fieldTypeArray.maxLength()) {
                                List<List<String>> collect = Arrays.stream(multipartFiles).map(object -> {
                                    List<String> typeErr = new ArrayList<>();
                                    if (object != null) {
                                        MultipartFile o1 = null;
                                        o1 = (MultipartFile) object;
                                        String filename = o1.getOriginalFilename();
                                        assert filename != null;
                                        String ext = filename.substring(filename.lastIndexOf(".") + 1);
                                        if (!fieldTypeFile.extension().equals("*")
                                                && ext.length() > 0
                                                && !fieldTypeFile.extension().contains(ext.toLowerCase())) {
                                            typeErr.add("The file type must be " + fieldTypeFile.extension());
                                        }
                                        if (fieldTypeFile.size() != -1 && fieldTypeFile.size() < o1.getSize()) {
                                            typeErr.add("The file size must be less than " + fieldTypeFile.size() + " bytes");
                                        }
                                    }
                                    return typeErr;
                                }).collect(Collectors.toList());
                                List<List<String>> listList = collect.stream().filter(strings -> strings.size() > 0).collect(Collectors.toList());
                                listList.forEach(err::addAll);
                            } else if (fieldTypeArray.maxLength() == -1) {
                                List<List<String>> collect = Arrays.stream(multipartFiles).map(object -> {
                                    List<String> typeErr = new ArrayList<>();
                                    if (object != null) {
                                        MultipartFile o1 = null;
                                        o1 = (MultipartFile) object;
                                        String filename = o1.getOriginalFilename();
                                        assert filename != null;
                                        String[] split = filename.split(".");
                                        if (!fieldTypeFile.extension().equals("*")
                                                && split.length > 0
                                                && !fieldTypeFile.extension().contains(split[split.length - 1])) {
                                            typeErr.add("The file type must be " + fieldTypeFile.extension());
                                        }
                                        if (fieldTypeFile.size() != -1 && fieldTypeFile.size() < o1.getSize()) {
                                            typeErr.add("The file size must be less than " + fieldTypeFile.size() + " bytes");
                                        }
                                    }
                                    return typeErr;
                                }).collect(Collectors.toList());
                                List<List<String>> listList = collect.stream().filter(strings -> strings.size() > 0).collect(Collectors.toList());
                                listList.forEach(err::addAll);
                            }
                            if (fieldTypeArray.minLength() != -1 && multipartFiles.length < fieldTypeArray.minLength()) {
                                err.add("The length of the list should not be less than " + fieldTypeArray.minLength());
                            } else if (fieldTypeArray.minLength() != -1 && multipartFiles.length >= fieldTypeArray.minLength()) {
                                List<List<String>> collect = Arrays.stream(multipartFiles).map(object -> {
                                    List<String> typeErr = new ArrayList<>();
                                    if (object != null) {
                                        MultipartFile o1 = null;
                                        o1 = (MultipartFile) object;
                                        String filename = o1.getOriginalFilename();
                                        assert filename != null;
                                        String[] split = filename.split(".");
                                        if (!fieldTypeFile.extension().equals("*")
                                                && split.length > 0
                                                && !fieldTypeFile.extension().contains(split[split.length - 1])) {
                                            typeErr.add("The file type must be " + fieldTypeFile.extension());
                                        }
                                        if (fieldTypeFile.size() != -1 && fieldTypeFile.size() < o1.getSize()) {
                                            typeErr.add("The file size must be less than " + fieldTypeFile.size() + " bytes");
                                        }
                                    }
                                    return typeErr;
                                }).collect(Collectors.toList());
                                List<List<String>> listList = collect.stream().filter(strings -> strings.size() > 0).collect(Collectors.toList());
                                listList.forEach(err::addAll);
                            } else if (fieldTypeArray.minLength() == -1) {
                                List<List<String>> collect = Arrays.stream(multipartFiles).map(object -> {
                                    List<String> typeErr = new ArrayList<>();
                                    if (object != null) {
                                        MultipartFile o1 = null;
                                        o1 = (MultipartFile) object;
                                        String filename = o1.getOriginalFilename();
                                        assert filename != null;
                                        String[] split = filename.split(".");
                                        if (!fieldTypeFile.extension().equals("*")
                                                && split.length > 0
                                                && !fieldTypeFile.extension().contains(split[split.length - 1])) {
                                            typeErr.add("The file type must be " + fieldTypeFile.extension());
                                        }
                                        if (fieldTypeFile.size() != -1 && fieldTypeFile.size() < o1.getSize()) {
                                            typeErr.add("The file size must be less than " + fieldTypeFile.size() + " bytes");
                                        }
                                    }
                                    return typeErr;
                                }).collect(Collectors.toList());
                                List<List<String>> listList = collect.stream().filter(strings -> strings.size() > 0).collect(Collectors.toList());
                                listList.forEach(err::addAll);
                            }
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
