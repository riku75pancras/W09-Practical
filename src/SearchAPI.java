import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SearchAPI {

    public void startFunctions(String searchCategory, String queryString, String cacheDirectoryPath) throws Exception {
        searchCategory = convertCategoryIntoURL(searchCategory);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://dblp.org/search/");
        stringBuilder.append(searchCategory);
        stringBuilder.append("/api?q=");
        stringBuilder.append(URLEncoder.encode(queryString, "UTF-8"));
        stringBuilder.append("&format=xml");
        stringBuilder.append("&h=40");
        stringBuilder.append("&c=0");

        String urlString = stringBuilder.toString();
        URL url = new URL(urlString);

        Document doc = createDocument(url);

        if (searchCategory.equals("publ")) {
            printPublicationInfo(doc);
        } else if (searchCategory.equals("author")) {
            printAuthorInfo(doc);
        } else {
            printVenueInfo(doc);
        }


    }

    private String convertCategoryIntoURL(String searchCategory) {
        if (searchCategory.equals("publication")) {
            searchCategory = "publ";
        }

        return searchCategory;
    }

    private Document createDocument(URL url) throws IOException, ParserConfigurationException, SAXException {
        InputStream inputStream = url.openStream();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();

        return doc;
    }

    private void printPublicationInfo(Document doc) throws XPathExpressionException {
        NodeList publications = doc.getElementsByTagName("info");

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        XPathExpression xpeTitle = xp.compile("title");
        XPathExpression xpeAuthor = xp.compile("authors/author");

        for (int i = 0; i < publications.getLength(); i++){
            String title = (String) xpeTitle.evaluate(publications.item(i), XPathConstants.STRING);
            NodeList authorList = (NodeList) xpeAuthor.evaluate(publications.item(i), XPathConstants.NODESET);


            System.out.println(title + " (number of authors: " + authorList.getLength() + ")");
        }
    }

    private void printAuthorInfo(Document doc) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        NodeList authors = doc.getElementsByTagName("info");

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        XPathExpression xpeURL = xp.compile("url");
        XPathExpression xpeAuthor = xp.compile("author");

        for (int i = 0; i < authors.getLength(); i++) {
            String title = (String) xpeAuthor.evaluate(authors.item(i), XPathConstants.STRING);
            System.out.print(title + " - ");

            String url2 = (String) xpeURL.evaluate(authors.item(i), XPathConstants.STRING);
            URL url = new URL(url2 + ".xml");

            Document doc2 = createDocument(url);

            NodeList publicationsList = doc2.getElementsByTagName("r");

            System.out.print(publicationsList.getLength() + " publications with ");

            NodeList coauthorsList = doc2.getElementsByTagName("co");
            System.out.println(coauthorsList.getLength() + " publications");

        }

    }

    private void printVenueInfo(Document doc) {
        NodeList venues = doc.getElementsByTagName("venue");

        for (int i = 0; i < venues.getLength(); i++) {
            Element e = (Element) venues.item(i);
            System.out.println(e.getTextContent());
        }


    }
}
