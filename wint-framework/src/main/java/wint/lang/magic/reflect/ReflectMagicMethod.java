package wint.lang.magic.reflect;

import wint.lang.exceptions.MethodInvokeException;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicList;
import wint.lang.magic.MagicMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 通过反射实现的方法
 *
 * @author pister
 */
public class ReflectMagicMethod extends MagicMethod {

    public ReflectMagicMethod(Method method) {
        super(method);
    }

    @Override
    public Object invoke(final Object object, final Object[] arguments) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                targetMethod.setAccessible(true);
                try {
                    return targetMethod.invoke(object, arguments);
                } catch (IllegalAccessException e) {
                    throw new MethodInvokeException(e);
                } catch (InvocationTargetException e) {
                    throw new MethodInvokeException(e.getTargetException());
                }
            }
        });
    }

    public MagicList<MagicClass> getParameterTypes() {
        Class<?>[] types = targetMethod.getParameterTypes();
        MagicList<MagicClass> ret = MagicList.newList();
        for (Class<?> t : types) {
            ret.add(new ReflectMagicClass(t));
        }
        return ret;
    }

    @Override
    public String toString() {
        return "ReflectMagicMethod [" + targetMethod + "]";
    }

}