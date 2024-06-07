package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@Service
@AllArgsConstructor
@Slf4j
public class HealthUseCase {
    private final WebClientRepository<Void, String> repository;

    public Mono<String> execute(ServerRequest req) {
        try {
            val factory = DocumentBuilderFactory.newInstance();
            val builder = factory.newDocumentBuilder();

            val dom = builder.getDOMImplementation();
            val document = dom.createDocument("", "rootelem", null);
            // root取得
            val root = document.getDocumentElement();

            val tranFactory = TransformerFactory.newInstance();
            val transformer = tranFactory.newTransformer();
            var writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            log.info(writer.toString());

            writer = new StringWriter();
            transformer.transform(new DOMSource(root), new StreamResult(writer));
            log.info(writer.toString());

            val info = document.createElement("hoge");
            info.setAttribute("value", "xml_to_string");
            root.appendChild(info);

            writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            log.info(writer.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return repository.get(req, "/health", String.class);
    }
}
