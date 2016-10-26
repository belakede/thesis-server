package me.belakede.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * http://stackoverflow.com/a/3623660
 */
public class OrderedSpringRunner extends SpringJUnit4ClassRunner {

    public OrderedSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return super.computeTestMethods().stream().sorted(new OrderComparator()).collect(Collectors.toList());
    }

    private static final class OrderComparator implements Comparator<FrameworkMethod> {
        @Override
        public int compare(FrameworkMethod method1, FrameworkMethod method2) {
            int result = -1;
            Order o1 = method1.getAnnotation(Order.class);
            Order o2 = method2.getAnnotation(Order.class);
            if (o1 != null && o2 != null) {
                result = o1.value() - o2.value();
            }
            return result;
        }
    }

}
