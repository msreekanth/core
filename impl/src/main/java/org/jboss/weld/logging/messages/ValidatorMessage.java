/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.weld.logging.messages;

import org.jboss.weld.logging.MessageId;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("org.jboss.weld.messages.validator")
@LocaleData({
   @Locale("en")
})
/**
 * Log messages for validation related classes.
 * 
 * Message IDs: 001400 - 001499
 * 
 * @author David Allen
 *
 */
public enum ValidatorMessage
{
   @MessageId("001401") BEAN_SPECIALIZED_TOO_MANY_TIMES,
   @MessageId("001402") PASSIVATING_BEAN_WITH_NONSERIALIZABLE_INTERCEPTOR,
   @MessageId("001403") PASSIVATING_BEAN_WITH_NONSERIALIZABLE_DECORATOR,
   @MessageId("001404") NEW_WITH_QUALIFIERS,
   @MessageId("001405") INJECTION_INTO_NON_BEAN,
   @MessageId("001406") INJECTION_INTO_NON_DEPENDENT_BEAN,
   @MessageId("001407") INJECTION_POINT_WITH_TYPE_VARIABLE,
   @MessageId("001408") INJECTION_POINT_HAS_UNSATISFIED_DEPENDENCIES,
   @MessageId("001409") INJECTION_POINT_HAS_AMBIGUOUS_DEPENDENCIES,
   @MessageId("001410") INJECTION_POINT_HAS_NON_PROXYABLE_DEPENDENCIES,
   @MessageId("001411") INJECTION_POINT_HAS_NULLABLE_DEPENDENCIES,
   @MessageId("001412") NON_SERIALIZABLE_BEAN_INJECTED_INTO_PASSIVATING_BEAN,
   @MessageId("001413") INJECTION_POINT_HAS_NON_SERIALIZABLE_DEPENDENCY,
   @MessageId("001414") AMBIGUOUS_EL_NAME,
   @MessageId("001415") BEAN_NAME_IS_PREFIX,
   @MessageId("001416") INTERCEPTOR_SPECIFIED_TWICE,
   @MessageId("001417") INTERCEPTOR_NOT_ANNOTATED_OR_REGISTERED,
   @MessageId("001418") DECORATOR_SPECIFIED_TWICE,
   @MessageId("001419") DECORATOR_CLASS_NOT_BEAN_CLASS_OF_DECORATOR,
   @MessageId("001420") ALTERNATIVE_STEREOTYPE_NOT_STEREOTYPE,
   @MessageId("001421") ALTERNATIVE_STEREOTYPE_SPECIFIED_MULTIPLE_TIMES,
   @MessageId("001422") ALTERNATIVE_BEAN_CLASS_NOT_ANNOTATED,
   @MessageId("001423") ALTERNATIVE_BEAN_CLASS_SPECIFIED_MULTIPLE_TIMES,
   @MessageId("001424") DISPOSAL_METHODS_WITHOUT_PRODUCER,
   @MessageId("001425") INJECTION_POINT_HAS_WILDCARD,
   @MessageId("001426") INJECTION_POINT_MUST_HAVE_TYPE_PARAMETER,
   @MessageId("001427") NON_FIELD_INJECTION_POINT_CANNOT_USE_NAMED,
   @MessageId("001428") DECORATORS_CANNOT_HAVE_PRODUCER_METHODS,
   @MessageId("001429") DECORATORS_CANNOT_HAVE_PRODUCER_FIELDS,
   @MessageId("001430") DECORATORS_CANNOT_HAVE_DISPOSER_METHODS,
   @MessageId("001431") INTERCEPTORS_CANNOT_HAVE_PRODUCER_METHODS,
   @MessageId("001432") INTERCEPTORS_CANNOT_HAVE_PRODUCER_FIELDS,
   @MessageId("001433") INTERCEPTORS_CANNOT_HAVE_DISPOSER_METHODS,
   @MessageId("001434") NOT_PROXYABLE_UNKNOWN,
   @MessageId("001435") NOT_PROXYABLE_NO_CONSTRUCTOR,
   @MessageId("001436") NOT_PROXYABLE_PRIVATE_CONSTRUCTOR,
   @MessageId("001437") NOT_PROXYABLE_FINAL_TYPE_OR_METHOD,
   @MessageId("001438") NOT_PROXYABLE_PRIMITIVE,
   @MessageId("001439") NOT_PROXYABLE_ARRAY_TYPE,
   @MessageId("001440") SCOPE_ANNOTATION_ON_INJECTION_POINT,
   @MessageId("001441") ALTERNATIVE_BEAN_CLASS_NOT_CLASS,
   @MessageId("001442") ALTERNATIVE_STEREOTYPE_NOT_ANNOTATED,
   @MessageId("001443") PSEUDO_SCOPED_BEAN_HAS_CIRCULAR_REFERENCES;
}
