package wint.core.service.bean.mock;

import wint.core.service.bean.BeanFactory;
import wint.core.service.supports.AbstractBeanFactory;
import wint.help.biz.result.Result;
import wint.help.mock.MockResultUtil;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.CollectionUtil;
import wint.mvc.holder.WintContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

/**
 * User: longyi
 * Date: 14-3-31
 * Time: 上午6:54
 */
public class MockBeanFactory extends AbstractBeanFactory implements BeanFactory {

    public Object getObject(String name) {
        return null;
    }

    private Object getObjectAndType(String name, MagicClass type) {
        return Proxy.newProxyInstance(type.getTargetClass().getClassLoader(), new Class[]{type.getTargetClass()}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Result.class.isAssignableFrom(method.getReturnType())) {
                    return MockResultUtil.loadMockResult(WintContext.getFlowData());
                }
                return null;
            }
        });
    }

    public Set<Property> injectProperties(Object target) {
        if (target == null) {
            return null;
        }
        MagicObject magicObject = MagicObject.wrap(target);
        Set<Property> ret = CollectionUtil.newHashSet();
        for (Map.Entry<String, Property> entry : magicObject.getMagicClass().getProperties().entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();
            if (property.isWritable()) {
                Object obj = getObjectAndType(name, property.getPropertyClass());
                if (obj != null) {
                    property.setValue(target, obj);
                    ret.add(property);
                }

            }
        }
        return ret;
    }


}
