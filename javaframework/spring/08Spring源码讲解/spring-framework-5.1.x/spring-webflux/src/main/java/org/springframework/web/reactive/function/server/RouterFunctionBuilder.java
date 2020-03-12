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

package org.springframework.web.reactive.function.server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link RouterFunctions.Builder}.
 *
 * @author Arjen Poutsma
 * @since 5.1
 */
class RouterFunctionBuilder implements RouterFunctions.Builder {

	private List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList<>();

	private List<HandlerFilterFunction<ServerResponse, ServerResponse>> filterFunctions = new ArrayList<>();


	@Override
	public RouterFunctions.Builder add(RouterFunction<ServerResponse> routerFunction) {
		Assert.notNull(routerFunction, "RouterFunction must not be null");
		this.routerFunctions.add(routerFunction);
		return this;
	}

	private RouterFunctions.Builder add(RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		this.routerFunctions.add(RouterFunctions.route(predicate, handlerFunction));
		return this;
	}

	@Override
	public RouterFunctions.Builder GET(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.GET(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder GET(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.GET(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder HEAD(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.HEAD(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder HEAD(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.HEAD(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder POST(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.POST(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder POST(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.POST(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder PUT(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.PUT(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder PUT(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.PUT(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder PATCH(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.PATCH(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder PATCH(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.PATCH(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder DELETE(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.DELETE(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder DELETE(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.DELETE(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder OPTIONS(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
		return add(RequestPredicates.OPTIONS(pattern), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder OPTIONS(String pattern, RequestPredicate predicate,
			HandlerFunction<ServerResponse> handlerFunction) {

		return add(RequestPredicates.OPTIONS(pattern).and(predicate), handlerFunction);
	}

	@Override
	public RouterFunctions.Builder resources(String pattern, Resource location) {
		return add(RouterFunctions.resources(pattern, location));
	}

	@Override
	public RouterFunctions.Builder resources(Function<ServerRequest, Mono<Resource>> lookupFunction) {
		return add(RouterFunctions.resources(lookupFunction));
	}

	@Override
	public RouterFunctions.Builder nest(RequestPredicate predicate,
			Consumer<RouterFunctions.Builder> builderConsumer) {

		Assert.notNull(builderConsumer, "Consumer must not be null");

		RouterFunctionBuilder nestedBuilder = new RouterFunctionBuilder();
		builderConsumer.accept(nestedBuilder);
		RouterFunction<ServerResponse> nestedRoute = nestedBuilder.build();
		this.routerFunctions.add(RouterFunctions.nest(predicate, nestedRoute));
		return this;
	}

	@Override
	public RouterFunctions.Builder nest(RequestPredicate predicate,
			Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier) {

		Assert.notNull(routerFunctionSupplier, "RouterFunction Supplier must not be null");

		RouterFunction<ServerResponse> nestedRoute = routerFunctionSupplier.get();
		this.routerFunctions.add(RouterFunctions.nest(predicate, nestedRoute));
		return this;
	}

	@Override
	public RouterFunctions.Builder path(String pattern,
			Consumer<RouterFunctions.Builder> builderConsumer) {

		return nest(RequestPredicates.path(pattern), builderConsumer);
	}

	@Override
	public RouterFunctions.Builder path(String pattern,
			Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier) {

		return nest(RequestPredicates.path(pattern), routerFunctionSupplier);
	}

	@Override
	public RouterFunctions.Builder filter(HandlerFilterFunction<ServerResponse, ServerResponse> filterFunction) {
		Assert.notNull(filterFunction, "HandlerFilterFunction must not be null");

		this.filterFunctions.add(filterFunction);
		return this;
	}

	@Override
	public RouterFunctions.Builder before(Function<ServerRequest, ServerRequest> requestProcessor) {
		Assert.notNull(requestProcessor, "RequestProcessor must not be null");
		return filter((request, next) -> next.handle(requestProcessor.apply(request)));
	}

	@Override
	public RouterFunctions.Builder after(
			BiFunction<ServerRequest, ServerResponse, ServerResponse> responseProcessor) {

		Assert.notNull(responseProcessor, "ResponseProcessor must not be null");
		return filter((request, next) -> next.handle(request)
				.map(serverResponse -> responseProcessor.apply(request, serverResponse)));
	}

	@Override
	public RouterFunctions.Builder onError(Predicate<? super Throwable> predicate,
			BiFunction<? super Throwable, ServerRequest, Mono<ServerResponse>> responseProvider) {

		Assert.notNull(predicate, "Predicate must not be null");
		Assert.notNull(responseProvider, "ResponseProvider must not be null");

		return filter((request, next) -> next.handle(request)
				.onErrorResume(predicate, t -> responseProvider.apply(t, request)));
	}

	@Override
	public <T extends Throwable> RouterFunctions.Builder onError(Class<T> exceptionType,
			BiFunction<? super T, ServerRequest, Mono<ServerResponse>> responseProvider) {

		Assert.notNull(exceptionType, "ExceptionType must not be null");
		Assert.notNull(responseProvider, "ResponseProvider must not be null");

		return filter((request, next) -> next.handle(request)
				.onErrorResume(exceptionType, t -> responseProvider.apply(t, request)));
	}

	@Override
	public RouterFunction<ServerResponse> build() {
		RouterFunction<ServerResponse> result = this.routerFunctions.stream()
				.reduce(RouterFunction::and)
				.orElseThrow(IllegalStateException::new);

		if (this.filterFunctions.isEmpty()) {
			return result;
		}
		else {
			HandlerFilterFunction<ServerResponse, ServerResponse> filter =
					this.filterFunctions.stream()
							.reduce(HandlerFilterFunction::andThen)
							.orElseThrow(IllegalStateException::new);

			return result.filter(filter);
		}
	}

}
