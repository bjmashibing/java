/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.util.concurrent;

/**
 * Callback mechanism for the outcome, success or failure, from a
 * {@link ListenableFuture}.
 *
 * @author Arjen Poutsma
 * @author Sebastien Deleuze
 * @since 4.0
 * @param <T> the result type
 */
public interface ListenableFutureCallback<T> extends SuccessCallback<T>, FailureCallback {

}
