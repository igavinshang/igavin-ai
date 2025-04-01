
package org.igavin.ai.springai.controller;


import org.igavin.ai.springai.rag.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/springai")
public class SpringAIRagController {

	private final RagService localRagService;

	public SpringAIRagController(RagService localRagService) {
		this.localRagService = localRagService;
	}

	@GetMapping("/rag/importDocument")
	public void importDocument() {
		localRagService.importDocuments();
	}

	@GetMapping("/rag")
	public Flux<String> generate(@RequestParam(value = "message") String message) {
		return localRagService.retrieve(message)
				.map(x -> x.getResult().getOutput().getText());
	}

}
