import org.springframework.beans.factory.getBean
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.reactive.result.view.script.RenderingContext
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings

fun render(template: String, model: Map<String, Any>, renderingContext: RenderingContext): String {
	val engine = ScriptEngineManager().getEngineByName("kotlin")
	val bindings = SimpleBindings()
	bindings.putAll(model)
	var messageSource = renderingContext.applicationContext.getBean<ResourceBundleMessageSource>()
	bindings.put("i18n", { code: String -> messageSource.getMessage(code, null, renderingContext.locale) })
	bindings.put("include", { path: String -> renderingContext.templateLoader.apply("org/springframework/web/reactive/result/view/script/kotlin/$path.html") })
	return engine.eval(template, bindings) as String
}

