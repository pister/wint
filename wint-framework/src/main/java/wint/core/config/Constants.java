package wint.core.config;

import wint.lang.utils.SystemUtil;
import wint.sessionx.constants.DefaultSupportTypes;

public interface Constants {

    interface PropertyKeys {

        String USER_DEFINE_WINT_FILE = "wint.app.user.define.wint.file";

        String APP_ENV = "wint.app.env";

        String APP_PACKAGE = "wint.app.package";

        String APP_MODULE_METHOD = "wint.app.module.method";

        String APP_PACKAGE_WEB_ACTION = "wint.app.web.action";

        String APP_PACKAGE_WEB_WIDGET = "wint.app.web.widget";

        String APP_PACKAGE_BIZ_DOMAIN = "wint.app.biz.domain";

        String APP_PACKAGE_BIZ_AO = "wint.app.biz.ao";

        String APP_PACKAGE_BIZ_MANAGER = "wint.app.biz.manager";

        String APP_PACKAGE_BIZ_DAO = "wint.app.biz.dao";

        String APP_PACKAGE_BIZ_MISC = "wint.app.biz.misc";

        String APP_I18N = "wint.app.i18n";

        String TEMPLATE_PATH = "wint.app.template.path";

        String TEMPLATE_LAYOUT = "wint.app.template.layout";

        String TEMPLATE_PAGE = "wint.app.template.page";

        String TEMPLATE_WIDGET = "wint.app.template.widget";

        String DEFAULT_LAYOUT_FILE = "wint.app.template.default.layout.file";

        String PAGE_CONTENT_NAME = "wint.app.template.page.content";

        String WIDGET_CONTAINER_NAME = "wint.app.template.widget.container";

        String WIDGET_OUTER_CONTAINER_NAME = "wint.app.template.outer.widget.container";

        String ERROR_PAGE_DEFAULT = "wint.app.error.page.default";

        String CHARSET_ENCODING = "wint.app.charset.encoding";

        String NO_FILTER_NAMES_FILE = "wint.app.template.no.filter.names.file";

        String URL_SUFFIX = "wint.app.url.suffix";

        String URL_ARGUMENT_SEPARATOR = "wint.app.url.argument.separator";

        String URL_REWRITE_ARGUMENT_SEPARATOR = "wint.app.url.rewrite.argument.separator";

        String CSRF_TOKEN_NAME = "wint.app.csrf.token.name";

        String URL_CONFIG_FILE = "wint.app.url.config.file";

        String SPRING_CONTEXT_FILE = "wint.app.spring.context.file";

        String OBJECT_MAGIC_TYPE = "wint.app.object.magic.type";

        String PARAMS_NAME = "wint.app.params.name";

        String ARGS_NAME = "wint.app.args.name";

        String TARGET_NAME = "wint.app.target.name";

        String UPLOAD_FILE_MAX_SIZE = "wint.app.upload.max.size";

        String SQLMAP_AUTO_LOAD = "wint.app.sqlmap.dev.autoload";

        String SQLMAP_SHOW_SQL = "wint.app.sqlmap.show.sql";

        String SQLMAP_SHOW_LOG_NAME = "wint.app.sqlmap.show.log.name";

        String WINT_SESSION_USE = "wint.session.use";

        String WINT_I18N_VAR_NAME = "wint.i18n.var.name";

        String URL_PATH_AS_TARGET_NAME = "wint.url.path.as.default.name";

        String FILE_FOR_DIR_TARGET = "wint.file.for.dir.target";

        String WINT_SESSION_EXPIRE = "wint.session.expire";

        String WINT_SESSION_TYPE = "wint.session.type";

        String WINT_SESSION_IGNORE_PATHS = "wint.session.ignore.paths";

        String TEMPLATE_MODIFICATION_CHECK_INTERVAL = "wint.templates.modification.check.interval";

        String WINT_DEBUG_VIEW_SUPPORT = "wint.debug.view.support";

        String WINT_DEBUG_VIEW_PARAM_NAME = "wint.debug.view.param.name";

        String MOCK_DATA_PREFIX = "wint.mock.data.name";

        String WINT_FORCE_MOCK = "wint.mock.force";

        String WINT_JSON_ROOT = "wint.json.root.name";

        String WINT_REQUEST_CONTEXT_PATH = "wint.request.context.path";

        String WINT_OUTER_TEMPLATE_PATH = "wint.outer.template.path";

        String WINT_OUTER_TEMPLATE_TEMP_PATH = "wint.outer.template.temp.path";

        String WINT_OUTER_TEMPLATE_EXPIRE_SECONDS = "wint.outer.template.expire.seconds";

        String WINT_OUTER_TEMPLATE_REMOTE_FAIL_USER_CACHE = "wint.outer.template.remote.fail.use.cache";

