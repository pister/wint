package wint.help.tools.gen.common;

import wint.help.tools.gen.dao.DaoGenUtil;
import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.StringUtil;

/**
 * User: longyi
 * Date: 13-9-21
 * Time: 上午7:33
 */
public class GenMetaInfo {

    private Class<?> targetDoClass;

    private String idName;

    private String doAlias;

    private String doPackage;

    private String doSimpleClassName;

    private String doFullClassName;

    private String baseDalPackage;

    private String idType;

    private String daoSimpleClassName;

    private String daoFullClassName;

    private String daoPackage;

    private String ibatisPackage;

    private String ibatisSimpleClassName;

    private String ibatisFullClassName;

    private String idTypeWrapper;

    private String testDaoSimpleClassName;

    private String testDaoPackage;

    private String testDaoFullClassName;

    private String implSuffix = "Ibatis";

    private Class<?> idClass;

    public GenMetaInfo(Class<?> targetDoClass, String idName) {
        this.targetDoClass = targetDoClass;
        this.idName = idName;
        if (StringUtil.isEmpty(idName)) {
            this.idName = "id";
        }
        init(targetDoClass);
    }

    public String getBaseBizPackage() {
        return StringUtil.getLastBefore(baseDalPackage, ".dal");
    }

    private void init(Class<?> targetDoClass) {
        doSimpleClassName = ClassUtil.getShortClassName(targetDoClass);
        doAlias = DaoGenUtil.getDoAlias(targetDoClass);
        daoSimpleClassName = StringUtil.uppercaseFirstLetter(doAlias) + "DAO";

        MagicClass magicClass = MagicClass.wrap(targetDoClass);
        Property idProperty = magicClass.getProperty(idName);
        if (idProperty == null) {
            throw new RuntimeException("the id " + idName + " from " + targetDoClass + " not exist!");
        }
        idClass = idProperty.getPropertyClass().getTargetClass();
        idType = ClassUtil.getShortClassName(idClass);
        idTypeWrapper = ClassUtil.getShortClassName(ClassUtil.getWrapperClass(idClass));

        doPackage = targetDoClass.getPackage().getName();
        doFullClassName = targetDoClass.getName();
        baseDalPackage = StringUtil.getLastBefore(doPackage, ".domain");

        daoPackage = baseDalPackage + ".dao";
        daoFullClassName = daoPackage + "." + daoSimpleClassName;

        ibatisPackage = daoPackage + ".ibatis";
        ibatisSimpleClassName = daoSimpleClassName + implSuffix;
        ibatisFullClassName = ibatisPackage + "."  + ibatisSimpleClassName;

        testDaoSimpleClassName = daoSimpleClassName +"Tests";
        testDaoPackage = daoPackage;
        testDaoFullClassName = testDaoPackage + "." + testDaoSimpleClassName;

    }

    public String getBaseDalPackage() {
        return baseDalPackage;
    }

    public String getDaoFullClassName() {
        return daoFullClassName;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public String getDaoSimpleClassName() {
        return daoSimpleClassName;
    }

    public String getDoAlias() {
        return doAlias;
    }

    public String getDoFullClassName() {
        return doFullClassName;
    }

    public String getDoPackage() {
        return doPackage;
    }

    public String getDoSimpleClassName() {
        return doSimpleClassName;
    }

    public String getIbatisPackage() {
        return ibatisPackage;
    }

    public String getIbatisSimpleClassName() {
        return ibatisSimpleClassName;
    }

    public String getIbatisFullClassName() {
        return ibatisFullClassName;
    }

    public String getIdName() {
        return idName;
    }

    public String getIdType() {
        return idType;
    }

    public Class<?> getTargetDoClass() {
        return targetDoClass;
    }

    public Class<?> getIdClass() {
        return idClass;
    }

    public String getIdTypeWrapper() {
        return idTypeWrapper;
    }

    public String getTestDaoFullClassName() {
        return testDaoFullClassName;
    }

    public String getTestDaoPackage() {
        return testDaoPackage;
    }

    public String getTestDaoSimpleClassName() {
        return testDaoSimpleClassName;
    }

    public String getImplSuffix() {
        return implSuffix;
    }

    public void setImplSuffix(String implSuffix) {
        this.implSuffix = implSuffix;
    }
}
