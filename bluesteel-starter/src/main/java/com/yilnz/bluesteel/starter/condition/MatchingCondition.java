package com.yilnz.bluesteel.starter.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MatchingCondition  implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        System.out.println(annotatedTypeMetadata.getAnnotationAttributes("Bean").get("name").toString());
        return true;
    }
}
