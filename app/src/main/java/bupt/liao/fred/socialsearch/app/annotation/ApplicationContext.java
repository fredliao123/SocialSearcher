package bupt.liao.fred.socialsearch.app.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Fred.Liao on 2017/12/6.
 * Email:fredliaobupt@qq.com
 * Description: A scope that describe Application context
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
