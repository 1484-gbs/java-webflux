package com.example.webflux.usecase;

import com.example.webflux.repository.WebClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.w3c.dom.Element;
import reactor.core.publisher.Mono;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.IntStream;

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
            info.setAttribute("value", "1234");
//            info.setAttribute("value2", "20241018100000000946");
            info.setAttribute("value2", "00");
            root.appendChild(info);

            val info2 = document.createElement("hoge");
            info2.setAttribute("value", "1234");
            info2.setAttribute("value2", "02");
            root.appendChild(info2);
            root.setAttribute("test", "oraora");
            writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            log.info(writer.toString());

            val nodeList2 = root.getElementsByTagName("hoge");
            val document2 = dom.createDocument("", "test", null);
            val test = document2.importNode(document.getDocumentElement(), false);
            document2.getDocumentElement().appendChild(test);

            writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            log.info(writer.toString());


            IntStream.range(0, nodeList2.getLength()).mapToObj(nodeList2::item)
                    .sorted(Comparator.comparing(item -> ((Element) item).getAttribute("value"))
                            .thenComparing(item -> ((Element) item).getAttribute("value2")).reversed())
                    .forEach(item -> {
                        val hoge = document2.importNode(item, false);
                        test.appendChild(hoge);
                    });

            String aaa = IntStream.range(0, nodeList2.getLength()).mapToObj(nodeList2::item)
                    .filter(item -> Objects.equals(((Element) item).getAttribute("value"), "hoge"))
                    .max(Comparator.comparing(item -> ((Element) item).getAttribute("value"))
                            .thenComparing(item -> ((Element) item).getAttribute("value2")))
                    .map(item -> "hoge")
                    .orElseGet(() ->
                            IntStream.range(0, nodeList2.getLength()).mapToObj(nodeList2::item)
                                    .filter(item -> Objects.equals(((Element) item).getAttribute("value"), "hoge2"))
                                    .findFirst()
                                    .map(item -> "unko")
                                    .orElse("aiueo")
                    );

            val writer2 = new StringWriter();
            transformer.transform(new DOMSource(document2), new StreamResult(writer2));
            log.info(writer2.toString());

            val nodeList = document.getElementsByTagName("test");
            val testPrice = Objects.isNull(nodeList)
                    ? BigDecimal.ZERO
                    : IntStream.range(0, nodeList.getLength())
                    .mapToObj(nodeList::item)
                    .filter(item -> Objects.equals(((Element) item).getAttribute("testNo"), "testNo"))
                    .map(item -> {
                        log.info(((Element) item).getAttribute("testNo"));
                        val price = ((Element) item).getAttribute("price");
                        log.info(price);
                        return StringUtils.hasText(price)
                                ? new BigDecimal(price)
                                : BigDecimal.ZERO;
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return repository.get(req, "/health", String.class);
    }
}
