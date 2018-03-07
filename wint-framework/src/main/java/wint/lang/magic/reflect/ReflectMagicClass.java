package wint.lang.magic.reflect;

import wint.lang.exceptions.CanNotFindMethodException;
import wint.lang.exceptions.CanNotInstanceException;
import wint.lang.magic.*;
import wint.lang.magic.compatible.AutoAdaptMagicMethod;
import wint.lang.utils.ArrayUtil;
import wint.lang.utils.CollectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;

public class ReflectMagicClass extends MagicClass {

    private static final long serialVersionUID = 3729825268816208769L;

    public ReflectMagicClass(Class<?> targetClass) {
        super(targetClass);
    }

    @Override
    public Map<String, Property> getProperties() {
        return findPropertiesFromClass();
    }

    @Override
    public MagicObject newInstance() {
        return newInstance(ArrayUtil.EMPTY_CLASS_ARRAY, null);
    }

    @Override
    public MagicObject newInstance(final Class<?>[] parameterTypes, final Object[] constructorArguments) {
        return AccessController.doPrivileged(new PrivilegedAction<MagicObject>() {
            @Override
            public MagicObject run() {
                try {
                    Constructor<?> constructor = targetClass.getConstructor(parameterTypes);
                    return new ReflectMagicObject(constructor.newInstance(constructorArguments));
                } catch (NoSuchMethodException e) {
                    throw new CanNotInstanceException(e);
                } catch (IllegalArgumentException e) {
                    throw new CanNotInstanceException(e);
                } catch (InstantiationException e) {
                    throw new CanNotInstanceException(e);
                } catch (IllegalAccessException e) {
                    throw new CanNotInstanceException(e);
                } catch (InvocationTargetException e) {
                    throw new CanNotInstanceException(e.getTargetException());
                }
            }
        });
    }

    @Override
    public MagicMethod getMethod(String methodName) {
        Method method = getMethodByName(methodName);
        if (method != null) {
            return new AutoAdaptMagicMethod(new ReflectMagicMethod(method));
        } else {
            return null;
        }
    }

    @Override
    public List<MagicMethod> getMethods(String methodName) {
        List<Method> methods = getMethodsByName(methodName);
        return CollectionUtil.transformList(methods, new Transformer<Method, MagicMethod>() {
            @Override
            public MagicMethod transform(Method method) {
                return new AutoAdaptMagicMethod(new ReflectMagicMethod(method));
            }
        });
    }

    @Override
    public MagicMethod getMethod(final String methodName, final Class<?>[] argumentTypes) {
        return AccessController.doPrivileged(new PrivilegedAction<MagicMethod>() {
            @Override
            public MagicMethod run() {
                try {
                    Method method = targetClass.getMethod(methodName, argumentTypes);
                    return new ReflectMagicMethod(method);
                } catch (NoSuchMethodException e) {
                    throw new CanNotFindMethodException(e);
                }
            }
        });
    }


}