        String WINT_ENVIRONMENT_VAR_NAME = "wint.environment.var.name";

        String WINT_JSON_VIEW_FORM_VALIDATE_SUCCESS_FIELD = "wint.json.view.form.validate.success.field";

        String WINT_JSON_VIEW_FORM_VALIDATE_MESSAGES_FIELD = "wint.json.view.form.validate.messages.field";


    }

    interface FlowDataAttributeKeys {

        String INDEXED_PARAMETERS = "wint.flowdata.indexed.parameters";

    }

    interface Defaults {

        String APP_I18N = "i18n";

        String APP_PACKAGE_WEB_ACTION = "web.action";

        String APP_PACKAGE_WEB_WIDGET = "web.widget";

        String APP_PACKAGE_BIZ_DOMAIN = "biz.domain";

        String APP_PACKAGE_BIZ_AO = "biz.ao";

        String APP_PACKAGE_BIZ_DAO = "biz.dao";

        String APP_PACKAGE_BIZ_MANAGER = "biz.manager";

        String APP_PACKAGE_BIZ_MISC = "biz.misc";

        String NO_FILTER_NAMES_FILE = "no-filter-names";

        String WIDGET_CONTAINER_NAME = "widget";

        String WIDGET_OUTER_CONTAINER_NAME = "outerWidget";

        String PAGE_CONTENT_NAME = "page_content";

        String TEMPLATE_PATH = "templates";

        String TEMPLATE_LAYOUT = "layout";

        String TEMPLATE_PAGE = "page";

        String TEMPLATE_WIDGET = "widget";

        String DEFAULT_LAYOUT_FILE = "default";

        String APP_PACKAGE = "wint.app";

        String SYS_WINT_FILE = "default-wint.xml";

        String USER_DEFINE_WINT_FILE = "wint.xml";

        String PIPELINE_NAME = "default";

        String MODULE_METHOD = "execute";

        String CHARSET_ENCODING = "utf-8";

        String ERROR_PAGE_DEFAULT = "default-error-pages/default";

        String INDEX_TARGET = "index";

        int TEMPLATE_MODIFICATION_CHECK_INTERVAL = 10 * 60;

        String URL_SUFFIX = ".htm";

        String URL_ARGUMENT_SEPARATOR = "-";

        String URL_CONFIG_FILE = "url.xml";

        String SPRING_CONTEXT_FILE = "applicationContext.xml";

        String OBJECT_MAGIC_TYPE = "java";

        String PARAMS_NAME = "params";

        String ARGS_NAME = "args";

        String TARGET_NAME = "target";

        String CSRF_TOKEN_NAME = "x_token";

        long UPLOAD_FILE_MAX_SIZE = 20 * 1024 * 1024;

        boolean SQLMAP_AUTO_LOAD = true;

        boolean WINT_SESSION_USE = false;

        String WINT_I18N_VAR_NAME = "i18n";

        String URL_PATH_AS_TARGET_NAME = ".";

        String FILE_FOR_DIR_TARGET = "default";

        int WINT_SESSION_EXPIRE = 30 * 60;

        boolean WINT_DEBUG_VIEW_SUPPORT = true;

        String WINT_DEBUG_VIEW_PARAM_NAME = "_debug_view";

        String MOCK_DATA_PREFIX = "wint_mock";

        String WINT_FORCE_MOCK = "_force_mock";

        String WINT_JSON_ROOT = "%json_root%";

        String WINT_REQUEST_CONTEXT_PATH = "";

        String WINT_SESSION_TYPE = DefaultSupportTypes.COOKIE;

        String WINT_OUTER_TEMPLATE_PATH = SystemUtil.getUserHome() + "/templates";

        String WINT_OUTER_TEMPLATE_TEMP_PATH = SystemUtil.getUserHome() + "/wint_temp_templates";

        int WINT_OUTER_TEMPLATE_EXPIRE_SECONDS = 3 * 60;

        boolean WINT_OUTER_TEMPLATE_REMOTE_FAIL_USER_CACHE = true;

        String WINT_ENVIRONMENT_VAR_NAME = "env";

        String WINT_JSON_VIEW_FORM_VALIDATE_SUCCESS_FIELD = "success";

        String WINT_JSON_VIEW_FORM_VALIDATE_MESSAGES_FIELD = "messages";

        String SQLMAP_SHOW_LOG_NAME = "wint.sqlmap.sql";


    }

    interface Form {

        String FORM_CONFIG_FILE = "form.xml";

        String VAR_NAME_FORM = "form";

        String VAR_NAME_FIELD = "field";

        String TEMPLATE_FORM_FACTORY_NAME = "formFactory";

        String LAST_FORM_NAME = "last_form_name";

    }

    interface Url {
        int MAX_ARGUMENT_COUNT = 256;
    }

}
