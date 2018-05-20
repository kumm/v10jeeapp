package com.acme.v10jeeapp.app.security;

import com.acme.v10jeeapp.app.ui.LoginView;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.wcs.vaadin.flow.cdi.RouteScopeOwner;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AnnotationsAuthorizingMethodInterceptor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@ApplicationScoped
@Typed
public class NavigationInterceptor extends AnnotationsAuthorizingMethodInterceptor {

    @Inject
    @RouteScopeOwner(LoginView.class)
    private LoginView.AfterLoginNavigation afterLoginNavigation;

    @PostConstruct
    private void init() {
        NavigationTargetAnnotationResolver annotationResolver
                = new NavigationTargetAnnotationResolver();
        getMethodInterceptors().forEach(interceptor
                -> interceptor.setResolver(annotationResolver));
    }

    private void onBeforeLeave(@Observes BeforeLeaveEvent event) {
        NavigationTargetInvocation mi = new NavigationTargetInvocation(
                event.getNavigationTarget());
        try {
            assertAuthorized(mi);
        } catch (UnauthenticatedException e) {
            afterLoginNavigation.setLocation(event.getLocation());
            event.rerouteTo(LoginView.class);
        }
    }

    private static class NavigationTargetInvocation implements MethodInvocation {
        private final Class<?> target;

        NavigationTargetInvocation(Class<?> target) {
            this.target = target;
        }

        public Object proceed() {
            return null;
        }

        public Method getMethod() {
            return null;
        }

        public Object[] getArguments() {
            return null;
        }

        public Object getThis() {
            return null;
        }

        Class<?> getTarget() {
            return target;
        }
    }

    private static class NavigationTargetAnnotationResolver implements AnnotationResolver {
        @Override
        public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
            Class<?> target = ((NavigationTargetInvocation) mi).getTarget();
            return target.getAnnotation(clazz);
        }
    }

}
