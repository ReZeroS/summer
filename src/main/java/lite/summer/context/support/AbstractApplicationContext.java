package lite.summer.context.support;

import lite.summer.beans.factory.annotation.AutowiredAnnotationProcessor;
import lite.summer.beans.factory.config.AutowireCapableBeanFactory;
import lite.summer.beans.factory.config.ConfigurableBeanFactory;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.factory.xml.XmlBeanDefinitionReader;
import lite.summer.context.ApplicationContext;
import lite.summer.core.io.Resource;
import lite.summer.util.ClassUtils;

/**
 * @Author: ReZero
 * @Date: 3/18/19 9:40 AM
 * @Version 1.0
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private ClassLoader classLoader;

    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String configFile){
        this(configFile, ClassUtils.getDefaultClassLoader());
    }

    public AbstractApplicationContext(String configFile, ClassLoader classLoader){
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = this.getResourceByPath(configFile);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(classLoader);
        registerBeanPostProcessors(factory);
    }

    protected abstract Resource getResourceByPath(String path);

    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory configurableBeanFactory){
        AutowiredAnnotationProcessor autowiredAnnotationProcessor = new AutowiredAnnotationProcessor();
        autowiredAnnotationProcessor.setBeanFactory(configurableBeanFactory);
        configurableBeanFactory.addBeanPostProcessor(autowiredAnnotationProcessor);
    }


}
